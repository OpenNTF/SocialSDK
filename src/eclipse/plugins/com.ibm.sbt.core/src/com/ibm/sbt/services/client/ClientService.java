/*
 * © Copyright IBM Corp. 2012
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.services.client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Node;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.NoAccessSignal;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.FastStringBuffer;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonFactory;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.commons.xml.util.XMIConverter;
import com.ibm.sbt.plugin.SbtCoreLogger;
import com.ibm.sbt.service.debug.ProxyDebugUtil;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.endpoints.SmartCloudFormEndpoint;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * Base class for a REST service client.
 * 
 * @author Philippe Riand
 * @author Mark Wallace
 */
public abstract class ClientService {

	protected Endpoint endpoint;

	// Constants for the methods
	public static final String METHOD_GET = "get"; //$NON-NLS-1$
	public static final String METHOD_PUT = "put"; //$NON-NLS-1$
	public static final String METHOD_POST = "post"; //$NON-NLS-1$
	public static final String METHOD_DELETE = "delete"; //$NON-NLS-1$

	// These represents how the result should be formatted to the caller
	public static final Handler FORMAT_UNKNOWN = null;
	public static final Handler FORMAT_NULL = new HandlerNull();
	public static final Handler FORMAT_TEXT = new HandlerString();
	public static final Handler FORMAT_INPUTSTREAM = new HandlerInputStream();
	public static final Handler FORMAT_XML = new HandlerXml();
	public static final Handler FORMAT_JSON = new HandlerJson();

	// TODO: Should be moved elsewhere? What is it for?
	public static final Handler FORMAT_CONNECTIONS_OUTPUT = new HandlerConnectionHeader();
	
	private static final ProfilerType profilerRequest = new ProfilerType("Executing REST request, "); //$NON-NLS-1$

	private static final String sourceClass = ClientService.class.getName();
	private static final Logger logger = Logger.getLogger(sourceClass);

	/**
	 * Default constructor
	 */
	public ClientService() {
	}

	/**
	 * Construct a ClientService with the specified Endpoint
	 * 
	 * @param endpoint
	 */
	public ClientService(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * Construct a ClientService with the Endpoint with the specified name
	 * 
	 * @param endpoint
	 */
	public ClientService(String endpointName) {
		this.endpoint = EndpointFactory.getEndpoint(endpointName);
	}

	/**
	 * Return the associated Endpoint
	 * 
	 * @return
	 */
	public Endpoint getEndpoint() {
		return endpoint;
	}

	/**
	 * Set the associated Endpoint
	 * 
	 * @param endpoint
	 */
	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	/**
	 * If there is an associated endpoint then check is authentication required
	 * and if so trigger the authentication process.
	 * 
	 * @param args
	 * @throws ClientServicesException
	 */
	protected void checkAuthentication(Args args) throws ClientServicesException {
		if (endpoint != null) {
			if (endpoint.isRequiresAuthentication() && !endpoint.isAuthenticated()) {
				endpoint.authenticate(false);
			}
		}
	}

	/**
	 * Get the URL path for the specified arguments and check it is valid
	 * i.e. not null. If it is a null throw a ClientServicesException.
	 * 
	 * @param args
	 * @throws ClientServicesException
	 */
	protected void checkUrl(Args args) throws ClientServicesException {
		if (StringUtil.isEmpty(getUrlPath(args))) {
			throw new ClientServicesException(null, "The service URL is empty");
		}
	}

	/**
	 * Check the read parameters and throw a ClientServicesException if
	 * they are invalid.
	 * 
	 * @param parameters
	 * @throws ClientServicesException
	 */
	protected void checkReadParameters(Map<String, String> parameters) throws ClientServicesException {
		// nothing for now...
	}

	/**
	 * Return the URL from the associated endpoint.
	 * 
	 * @return
	 */
	protected String getBaseUrl() {
		if (endpoint != null) {
			return endpoint.getUrl();
		}
		return null;
	}

	/**
	 * Initialize the associated Endpoint with the specified HttpClient.
	 * 
	 * @param httpClient
	 * @throws ClientServicesException
	 */
	protected void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		if (endpoint != null) {
			endpoint.initialize(httpClient);
		}
	}

	/**
	 * Return true if force trust SSL certificate is set for the associated Endpoint.
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	protected boolean isForceTrustSSLCertificate() throws ClientServicesException {
		if (endpoint != null) {
			return endpoint.isForceTrustSSLCertificate();
		}
		return false;
	}

	/**
	 * Return the proxy info from the associated Endpoint or an empty string
	 * 
	 * @return
	 * @throws ClientServicesException
	 */
	protected String getHttpProxy() throws ClientServicesException {
		if (endpoint != null) {
			String proxyinfo = endpoint.getHttpProxy();
			if (StringUtil.isEmpty(proxyinfo)) {
				Context context = Context.getUnchecked();
		    	if (context != null) {
					proxyinfo = Context.get().getProperty("sbt.httpProxy");
		    	}
			}
			return proxyinfo;
		}
		return ""; // TODO should this be null?
	}

	/**
	 * Force authentication for the associated Endpoint.
	 * 
	 * @param args
	 * @throws ClientServicesException
	 */
	protected void forceAuthentication(Args args) throws ClientServicesException {
		if (endpoint != null) {
			endpoint.authenticate(true);
		} else {
			String msg = StringUtil.format("Authorization needed for service {0}", getUrlPath(args));
			throw new NoAccessSignal(msg);
		}
	}

	// =================================================================
	// Generic access
	// =================================================================

	public static class Args {

		private String serviceUrl; // Service URL to call, relative to the endpoint
		private Map<String, String> parameters; // Query String parameters
		private Map<String, String> headers; // HTTP Headers
		private Handler handler; // Format of the result

		public Args() {
		}

		public String getServiceUrl() {
			return serviceUrl;
		}

		public Args setServiceUrl(String url) {
			this.serviceUrl = url;
			return this;
		}

		public Map<String, String> getParameters() {
			return parameters;
		}

		public Args setParameters(Map<String, String> parameters) {
			this.parameters = parameters;
			return this;
		}

		public Args addParameter(String name, String value) {
			if (parameters == null) {
				this.parameters = new HashMap<String, String>();
			}
			parameters.put(name, value);
			return this;
		}

		public Map<String, String> getHeaders() {
			return headers;
		}

		public Args setHeaders(Map<String, String> headers) {
			this.headers = headers;
			return this;
		}

		public Args addHeader(String name, String value) {
			if (headers == null) {
				this.headers = new HashMap<String, String>();
			}
			headers.put(name, value);
			return this;
		}

		public Handler getHandler() {
			return handler;
		}

		public Args setHandler(Handler handler) {
			this.handler = handler;
			return this;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{ serviceUrl:").append(serviceUrl);
			sb.append(", parameters:").append(parameters);
			sb.append(", headers:").append(headers).append("}");
			return sb.toString();
		}
	}

	protected Args createArgs(String serviceUrl, Map<String, String> parameters)
			throws ClientServicesException {
		Args args = new Args();
		args.setServiceUrl(serviceUrl);
		args.setParameters(parameters);
		return args;
	}

	// =================================================================
	// Request content
	// =================================================================

	public static abstract class Content {

		private final String	contentType;

		protected Content(String contentType) {
			this.contentType = contentType;
		}

		protected String getContentType() {
			return contentType;
		}

		public void initRequestContent(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args)
				throws ClientServicesException {
			HttpEntity entity = createEntity();

			// set the http entity to the request, along with its content type
			if (entity != null && (httpRequestBase instanceof HttpEntityEnclosingRequestBase)) {
				setEntity(httpClient, httpRequestBase, args, entity);
			}
		}

		protected void setEntity(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args,
				HttpEntity entity) throws ClientServicesException {
			String contentType = getContentType();
			if (StringUtil.isNotEmpty(contentType)) {
				httpRequestBase.setHeader("Content-Type", contentType);
			}
			((HttpEntityEnclosingRequestBase) httpRequestBase).setEntity(entity);
		}

		protected HttpEntity createEntity() throws ClientServicesException {
			return null;
		}
	}

	public static class ContentString extends Content {

		private final String	content;

		public ContentString(String content, String contentType) {
			super(contentType);
			this.content = content;
		}

		public ContentString(String content) {
			this(content, "text/plain");
		}

		@Override
		protected HttpEntity createEntity() throws ClientServicesException {
			try {
				return new StringEntity(content);
			} catch (Exception ex) {
				throw new ClientServicesException(ex);
			}
		}
	}

	public static class ContentJson extends Content {

		private final JsonFactory	factory;
		private final Object		content;

		public ContentJson(Object content, String contentType, JsonFactory factory) {
			super(contentType);
			this.content = content;
			this.factory = factory;
		}

		public ContentJson(Object content, String contentType) {
			this(content, contentType, JsonJavaFactory.instanceEx);
		}

		public ContentJson(Object content) {
			this(content, "application/json", JsonJavaFactory.instanceEx);
		}

		public ContentJson(Object content, JsonFactory factory) {
			this(content, "application/json", factory);
		}

		@Override
		protected HttpEntity createEntity() throws ClientServicesException {
			try {
				return new StringEntity(JsonGenerator.toJson(factory, content, true));
			} catch (Exception ex) {
				throw new ClientServicesException(ex);
			}
		}
	}

	public static class ContentXml extends Content {

		private Node	content;

		public ContentXml(Node content, String contentType) {
			super(contentType);
			this.content = content;
		}

		public ContentXml(Node content) {
			this(content, "application/xml");
			this.content = content;
		}

		@Override
		protected HttpEntity createEntity() throws ClientServicesException {
			try {
				return new StringEntity(DOMUtil.getXMLString(content, true));
			} catch (Exception ex) {
				throw new ClientServicesException(ex);
			}
		}
	}

	public static class ContentFile extends Content {

		private final File		content;
		private final String	name;

		public ContentFile(String name, File content, String contentType) {
			super(contentType);
			this.name = name;
			this.content = content;
		}

		public ContentFile(File content, String contentType) {
			this(content.getName(), content, contentType);
		}

		public ContentFile(File content) {
			this(content, "application/octet-stream");
		}

		public ContentFile(String name, File content) {
			this(name, content, "application/octet-stream");
		}

		@Override
		public HttpEntity createEntity() throws ClientServicesException {
			FileEntity fileEnt = new FileEntity(content, getContentType());
			fileEnt.setContentEncoding("binary"); // Is that OK?
			return fileEnt;
		}

		@Override
		protected void setEntity(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args,
				HttpEntity entity) throws ClientServicesException {
			httpRequestBase.setHeader("slug", name);
			httpRequestBase.setHeader("Content-Type", getContentType());
			super.setEntity(httpClient, httpRequestBase, args, entity);
		}
	}

	public static class ContentStream extends Content {

		private final long					length;
		private final java.io.InputStream	stream;
		private final String				name;
		private final boolean				markSupportedFromWrappedStream;

		public ContentStream(String name, InputStream stream, long length, String contentType) {
			super(contentType);
			this.length = length;
			this.markSupportedFromWrappedStream = stream.markSupported();
			if (stream instanceof BufferedInputStream) {
				this.stream = stream;
			} else {
				this.stream = new BufferedInputStream(stream);
			}
			if (!StringUtil.isEmpty(name)) {
				this.name = name.trim();
			} else {
				this.name = name;
			}
		}

		public ContentStream(InputStream stream) {
			this(null, stream, -1, "binary/octet-stream");
		}

		public ContentStream(java.io.InputStream stream, String name) {
			this(name, stream, -1, "binary/octet-stream");
		}

		public ContentStream(java.io.InputStream stream, long length, String name) {
			this(name, stream, length, "binary/octet-stream");
		}

		@Override
		protected HttpEntity createEntity() throws ClientServicesException {
			InputStreamEntity inputStreamEntity = new InputStreamEntity(stream, length);
			inputStreamEntity.setContentEncoding("binary");
			if (length == -1) {
				inputStreamEntity.setChunked(true);
			}

			return inputStreamEntity;
		}

		@Override
		public void initRequestContent(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args)
				throws ClientServicesException {
			// TODO Auto-generated method stub
			super.initRequestContent(httpClient, httpRequestBase, args);
		}

		@Override
		protected void setEntity(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args,
				HttpEntity entity) throws ClientServicesException {
			if (name != null) {
				httpRequestBase.setHeader("slug", name);
			}
			httpRequestBase.setHeader("Content-Type", getContentType());
			super.setEntity(httpClient, httpRequestBase, args, entity);
		}

	}

	protected Content createRequestContent(Args args, Object content) throws ClientServicesException {
		if (args.getHeaders() != null && args.getHeaders().get("Content-Type") != null) {
			String contentType = args.getHeaders().get("Content-Type");
			if (content instanceof String) {
				return new ContentString((String) content, contentType);
			}
			if (content instanceof Node) {
				return new ContentXml((Node) content, contentType);
			}
			if ((content instanceof JsonObject) || (content instanceof List)) {
				return new ContentJson(content, contentType);
			}
			if (content instanceof File) {
				return new ContentFile((File) content, contentType);
			}
			if (content instanceof InputStream) {
				return new ContentStream(args.getHeaders().get("Slug"), (InputStream) content, -1,
						contentType);
			}
		} else {
			if (content instanceof String) {
				return new ContentString((String) content);
			}
			if (content instanceof Node) {
				return new ContentXml((Node) content);
			}
			if ((content instanceof JsonObject) || (content instanceof List)) {
				return new ContentJson(content);
			}
			if (content instanceof File) {
				return new ContentFile((File) content);
			}
			if (content instanceof InputStream) {
				return new ContentStream((InputStream) content);
			}
		}

		throw new ClientServicesException(null, "Cannot create HTTP content for object of type {0}",
				content.getClass());
	}

	// =================================================================
	// Response Handler
	// =================================================================

	public static abstract class Handler {
		public abstract Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException;
	}

	public static class HandlerNull extends Handler {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			if (entity != null) {
				InputStream is = getEntityContent(request, response, entity);
				try {
					// Just eat the entire content - requested for persistent http 1.1 sessions
					byte[] buffer = new byte[8192];
					while ((is.read(buffer)) > 0) {}
				} finally {}
			}
			return null;
		}
	}

	public static class HandlerString extends Handler {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			if (entity != null) {
				String encoding = EntityUtils.getContentCharSet(entity);
				if (encoding == null) {
					encoding = "UTF-8";
				}
				Reader reader = new InputStreamReader(getEntityContent(request, response, entity), encoding);
				try {
					FastStringBuffer b = new FastStringBuffer();
					b.append(reader);
					return b.toString();
				} finally {
					reader.close();
				}
			}
			return null;
		}
	}

	public static class HandlerJson extends Handler {
		private final JsonFactory	factory;

		public HandlerJson() {
			this.factory = JsonJavaFactory.instanceEx;
		}

		public HandlerJson(JsonFactory factory) {
			this.factory = factory;
		}

		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			if (entity != null) {
				String encoding = EntityUtils.getContentCharSet(entity);
				if (encoding == null) {
					encoding = "UTF-8";
				}
				Reader reader = createReader(request, response, entity, encoding);
				try {
					return JsonParser.fromJson(factory, reader);
				} catch (JsonException ex) {
					IOException e = new IOException();
					e.initCause(ex);
					throw e;
				} finally {
					reader.close();
				}
			}
			return null;
		}

		protected Reader createReader(HttpRequestBase request, HttpResponse response, HttpEntity entity,
				String encoding) throws IOException {
			return new InputStreamReader(getEntityContent(request, response, entity), encoding);
		}
	}

	public static class HandlerXml extends Handler {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			if (entity != null) {
				InputStream is = getEntityContent(request, response, entity);
				try {
					return DOMUtil.createDocument(is);
				} catch (XMLException ex) {
					IOException e = new IOException();
					e.initCause(ex);
					throw e;
				} finally {
					is.close();
				}
			}
			return null;
		}
	}

	public static class HandlerConnectionHeader extends Handler {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			Header[] headers = response.getHeaders("Location");
			if (headers != null) {
				if (headers.length > 0) {
					return headers[0].getValue();
				}
			}
			return null;
		}
	}

	public static class HandlerInputStream extends Handler {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			if (entity != null) {
				InputStream is = getEntityContent(request, response, entity);
				return is;
			}
			return null;
		}
	}

	public static class HandlerRaw extends Handler {
		@Override
		public Object parseContent(HttpRequestBase request, HttpResponse response, HttpEntity entity)
				throws ClientServicesException, IOException {
			return response;
		}
	}

	protected Handler findErrorHandler(HttpRequestBase request, HttpResponse response)
			throws ClientServicesException, UnsupportedEncodingException, IOException {
		throwClientServicesException(request, response);
		return null;
	}
	
	protected Handler findSuccessHandler(HttpRequestBase request, HttpResponse response)
			throws UnsupportedEncodingException, IOException {
		HttpEntity entity = response.getEntity();
		if (entity != null) {
			Header hd = entity.getContentType();
			if (hd != null) {
				String ct = hd.getValue();
				if (ct.indexOf("json") >= 0) {
					return FORMAT_JSON;
				}
				if (ct.indexOf("xml") >= 0) {
					return FORMAT_XML;
				}
			}
		}
		return getDefaultFormat(response, entity);
	}

	// =================================================================
	// GET
	// =================================================================

	public final Response get(String serviceUrl) throws ClientServicesException { 
		return get(serviceUrl, null, null);
	}

	public final Response get(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
		return get(serviceUrl, parameters, null);
	}

	public final Response get(String serviceUrl, Handler format) throws ClientServicesException {
		return get(serviceUrl, null, format);
	}

	public final Response get(String serviceUrl, Map<String, String> parameters, Handler format)
			throws ClientServicesException {
		return get(serviceUrl, parameters, null, format);
	}

	public final Response get(String serviceUrl, Map<String, String> parameters, Map<String, String> headers,
			Handler format) throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		if (headers != null) {
			args.setHeaders(headers);
		}
		args.setHandler(format);
		return get(args);
	}

	public final Response get(Args args) throws ClientServicesException {
		return xhr(METHOD_GET, args, null);
	}

	// =================================================================
	// POST
	// =================================================================

	public final Response post(String serviceUrl, Object content) throws ClientServicesException {
		return post(serviceUrl, null, content, null);
	}

	public final Response post(String serviceUrl, Map<String, String> parameters, Object content)
			throws ClientServicesException {
		return post(serviceUrl, parameters, content, null);
	}

	public final Response post(String serviceUrl, Map<String, String> parameters, Object content, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return post(args, content);
	}

	public final Response post(String serviceUrl, Map<String, String> parameters, Map<String, String> headers,
			Object content, Handler format) throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		args.setHeaders(headers);
		return post(args, content);
	}

	public final Response post(Args args, Object content) throws ClientServicesException {
		return xhr(METHOD_POST, args, content);
	}

	// =================================================================
	// PUT
	// =================================================================

	public final Response put(String serviceUrl, Object content) throws ClientServicesException {
		return put(serviceUrl, null, content, null);
	}

	public final Response put(String serviceUrl, Map<String, String> parameters, Object content)
			throws ClientServicesException {
		return put(serviceUrl, parameters, content, null);
	}

	public final Response put(String serviceUrl, Map<String, String> parameters, Object content, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return put(args, content);
	}

	public final Response put(String serviceUrl, Map<String, String> parameters, Map<String, String> headers,
			Object content, Handler format) throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		args.setHeaders(headers);
		return put(args, content);
	}

	public final Response put(Args args, Object content) throws ClientServicesException {
		return xhr(METHOD_PUT, args, content);
	}

	// =================================================================
	// DELETE
	// =================================================================

	public final Response delete(String serviceUrl) throws ClientServicesException {
		return delete(serviceUrl, null, null);
	}

	public final Response delete(String serviceUrl, Map<String, String> parameters)
			throws ClientServicesException {
		return delete(serviceUrl, parameters, null);
	}

	public final Response delete(String serviceUrl, Map<String, String> parameters, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return delete(args);
	}
    public final Response delete(String serviceUrl, Map<String, String> parameters, Map<String, String> headers,
            Handler format) throws ClientServicesException {
        Args args = createArgs(serviceUrl, parameters);
        args.setHandler(format);
        args.setHeaders(headers);
        return delete(args);        
    }


	public final Response delete(Args args) throws ClientServicesException {
		return xhr(METHOD_DELETE, args, null);
	}

	// =================================================================
	// Actual request execution
	// =================================================================

	/**
	 * Execute an XML Http request with the specified arguments
	 * 
	 * @param method
	 * @param args
	 * @param content
	 * @return
	 * @throws ClientServicesException
	 */
	public Response xhr(String method, Args args, Object content) throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "xhr", new Object[] { method, args });
		}

		checkAuthentication(args);
		checkUrl(args);
		checkReadParameters(args.parameters);
		String url = composeRequestUrl(args);
		Response response = null;
		if (StringUtil.equalsIgnoreCase(method, METHOD_GET)) {
			HttpGet httpGet = new HttpGet(url);
			response = execRequest(httpGet, args, content);
		} else if (StringUtil.equalsIgnoreCase(method, METHOD_POST)) {
			HttpPost httpPost = new HttpPost(url);
			response = execRequest(httpPost, args, content);
		} else if (StringUtil.equalsIgnoreCase(method, "put")) {
			HttpPut httpPut = new HttpPut(url);
			response = execRequest(httpPut, args, content);
		} else if (StringUtil.equalsIgnoreCase(method, "delete")) {
			HttpDelete httpDelete = new HttpDelete(url);
			response = execRequest(httpDelete, args, content);
		} else {
			throw new ClientServicesException(null, "Unsupported HTTP method {0}", method);
		}
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "xhr", response);
		}
		return response;
	}

	/**
	 * Execute the specified HttpRequest
	 * 
	 * @param httpRequestBase
	 * @param args
	 * @param content
	 * @return
	 * @throws ClientServicesException
	 */
	protected Response execRequest(HttpRequestBase httpRequestBase, Args args, Object content) throws ClientServicesException {
		if (Profiler.isEnabled()) {
			String msg = httpRequestBase.getMethod().toUpperCase() + " " + getUrlPath(args);
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerRequest, msg);
			long ts = Profiler.getCurrentTime();
			try {
				return _xhr(httpRequestBase, args, content);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		} else {
			return _xhr(httpRequestBase, args, content);
		}
	}

	/**
	 * Allows clients to override the process content section of {@link #_xhr(HttpRequestBase, Args)}. <br/>
	 * 
	 * @param httpRequestBase
	 *            the base HTTP request created by the service
	 * @param content
	 *            the content that is to be sent via the request
	 * @param args
	 *            the args (such as url params) that have been pushed through by the calling service
	 * @return true if the client wants the super class to take care of processing the content
	 */
	protected Response _xhr(HttpRequestBase httpRequestBase, Args args, Object content) throws ClientServicesException {
		DefaultHttpClient httpClient = createHttpClient(httpRequestBase, args);
		initialize(httpClient);

		// HttpClient 4.1
		// httpClient.addRequestInterceptor(new RequestAcceptEncoding());
		// httpClient.addResponseInterceptor(new ResponseContentEncoding());

		Content reqContent = null;
		if (content != null) {
			if (content instanceof Content) {
				reqContent = (Content) content;
			} else {
				reqContent = createRequestContent(args, content);
			}
		}

		prepareRequest(httpClient, httpRequestBase, args, reqContent);

		HttpResponse response = executeRequest(httpClient, httpRequestBase, args);
		return processResponse(httpClient, httpRequestBase, response, args);
	}

	protected void prepareRequest(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args,
			Content content) throws ClientServicesException {
		// TODO: add support for gzip content
		// httpClient.addRequestHeader("Accept-Encoding", "gzip");

		if (args.getHeaders() != null) {
			addHeaders(httpClient, httpRequestBase, args);
		}
		if (content != null) {
			content.initRequestContent(httpClient, httpRequestBase, args);
		}
	}

	protected void addHeaders(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args) {
		for (Map.Entry<String, String> e : args.getHeaders().entrySet()) {
			String headerName = e.getKey();
			String headerValue = e.getValue();
			httpRequestBase.addHeader(headerName, headerValue);
		}
	}

	/**
	 * Execute the specified the request.
	 * 
	 * @param httpClient
	 * @param httpRequestBase
	 * @param args
	 * @return
	 * @throws ClientServicesException
	 */
	protected HttpResponse executeRequest(HttpClient httpClient, HttpRequestBase httpRequestBase, 
			Args args) throws ClientServicesException {
		try {
			return httpClient.execute(httpRequestBase);
		} catch (Exception ex) {
			if (logger.isLoggable(Level.FINE)) {
				String msg = "Exception ocurred while execuring request {0} {1}";
				msg = StringUtil.format(msg, httpRequestBase.getMethod(), args);
				logger.log(Level.FINE, msg, ex);
			}
			
			if (ex instanceof ClientServicesException) {
				throw (ClientServicesException) ex;
			}
			String msg = "Error while executing the REST service {0}";
			String param = httpRequestBase.getURI().toString();
			throw new ClientServicesException(ex, msg, param);
		}
	}

	/**
	 * Process the specified response
	 * 
	 * @param httpClient
	 * @param httpRequestBase
	 * @param httpResponse
	 * @param args
	 * @return
	 * @throws ClientServicesException
	 */
	protected Response processResponse(HttpClient httpClient, HttpRequestBase httpRequestBase,
			HttpResponse httpResponse, Args args) throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "processResponse", new Object[] { httpRequestBase.getURI(), httpResponse.getStatusLine() });
		}

		int statusCode = httpResponse.getStatusLine().getStatusCode();
		String reasonPhrase = httpResponse.getStatusLine().getReasonPhrase();
		if (!checkStatus(statusCode)) {
			if (SbtCoreLogger.SBT.isErrorEnabled()) {
				// Do not throw an exception here as some of the non OK responses are not error cases.
				String msg = "Client service request to: {0} did not return OK status. Status returned: {1}, reason: {2}, expected: {3}";
				msg = StringUtil.format(msg, httpRequestBase.getURI(), statusCode, reasonPhrase, HttpStatus.SC_OK);
				SbtCoreLogger.SBT.traceDebugp(this, "processResponse", msg);
			}
		}
		

		if(isResponseRequireAuthentication(httpResponse)){
			forceAuthentication(args);
			return null;
		}
		
/*		if ((httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED) || 
			(endpoint != null && endpoint.getAuthenticationErrorCode() == statusCode)) {
			forceAuthentication(args);
			return null;
		}*/
		
		Handler format = findHandler(httpRequestBase, httpResponse, args.handler);
		
		Response response = new Response(httpClient, httpResponse, httpRequestBase, args, format);

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "processResponse", response);
		}
		return response;
	}

	private boolean checkStatus(int statusCode) {
		if (statusCode >= 200 && statusCode < 300) {
			return true;
		}
		return false;
	}
	// Each endpoint provides its implementation whether an authentication is required based on response.
	protected boolean isResponseRequireAuthentication(HttpResponse httpResponse){
		int statusCode = httpResponse.getStatusLine().getStatusCode();
		if ((httpResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED) || 
				(endpoint != null && endpoint.getAuthenticationErrorCode() == statusCode)) {
				return true;
			}
		return false;
	}

	// =================================================================
	// URL composition
	// =================================================================

	protected String composeRequestUrl(Args args) throws ClientServicesException {
		// Compose the URL
		StringBuilder b = new StringBuilder(256);
		
		if(!(UrlUtil.isAbsoluteUrl(args.getServiceUrl()))){ // check if url supplied is absolute
			String url = getUrlPath(args);
			if (url.charAt(url.length() - 1) == '/') {
				url = url.substring(0, url.length() - 1);
			}
			b.append(url);
			addUrlParts(b, args);
		}else{
			// Calling app has provided the complete url, do not do url manipulation in clientservice
			b.append(args.getServiceUrl()); 
		}
		
		addUrlParameters(b, args);
		return b.toString();
	}

	protected String getUrlPath(Args args) {
		String baseUrl = getBaseUrl();
		String serviceUrl = args.getServiceUrl();
		serviceUrl = substituteServiceMapping(serviceUrl);
		return PathUtil.concat(baseUrl, serviceUrl, '/');
	}
	
	protected String substituteServiceMapping(String url){
        String regex = "\\{(.*?)\\}";
        
        Pattern paramsPattern = Pattern.compile(regex);
        Matcher paramsMatcher = paramsPattern.matcher(url);
        
        while(paramsMatcher.find()){
            String subOut = paramsMatcher.group(1);
            String subIn = this.endpoint.getServiceMappings().get(subOut);
            if(subIn != null){
                return url.replaceFirst("\\{" + subOut + "\\}", subIn);
            }
        }
        
        return url.replace("{", "").replace("}", "");
    }

	protected void addUrlParts(StringBuilder b, Args args) throws ClientServicesException {
	}

	protected void addUrlParameters(StringBuilder b, Args args) throws ClientServicesException {
		Map<String, String> parameters = args.getParameters();
		if (parameters != null) {
			boolean first = true;
			for (Map.Entry<String, String> e : parameters.entrySet()) {
				String name = e.getKey();
				if (StringUtil.isNotEmpty(name) && isValidUrlParameter(args, name)) {
					String value = e.getValue();
					first = addParameter(b, first, name, value);
				}
			}
		}
	}

	protected boolean isValidUrlParameter(Args args, String name) throws ClientServicesException {
		return true;
	}

	//
	// Url Utilities
	//
	protected boolean addParameter(StringBuilder b, boolean first, String name, Date value)
			throws ClientServicesException {
		if (value != null) {
			String date = XMIConverter.composeDate(value.getTime());
			return addParameter(b, first, name, date);
		}
		return first;
	}

	protected boolean addParameter(StringBuilder b, boolean first, String name, int value)
			throws ClientServicesException {
		if (value != 0) {
			return addParameter(b, first, name, Integer.toString(value));
		}
		return first;
	}

	protected boolean addParameter(StringBuilder b, boolean first, String name, String value)
			throws ClientServicesException {
		try {
			if (value != null) {
				b.append(first ? '?' : '&');
				b.append(name);
				b.append('=');
				b.append(URLEncoder.encode(value, "UTF-8"));
				return false;
			}
			return first;
		} catch (UnsupportedEncodingException ex) {
			throw new ClientServicesException(ex);
		}
	}

	// =================================================================
	// Content formatting
	// =================================================================

	protected Handler getDefaultFormat(HttpResponse response, HttpEntity entity) {
		return FORMAT_INPUTSTREAM;
	}

	/**
	 * Find the handler for the specified response
	 * 
	 * @param request
	 * @param response
	 * @param format
	 * @return
	 * @throws ClientServicesException
	 */
	protected Handler findHandler(HttpRequestBase request, HttpResponse response, Handler handler)
			throws ClientServicesException {
		if (logger.isLoggable(Level.FINEST)) {
			logger.entering(sourceClass, "findHandler", new Object[] { request.getURI(), response.getStatusLine(), handler });
		}

		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
				handler = findErrorHandler(request, response);
			}

			// Connections Delete API returns SC_NO_CONTENT for successful deletion.
			if (isErrorStatusCode(statusCode)) { 
				handler = findErrorHandler(request, response);
			}

			if (handler == null) {
				handler = findSuccessHandler(request, response);
			}

			// SBT doesn't have a JS interpreter...
			if (handler == null) {
				handler = new HandlerRaw();
			}
		} catch (Exception ex) {
			if (ex instanceof ClientServicesException) {
				throw (ClientServicesException) ex;
			}
			throw new ClientServicesException(ex, "Error while parsing the REST service results");
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.exiting(sourceClass, "findHandler", handler);
		}
		return handler;
	}

	/**
	 * @param statusCode
	 * @return
	 */
	protected boolean isErrorStatusCode(int statusCode) {
		return (statusCode != HttpStatus.SC_OK) && 
			(statusCode != HttpStatus.SC_CREATED) && 
			(statusCode != HttpStatus.SC_ACCEPTED) && 
			(statusCode != HttpStatus.SC_NO_CONTENT);
	}

	/**
	 * 
	 * @param request
	 * @param response
	 * @throws ClientServicesException
	 */
	protected void throwClientServicesException(HttpRequestBase request, HttpResponse response) throws ClientServicesException {
		throw new ClientServicesException(response, request);
	}

	// Until we move to HttpClient 4.1
	public static InputStream getEntityContent(HttpRequestBase request, HttpResponse response,
			HttpEntity entity) throws IOException {
		InputStream is = entity.getContent();
		if (is != null) {
			Header contentEncodingHeader = response.getFirstHeader("Content-Encoding");
			if (contentEncodingHeader != null && contentEncodingHeader.getValue().equalsIgnoreCase("gzip")) {
				is = new GZIPInputStream(is);
			}
		}
		return is;
	}

	public DefaultHttpClient createHttpClient(HttpRequestBase httpRequestBase, Args args)
			throws ClientServicesException {
		// Check if we should trust the HTTPS certificates
		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (isForceTrustSSLCertificate()) {
			// PHIL: we don't check the scheme here as the Apachae library will still verify the
			// certificate for some http requests...
			// String scheme = httpRequestBase.getURI().getScheme();
			// if(scheme!=null && scheme.equalsIgnoreCase("https")) {
			httpClient = SSLUtil.wrapHttpClient(httpClient);
			// }
		}
		// Capture network traffic through a network proxy like Fiddler or WireShark for debug purposes
		if (StringUtil.isNotEmpty(getHttpProxy())) {
			return ProxyDebugUtil.wrapHttpClient(httpClient, getHttpProxy());
		}

		return httpClient;
	}
}
