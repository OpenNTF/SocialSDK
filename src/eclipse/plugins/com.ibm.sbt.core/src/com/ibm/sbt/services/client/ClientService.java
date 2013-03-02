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
import com.ibm.commons.runtime.NoAccessSignal;
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
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.SSLUtil;
import com.ibm.sbt.util.SBTException;

/**
 * Base class for a REST service client.
 * 
 * @author Philippe Riand
 */
public abstract class ClientService {

	private static final ProfilerType	profilerRequest				= new ProfilerType(
																			"Executing REST request, ");	//$NON-NLS-1$

	// Constants for the methods
	public static final String			METHOD_GET					= "get";
	public static final String			METHOD_PUT					= "put";
	public static final String			METHOD_POST					= "post";
	public static final String			METHOD_DELETE				= "delete";

	// These represents how the result should be formatted to the caller
	public static final Handler			FORMAT_UNKNOWN				= null;
	public static final Handler			FORMAT_NULL					= new HandlerNull();
	public static final Handler			FORMAT_TEXT					= new HandlerString();
	public static final Handler			FORMAT_INPUTSTREAM			= new HandlerInputStream();
	public static final Handler			FORMAT_XML					= new HandlerXml();
	public static final Handler			FORMAT_JSON					= new HandlerJson();

	// TODO:
	// ?? Should be moved elsewhere?
	// ?? What is it for?
	public static final Handler			FORMAT_CONNECTIONS_OUTPUT	= new HandlerConnectionHeader();

	private Endpoint					endpoint;

	public ClientService() {
	}

	public ClientService(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	public ClientService(String endpointName) {
		this.endpoint = EndpointFactory.getEndpoint(endpointName);
	}

	public Endpoint getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(Endpoint endpoint) {
		this.endpoint = endpoint;
	}

	protected void checkAuthentication(Args args) throws ClientServicesException {
		if (endpoint != null) {
			if (endpoint.isRequiresAuthentication() && !endpoint.isAuthenticated()) {
				endpoint.authenticate(false);
			}
		}
	}

	protected void checkUrl(Args args) throws ClientServicesException {
		if (StringUtil.isEmpty(getUrlPath(args))) {
			throw new ClientServicesException(null, "The service URL is empty");
		}
	}

	protected void checkReadParameters(Map<String, String> parameters) throws ClientServicesException {
		// nothing for now...
	}
	
	protected String getBaseUrl() {
		if (endpoint != null) {
			return endpoint.getUrl();
		}
		return null;
	}

	protected void initialize(DefaultHttpClient httpClient) throws ClientServicesException {
		if (endpoint != null) {
			endpoint.initialize(httpClient);
		}
	}

	protected boolean isForceTrustSSLCertificate() throws ClientServicesException {
		if (endpoint != null) {
			return endpoint.isForceTrustSSLCertificate();
		}
		return false;
	}

	protected void forceAuthentication(Args args) throws ClientServicesException {
		if (endpoint != null) {
			endpoint.authenticate(true);
		} else {
			throw new NoAccessSignal(StringUtil.format("Authorization needed for service {0}",
					getUrlPath(args)));
		}
	}

	// =================================================================
	// Generic access
	// =================================================================

	public static class Args {

		private String				serviceUrl; // Service URL to call, relative to the endpoint
		private Map<String, String>	parameters; // Query String parameters
		private Map<String, String>	headers;	// HTTP Headers
		private Handler				handler;	// Format of the result

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
				httpRequestBase.setHeader("Content-type", contentType);
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
			httpRequestBase.setHeader("Content-type", getContentType());
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
			this.name = name.trim();
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
			httpRequestBase.setHeader("Content-type", getContentType());
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
		protected Reader createReader(HttpRequestBase request, HttpResponse response, HttpEntity entity, String encoding) throws IOException {
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

	protected Handler createResponseHandler(HttpResponse response, HttpEntity entity)
			throws UnsupportedEncodingException, IOException {
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

	public final Object get(String serviceUrl) throws ClientServicesException {
		return get(serviceUrl, null, null);
	}

	public final Object get(String serviceUrl, Map<String, String> parameters) throws ClientServicesException {
		return get(serviceUrl, parameters, null);
	}

	public final Object get(String serviceUrl, Handler format) throws ClientServicesException {
		return get(serviceUrl, null, format);
	}

	public final Object get(String serviceUrl, Map<String, String> parameters, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return get(args);
	}

	public final Object get(Args args) throws ClientServicesException {
		return xhr(METHOD_GET, args, null);
	}

	// =================================================================
	// POST
	// =================================================================

	public final Object post(String serviceUrl, Object content) throws ClientServicesException {
		return post(serviceUrl, null, content, null);
	}

	public final Object post(String serviceUrl, Map<String, String> parameters, Object content)
			throws ClientServicesException {
		return post(serviceUrl, parameters, content, null);
	}

	public final Object post(String serviceUrl, Map<String, String> parameters, Object content, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return post(args, content);
	}

	public final Object post(String serviceUrl, Map<String, String> parameters, Map<String, String> headers,
			Object content, Handler format) throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		args.setHeaders(headers);
		return post(args, content);
	}

	public final Object post(Args args, Object content) throws ClientServicesException {
		return xhr(METHOD_POST, args, content);
	}

	// =================================================================
	// PUT
	// =================================================================

	public final Object put(String serviceUrl, Object content) throws ClientServicesException {
		return put(serviceUrl, null, content, null);
	}

	public final Object put(String serviceUrl, Map<String, String> parameters, Object content)
			throws ClientServicesException {
		return put(serviceUrl, parameters, content, null);
	}

	public final Object put(String serviceUrl, Map<String, String> parameters, Object content, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return put(args, content);
	}

	public final Object put(String serviceUrl, Map<String, String> parameters, Map<String, String> headers,
			Object content, Handler format) throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		args.setHeaders(headers);
		return put(args, content);
	}

	public final Object put(Args args, Object content) throws ClientServicesException {
		return xhr(METHOD_PUT, args, content);
	}

	// =================================================================
	// DELETE
	// =================================================================

	public final Object delete(String serviceUrl) throws ClientServicesException {
		return delete(serviceUrl, null, null);
	}

	public final Object delete(String serviceUrl, Map<String, String> parameters)
			throws ClientServicesException {
		return delete(serviceUrl, parameters, null);
	}

	public final Object delete(String serviceUrl, Map<String, String> parameters, Handler format)
			throws ClientServicesException {
		Args args = createArgs(serviceUrl, parameters);
		args.setHandler(format);
		return delete(args);
	}

	public final Object delete(Args args) throws ClientServicesException {
		return xhr(METHOD_DELETE, args, null);
	}

	// =================================================================
	// Actual request execution
	// =================================================================

	public Object xhr(String method, Args args, Object content) throws ClientServicesException {

		checkAuthentication(args);
		checkUrl(args);
		checkReadParameters(args.parameters);
		String url = composeRequestUrl(args);
		if (StringUtil.equalsIgnoreCase(method, "get")) {
			HttpGet httpGet = new HttpGet(url);
			return execRequest(httpGet, args, content);
		} else if (StringUtil.equalsIgnoreCase(method, "post")) {
			HttpPost httpPost = new HttpPost(url);
			return execRequest(httpPost, args, content);
		} else if (StringUtil.equalsIgnoreCase(method, "put")) {
			HttpPut httpPut = new HttpPut(url);
			return execRequest(httpPut, args, content);
		} else if (StringUtil.equalsIgnoreCase(method, "delete")) {
			HttpDelete httpDelete = new HttpDelete(url);
			return execRequest(httpDelete, args, content);
		} else {
			throw new ClientServicesException(null, "Unsupported HTTP method {0}", method);
		}
	}

	protected Object execRequest(HttpRequestBase httpRequestBase, Args args, Object content)
			throws ClientServicesException {

		if (Profiler.isEnabled()) {
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerRequest, httpRequestBase.getMethod()
					.toUpperCase() + " " + getUrlPath(args));
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
	protected Object _xhr(HttpRequestBase httpRequestBase, Args args, Object content)
			throws ClientServicesException {
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

	protected HttpResponse executeRequest(HttpClient httpClient, HttpRequestBase httpRequestBase, Args args)
			throws ClientServicesException {
		try {
			return httpClient.execute(httpRequestBase);
		} catch (Exception ex) {
			if (ex instanceof ClientServicesException) {
				throw (ClientServicesException) ex;
			}
			throw new ClientServicesException(ex, "Error while executing the REST service {0}",
					httpRequestBase.getURI().toString());
		}
	}

	protected Object processResponse(HttpClient httpClient, HttpRequestBase httpRequestBase,
			HttpResponse response, Args args) throws ClientServicesException {
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK
				&& response.getStatusLine().getStatusCode() != HttpStatus.SC_CREATED) {
			if (SbtCoreLogger.SBT.isErrorEnabled()) {
				// Do not throw an exception here as some of the non OK responses are not error cases.
				SbtCoreLogger.SBT
						.errorp(this,
								"processResponse",
								"Client service request to: {0} did not return OK status. Status returned: {1}, reason: {2}, expected: {3}",
								httpRequestBase.getURI(), response.getStatusLine().getStatusCode(), response
										.getStatusLine().getReasonPhrase(), HttpStatus.SC_OK);
			}
		}
		if ((response.getStatusLine().getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED)
				|| (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_FORBIDDEN)) {
			forceAuthentication(args);
			return null;
		}
		return _parseResult(httpRequestBase, response, args.handler);
	}

	// =================================================================
	// URL composition
	// =================================================================

	protected String composeRequestUrl(Args args) throws ClientServicesException {
		// Compose the URL
		StringBuilder b = new StringBuilder(256);
		String url = getUrlPath(args);
		if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}
		b.append(url);
		addUrlParts(b, args);
		addUrlParameters(b, args);
		return b.toString();
	}

	protected String getUrlPath(Args args) {
		String baseUrl = getBaseUrl();
		String serviceUrl = args.getServiceUrl();
		return PathUtil.concat(baseUrl,serviceUrl, '/');
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

	protected Object _parseResult(HttpRequestBase request, HttpResponse response, Handler format)
			throws ClientServicesException {
		try {
			int statusCode = response.getStatusLine().getStatusCode();
			if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_INTERNAL_SERVER_ERROR) {
				return generateResultError500(statusCode, request, response);
			}
			
			HttpEntity entity = response.getEntity();
			if (format == null) {
				format = createResponseHandler(response, entity);
			}

			if ((statusCode != HttpStatus.SC_OK) && (statusCode != HttpStatus.SC_CREATED)
					&& (statusCode != HttpStatus.SC_ACCEPTED) && (statusCode != HttpStatus.SC_NO_CONTENT)) { // Connections
																												// Delete
																												// API
																												// returns
																												// SC_NO_CONTENT
																												// for
																												// successful
																												// deletion.
				return generateResultError(statusCode, request, response, entity);
			}

			// SBT doesn't have a JS interpreter...
			if (format != null) {
				return format.parseContent(request, response, entity);
			}
			// else if (StringUtil.equals(format, FORMAT_CONNECTIONS_OUTPUT)) {
			// return readLocationHeaderAsString(httpRequestBase, response, entity);
			// }
			return response;
		} catch (Exception ex) {
			if (ex instanceof ClientServicesException) {
				throw (ClientServicesException) ex;
			}
			throw new ClientServicesException(ex, "Error while parsing the REST service results");
		}
	}
	
	protected Object generateResultError500(int statusCode, HttpRequestBase request, HttpResponse response) throws ClientServicesException {
		// PHIL:
		// Why SBT exception and not ClientService
		throw new SBTException(null,
				"Request for url: {0} did not complete successfully. Error code {1} [{2}] returned",
				request.getURI(), response.getStatusLine().getStatusCode(), response
						.getStatusLine().getReasonPhrase());
	}
	
	protected Object generateResultError(int statusCode, HttpRequestBase request, HttpResponse response, HttpEntity entity) throws ClientServicesException {
		throw new ClientServicesException(response, request);
	}

	// Until we move to HttpClient 4.1
	protected static InputStream getEntityContent(HttpRequestBase request, HttpResponse response,
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

	protected DefaultHttpClient createHttpClient(HttpRequestBase httpRequestBase, Args args)
			throws ClientServicesException {
		// Check if we should trust the HTTPS certificates
		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (isForceTrustSSLCertificate()) {
			// PHIL: we don't check the scheme here as the Apachae library will still verify the
			// certificate for some http requests...
			// String scheme = httpRequestBase.getURI().getScheme();
			// if(scheme!=null && scheme.equalsIgnoreCase("https")) {
				return SSLUtil.wrapHttpClient(httpClient);
			// }
		}
		return httpClient;
	}
}
