package com.ibm.sbt.opensocial.domino.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpVersion;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.RequestAddCookies;
import org.apache.http.client.protocol.ResponseProcessCookies;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.ProxySelectorRoutePlanner;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HttpContext;
import org.apache.shindig.common.Nullable;
import org.apache.shindig.common.logging.i18n.MessageKeys;
import org.apache.shindig.common.servlet.Authority;
import org.apache.shindig.common.uri.Uri;
import org.apache.shindig.gadgets.GadgetException;
import org.apache.shindig.gadgets.http.BasicHttpFetcher;
import org.apache.shindig.gadgets.http.HttpRequest;
import org.apache.shindig.gadgets.http.HttpResponse;
import org.apache.shindig.gadgets.http.HttpResponseBuilder;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.ibm.sbt.opensocial.domino.config.OpenSocialContainerConfig;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPoint;
import com.ibm.sbt.opensocial.domino.container.ContainerExtPointManager;

/**
 * A simple HTTP fetcher that will allow connections over SSL to untrusted servers.
 * This should not be allowed in production but for development use cases it is useful and saves time
 * and frustration.  For this reason we allow each container to specify whether they want to allow this.
 * For a container to allow unsecure connections they should add domino.allowuntrustedsslconnections and
 * set it to true in their container configuration.
 * There are some cases where we don't have the container id and when we don't we always default back to 
 * the BasicHttpFetcher, which does not allow unsecure connections.
 */
@Singleton
public class DominoHttpFetcher extends BasicHttpFetcher {

	private static final int DEFAULT_CONNECT_TIMEOUT_MS = 5000;
	private static final int DEFAULT_READ_TIMEOUT_MS = 5000;
	private static final int DEFAULT_MAX_OBJECT_SIZE = 0;  // no limit
	private static final long DEFAULT_SLOW_RESPONSE_WARNING = 10000;
	private static final String CLASS = DominoHttpFetcher.class.getName();

	private final ContainerExtPointManager manager;
	private final HttpClient trustAllFetcher;
	private final Logger LOG;
	private final Authority authority;
	// mutable fields must be volatile
	private volatile int maxObjSize;
	private volatile long slowResponseWarning;

	private final Set<Class<?>> TIMEOUT_EXCEPTIONS = ImmutableSet.<Class<?>>of(ConnectionPoolTimeoutException.class,
			SocketTimeoutException.class, SocketException.class, HttpHostConnectException.class, NoHttpResponseException.class,
			InterruptedException.class, UnknownHostException.class);

	static class GzipDecompressingEntity extends HttpEntityWrapper {
		public GzipDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent() throws IOException, IllegalStateException {
			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();

			return new GZIPInputStream(wrappedin);
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}
	}

	static class DeflateDecompressingEntity extends HttpEntityWrapper {
		public DeflateDecompressingEntity(final HttpEntity entity) {
			super(entity);
		}

		@Override
		public InputStream getContent()
				throws IOException, IllegalStateException {

			// the wrapped entity's getContent() decides about repeatability
			InputStream wrappedin = wrappedEntity.getContent();

			return new InflaterInputStream(wrappedin, new Inflater(true));
		}

		@Override
		public long getContentLength() {
			// length of ungzipped content is not known
			return -1;
		}
	}


	private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		@Override
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		@Override
		public void checkClientTrusted(X509Certificate[] certs, String authType) {
			// Do nothing
		}

		@Override
		public void checkServerTrusted(X509Certificate[] certs, String authType) {
			// Do nothing
		}
	} };

	/**
	 * Creates a new DominoHttpFetcher.
	 * @param basicHttpFetcherProxy The http proxy to use.
	 * @param manager The container config manager to use.
	 * @param log The logger.
	 * @param authority The current server authority.
	 */
	@Inject
	public DominoHttpFetcher(@Nullable @Named("org.apache.shindig.gadgets.http.basicHttpFetcherProxy")
	String basicHttpFetcherProxy, ContainerExtPointManager manager, Logger log, Authority authority) {
		this(DEFAULT_MAX_OBJECT_SIZE, DEFAULT_CONNECT_TIMEOUT_MS, DEFAULT_READ_TIMEOUT_MS,
				basicHttpFetcherProxy, manager, log, authority);
	}

	/**
	 * Creates a new DominoHttpFetcher.
	 * @param maxObjSize Maximum size, in bytes, of the object we will fetch, 0 if no limit..
	 * @param connectionTimeoutMs timeout, in milliseconds, for connecting to hosts.
	 * @param readTimeoutMs timeout, in millseconds, for unresponsive connections
	 * @param basicHttpFetcherProxy The http proxy to use.
	 * @param manager The container extension point manager.
	 * @param log The logger.
	 * @param authority The server authority.
	 */
	public DominoHttpFetcher(int maxObjSize, int connectionTimeoutMs,
			int readTimeoutMs, String basicHttpFetcherProxy, ContainerExtPointManager manager, Logger log, Authority authority) {
		super(maxObjSize, connectionTimeoutMs, readTimeoutMs, basicHttpFetcherProxy);
		final String method = "constructor";
		setMaxObjectSizeBytes(maxObjSize);
	    setSlowResponseWarning(DEFAULT_SLOW_RESPONSE_WARNING);
		this.manager = manager;
		this.LOG = log;
		this.authority = authority;
		Scheme sch = null;
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			SSLSocketFactory socketFactory = new SSLSocketFactory(sc, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			sch = new Scheme("https", 443, socketFactory);
		} catch(Exception e) {
			LOG.logp(Level.WARNING, CLASS, method, "Error creating scheme for HTTPs.  The default fetcher will be used.  " +
					"The default fetcher will not allow untrusted SSL connections.", e);
		}

		HttpParams params = new BasicHttpParams();

		HttpConnectionParams.setConnectionTimeout(params, connectionTimeoutMs);

		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setUserAgent(params, "Apache Shindig");
		HttpProtocolParams.setContentCharset(params, "UTF-8");

		HttpConnectionParams.setConnectionTimeout(params, connectionTimeoutMs);
		HttpConnectionParams.setSoTimeout(params, readTimeoutMs);
		HttpConnectionParams.setStaleCheckingEnabled(params, true);
		HttpConnectionParams.setSoReuseaddr(params, true);

		HttpClientParams.setRedirecting(params, true);
		HttpClientParams.setAuthenticating(params, false);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		if(sch == null) {
			schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
		} else {
			schemeRegistry.register(sch);
		}

		ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
		// These are probably overkill for most sites.
		cm.setMaxTotal(1152);
		cm.setDefaultMaxPerRoute(256);

		DefaultHttpClient client = new DefaultHttpClient(cm, params);

		// Set proxy if set via guice.
		if (!Strings.isNullOrEmpty(basicHttpFetcherProxy)) {
			String[] splits = StringUtils.split(basicHttpFetcherProxy, ':');
			ConnRouteParams.setDefaultProxy(
					client.getParams(), new HttpHost(splits[0], Integer.parseInt(splits[1]), "http"));
		}

		// try resending the request once
		client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(1, true));

		// Add hooks for gzip/deflate
		client.addRequestInterceptor(new HttpRequestInterceptor() {
			public void process(
					final org.apache.http.HttpRequest request,
					final HttpContext context) throws HttpException, IOException {
				if (!request.containsHeader("Accept-Encoding")) {
					request.addHeader("Accept-Encoding", "gzip, deflate");
				}
			}
		});
		client.addResponseInterceptor(new HttpResponseInterceptor() {
			public void process(
					final org.apache.http.HttpResponse response,
					final HttpContext context) throws HttpException, IOException {
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					Header ceheader = entity.getContentEncoding();
					if (ceheader != null) {
						for (HeaderElement codec : ceheader.getElements()) {
							String codecname = codec.getName();
							if ("gzip".equalsIgnoreCase(codecname)) {
								response.setEntity(
										new GzipDecompressingEntity(response.getEntity()));
								return;
							} else if ("deflate".equals(codecname)) {
								response.setEntity(new DeflateDecompressingEntity(response.getEntity()));
								return;
							}
						}
					}
				}
			}
		});
		client.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler() );

		// Disable automatic storage and sending of cookies (see SHINDIG-1382)
		client.removeRequestInterceptorByClass(RequestAddCookies.class);
		client.removeResponseInterceptorByClass(ResponseProcessCookies.class);

		// Use Java's built-in proxy logic in case no proxy set via guice.
		if (Strings.isNullOrEmpty(basicHttpFetcherProxy)) {
			ProxySelectorRoutePlanner routePlanner = new ProxySelectorRoutePlanner(
					client.getConnectionManager().getSchemeRegistry(),
					ProxySelector.getDefault());
			client.setRoutePlanner(routePlanner);
		}

		trustAllFetcher = client;
	}

	@Override
	public HttpResponse fetch(HttpRequest request) throws GadgetException {
		String container = request.getContainer();
		ContainerExtPoint extpt = manager.getExtPoint(container);
		if(extpt != null) {
			OpenSocialContainerConfig config = extpt.getContainerConfig();
			Object allowedObj = config.getProperties().get(
					OpenSocialContainerConfig.ALLOW_UNTRUSTED_SSL_CONNECTIONS);
			if(allowedObj instanceof Boolean) {
				Boolean allowed = (Boolean)allowedObj;
				if(allowed) {
					return makeUnsecureFetch(request);
				}
			}
		}
		if(connectingToSelf(request)) {
			return makeUnsecureFetch(request);
		}
		return super.fetch(request);
	}
	
	private boolean connectingToSelf(HttpRequest request) {
		final String method = "connectingToSelf";
		try {
			String requestHost = request.getUri().toJavaUri().getHost();
			String authHost = new URL(authority.getOrigin()).getHost();
			return requestHost.equalsIgnoreCase(authHost);
		} catch (MalformedURLException e) {
			LOG.logp(Level.WARNING, CLASS, method, "Error constuction URL from the authority's origin. Value: " 
					+ authority.getOrigin(), e);
			return false;
		}
	}

	private HttpResponse makeUnsecureFetch(HttpRequest request) throws GadgetException { 
		HttpUriRequest httpMethod = null;
		Preconditions.checkNotNull(request);
		final String methodType = request.getMethod();

		final org.apache.http.HttpResponse response;
		final long started = System.currentTimeMillis();

		// Break the request Uri to its components:
		Uri uri = request.getUri();
		if (Strings.isNullOrEmpty(uri.getAuthority())) {
			throw new GadgetException(GadgetException.Code.INVALID_USER_DATA,
					"Missing domain name for request: " + uri,
					HttpServletResponse.SC_BAD_REQUEST);
		}
		if (Strings.isNullOrEmpty(uri.getScheme())) {
			throw new GadgetException(GadgetException.Code.INVALID_USER_DATA,
					"Missing schema for request: " + uri,
					HttpServletResponse.SC_BAD_REQUEST);
		}
		String[] hostparts = StringUtils.splitPreserveAllTokens(uri.getAuthority(),':');
		int port = -1; // default port
		if (hostparts.length > 2) {
			throw new GadgetException(GadgetException.Code.INVALID_USER_DATA,
					"Bad host name in request: " + uri.getAuthority(),
					HttpServletResponse.SC_BAD_REQUEST);
		}
		if (hostparts.length == 2) {
			try {
				port = Integer.parseInt(hostparts[1]);
			} catch (NumberFormatException e) {
				throw new GadgetException(GadgetException.Code.INVALID_USER_DATA,
						"Bad port number in request: " + uri.getAuthority(),
						HttpServletResponse.SC_BAD_REQUEST);
			}
		}

		String requestUri = uri.getPath();
		// Treat path as / if set as null.
		if (uri.getPath() == null) {
			requestUri = "/";
		}
		if (uri.getQuery() != null) {
			requestUri += '?' + uri.getQuery();
		}

		// Get the http host to connect to.
		HttpHost host = new HttpHost(hostparts[0], port, uri.getScheme());

		try {
			if ("POST".equals(methodType) || "PUT".equals(methodType)) {
				HttpEntityEnclosingRequestBase enclosingMethod = ("POST".equals(methodType))
						? new HttpPost(requestUri)
				: new HttpPut(requestUri);

						if (request.getPostBodyLength() > 0) {
							enclosingMethod.setEntity(new InputStreamEntity(request.getPostBody(), request.getPostBodyLength()));
						}
						httpMethod = enclosingMethod;
			} else if ("GET".equals(methodType)) {
				httpMethod = new HttpGet(requestUri);
			} else if ("HEAD".equals(methodType)) {
				httpMethod = new HttpHead(requestUri);
			} else if ("DELETE".equals(methodType)) {
				httpMethod = new HttpDelete(requestUri);
			}
			for (Map.Entry<String, List<String>> entry : request.getHeaders().entrySet()) {
				httpMethod.addHeader(entry.getKey(), Joiner.on(',').join(entry.getValue()));
			}

			// Disable following redirects.
			if (!request.getFollowRedirects()) {
				httpMethod.getParams().setBooleanParameter(ClientPNames.HANDLE_REDIRECTS, false);
			}

			// HttpClient doesn't handle all cases when breaking url (specifically '_' in domain)
			// So lets pass it the url parsed:
			response = trustAllFetcher.execute(host, httpMethod);

			if (response == null) {
				throw new IOException("Unknown problem with request");
			}

			long now = System.currentTimeMillis();
			if (now - started > slowResponseWarning) {
				slowResponseWarning(request, started, now);
			}

			return makeResponse(response);

		} catch (Exception e) {
			long now = System.currentTimeMillis();

			// Find timeout exceptions, respond accordingly
			if (TIMEOUT_EXCEPTIONS.contains(e.getClass())) {
				if (LOG.isLoggable(Level.INFO)) {
					LOG.logp(Level.INFO, CLASS, "fetch", MessageKeys.TIMEOUT_EXCEPTION, new Object[] {request.getUri(),CLASS,e.getMessage(),now-started});
				}
				return HttpResponse.timeout();
			}
			if (LOG.isLoggable(Level.INFO)) {
				LOG.logp(Level.INFO, CLASS, "fetch", MessageKeys.EXCEPTION_OCCURRED, new Object[] {request.getUri(),now-started});
				LOG.logp(Level.INFO, CLASS, "fetch", "", e);
			}
			// Separate shindig error from external error
			throw new GadgetException(GadgetException.Code.INTERNAL_SERVER_ERROR, e,
					HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		} finally {
			// cleanup any outstanding resources..
			if (httpMethod != null) try {
				httpMethod.abort();
			} catch (UnsupportedOperationException e) {
				// ignore
			}
		}
	}

	/**
	 * @param response The response to parse
	 * @return A HttpResponse object made by consuming the response of the
	 *         given HttpMethod.
	 * @throws IOException when problems occur processing the body content
	 */
	private HttpResponse makeResponse(org.apache.http.HttpResponse response) throws IOException {
		HttpResponseBuilder builder = new HttpResponseBuilder();

		if (response.getAllHeaders() != null) {
			for (Header h : response.getAllHeaders()) {
				if (h.getName() != null)
					builder.addHeader(h.getName(), h.getValue());
			}
		}

		HttpEntity entity = response.getEntity();

		if (maxObjSize > 0 && entity != null && entity.getContentLength() > maxObjSize) {
			return HttpResponse.badrequest("Exceeded maximum number of bytes - " + maxObjSize);
		}

		byte[] responseBytes = (entity == null) ? null : toByteArraySafe(entity);

		return builder
				.setHttpStatusCode(response.getStatusLine().getStatusCode())
				.setResponse(responseBytes)
				.create();
	}

	/**
	 * Change the global maximum fetch size (in bytes) for all fetches.
	 *
	 * @param maxObjectSizeBytes value for maximum number of bytes, or 0 for no limit
	 */
	@Inject(optional = true)
	@Override
	public void setMaxObjectSizeBytes(@Named("shindig.http.client.max-object-size-bytes") int maxObjectSizeBytes) {
		super.setMaxObjectSizeBytes(maxObjectSizeBytes);
		this.maxObjSize = maxObjectSizeBytes;
	}

	/**
	 * Change the global threshold for warning about slow responses
	 *
	 * @param slowResponseWarning time in milliseconds after we issue a warning
	 */

	@Inject(optional = true)
	@Override
	public void setSlowResponseWarning(@Named("shindig.http.client.slow-response-warning") long slowResponseWarning) {
		super.setSlowResponseWarning(slowResponseWarning);
		this.slowResponseWarning = slowResponseWarning;
	}

	/**
	 * Change the global connection timeout for all new fetchs.
	 *
	 * @param connectionTimeoutMs new connection timeout in milliseconds
	 */
	@Inject(optional = true)
	@Override
	public void setConnectionTimeoutMs(@Named("shindig.http.client.connection-timeout-ms") int connectionTimeoutMs) {
		super.setConnectionTimeoutMs(connectionTimeoutMs);
		trustAllFetcher.getParams().setIntParameter(HttpConnectionParams.CONNECTION_TIMEOUT, connectionTimeoutMs);
	}

	/**
	 * Change the global read timeout for all new fetchs.
	 *
	 * @param readTimeoutMs new connection timeout in milliseconds
	 */
	@Inject(optional = true)
	@Override
	public void setReadTimeoutMs(@Named("shindig.http.client.read-timeout-ms") int readTimeoutMs) {
		super.setConnectionTimeoutMs(readTimeoutMs);
		trustAllFetcher.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT, readTimeoutMs);
	}
}
