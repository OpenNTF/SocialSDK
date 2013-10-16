package com.ibm.sbt.service.basic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.runtime.NoAccessSignal;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientService.Content;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.AuthUtil;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * 
 * @author Vineet Kanwal
 **/
public abstract class AbstractFileProxyService extends ProxyEndpointService {

	protected enum Operations {
		UPLOAD_FILE("UploadFile"), DOWNLOAD_FILE("DownloadFile"), UPDATE_PROFILE_PHOTO("UpdateProfilePhoto"), UPLOAD_NEW_VERSION("UploadNewVersion");

		private Operations(final String text) {
			this.text = text;
		}

		private final String text;

		@Override
		public String toString() {
			return text;
		}

	}

	protected Map<String, String> parameters = new HashMap<String, String>();

	protected String operation = null;

	protected String method = null;

	private Long length = 0L;

	protected abstract String getRequestURI(String smethod, String authType, Map<String, String[]> params) throws ServletException;

	@Override
	protected void initProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		// TODO can this be moved to parametrers
		String pathinfo = request.getPathInfo().substring(request.getPathInfo().indexOf("/files"));
		String[] pathTokens = pathinfo.split("/");
		if (pathTokens.length >= 6) {
			// /files/<<endpointName>>/serviceTpe/fileName
			String endPointName = pathTokens[2];
			this.endpoint = (Endpoint) EndpointFactory.getEndpoint(endPointName);
			if (!endpoint.isAllowClientAccess()) {
				throw new ServletException(StringUtil.format("Client access forbidden for the specified endpoint {0}", endPointName));
			}
			operation = pathTokens[4];
			getParameters(pathTokens);
			method = request.getMethod();
			requestURI = getRequestURI(request.getMethod(), AuthUtil.INSTANCE.getAuthValue(endpoint), request.getParameterMap());
			return;
		}
		throw new ServletException(StringUtil.format("Invalid url {0}", pathinfo));
	}

	protected abstract void getParameters(String[] tokens) throws ServletException;

	@Override
	public void serviceProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {			
		try {
			initProxy(request, response);
			String smethod = request.getMethod();
			DefaultHttpClient client = getClient(request, getSocketReadTimeout());
			URI url = getRequestURI(request);
			HttpRequestBase method = createMethod(smethod, url, request);
			if (prepareForwardingMethod(method, request, client)) {
				if (smethod.equalsIgnoreCase("POST") || (smethod.equalsIgnoreCase("PUT"))) {
					FileItemFactory factory = new DiskFileItemFactory();
					// Create a new file upload handler
					ServletFileUpload upload = new ServletFileUpload(factory);
					// Parse the request
					@SuppressWarnings("unchecked")
					List<FileItem> fileItems = upload.parseRequest(request);
					if (fileItems.size() == 0) {
						writeErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "No File Item found in Multipart Form data", new String[] {}, new String[] {}, response, request);
						return;
					}
					for (FileItem uploadedFile : fileItems) {
						InputStream uploadedFileContent = uploadedFile.getInputStream();
						File file = convertInputStreamToFile(uploadedFileContent, uploadedFile.getSize());
						Map<String, String[]> params = request.getParameterMap() != null ? request.getParameterMap() : new HashMap<String, String[]>();
						Content content = getFileContent(file,  length, uploadedFile.getContentType());
						
						Map<String, String> headers = createHeaders();

						xhr(request, response, url.getPath(), params, headers, content, getFormat());
					}
				} else if (smethod.equalsIgnoreCase("GET")) {
					xhr(request, response, requestURI, new HashMap<String, String[]>(), new HashMap<String, String>(), null, ClientService.FORMAT_INPUTSTREAM);

				}
			}
		} catch (Exception e) {
			if (e instanceof ClientServicesException) {
				writeErrorResponse(((ClientServicesException) e).getResponseStatusCode(), "Unexpected Exception", new String[] { "exception" }, new String[] { e.toString() }, response, request);
			} else {
				writeErrorResponse("Unexpected Exception", new String[] { "exception" }, new String[] { e.toString() }, response, request);
			}
		} finally {
			termProxy(request, response);				
		}
	}

	protected abstract Handler getFormat();

	private File convertInputStreamToFile(InputStream inputStream, Long size) throws IOException {

		OutputStream out = null;
		try {
			File file = new File(parameters.get("FileName")); // TODO check with Phil to avoid conflict in name
			out = new FileOutputStream(file);
			int bufferSize = size.intValue() < 8192 ? size.intValue() : 8192;
			byte[] bytes = new byte[bufferSize];
			int read = 0;
			while ((read = inputStream.read(bytes)) != -1) {
				length += read;
				out.write(bytes);
				out.flush();
			}
			inputStream.close();
			out.close();
			return file;
		} catch (IOException e) {
			throw e;
		} finally {
			inputStream.close();
			if (out != null) {
				out.close();
			}
		}
	}

	protected abstract Map<String, String> createHeaders();

	protected abstract Content getFileContent(File file, long length, String contentType);

	private boolean addParameter(StringBuilder b, boolean first, String name, String value) throws ClientServicesException {
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

	private String composeRequestUrl(Args args, Map<String, String[]> parameters) throws ClientServicesException {
		// Compose the URL
		StringBuilder b = new StringBuilder(256);
		String baseUrl = endpoint.getUrl();
		String serviceUrl = args.getServiceUrl();
		String url = PathUtil.concat(baseUrl, serviceUrl, '/');
		if (url.charAt(url.length() - 1) == '/') {
			url = url.substring(0, url.length() - 1);
		}
		b.append(url);

		if (parameters != null) {
			boolean first = true;
			for (Map.Entry<String, String[]> e : parameters.entrySet()) {
				String name = e.getKey();
				if (StringUtil.isNotEmpty(name)) {
					String[] values = e.getValue();
					for (String value : values) {
						first = addParameter(b, first, name, value);
					}
				}
			}
		}
		return b.toString();
	}

	protected void forceAuthentication(Args args) throws ClientServicesException {
		if (endpoint != null) {
			endpoint.authenticate(true);
		} else {
			throw new NoAccessSignal(StringUtil.format("Authorization needed for service {0}", getUrlPath(args)));
		}
	}

	protected String getBaseUrl() {
		if (endpoint != null) {
			return endpoint.getUrl();
		}
		return null;
	}

	protected String getUrlPath(Args args) {
		String baseUrl = getBaseUrl();
		String serviceUrl = args.getServiceUrl();
		return PathUtil.concat(baseUrl, serviceUrl, '/');
	}

	private void xhr(HttpServletRequest request, HttpServletResponse response, String serviceUrl, Map<String, String[]> parameters, Map<String, String> headers, Content content, Handler format)
			throws ClientServicesException, ClientProtocolException, IOException, ServletException, URISyntaxException {
		Args args = new Args();
		args.setServiceUrl(serviceUrl);
		args.setHandler(format);
		args.setHeaders(headers);

		String smethod = request.getMethod();
		HttpRequestBase method = createMethod(smethod, new URI(composeRequestUrl(args, parameters)), request);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		if (endpoint.isForceTrustSSLCertificate()) {
			httpClient = SSLUtil.wrapHttpClient(httpClient);
		}
		endpoint.initialize(httpClient);
		for (Map.Entry<String, String> e : args.getHeaders().entrySet()) {
			String headerName = e.getKey();
			String headerValue = e.getValue();
			method.addHeader(headerName, headerValue);
		}
		if (content != null) {
			content.initRequestContent(httpClient, method, args);
		}
		endpoint.updateHeaders(httpClient, method);
		HttpResponse clientResponse = httpClient.execute(method);

		if ("get".equalsIgnoreCase(smethod) && (clientResponse.getStatusLine().getStatusCode() == HttpServletResponse.SC_UNAUTHORIZED)
				|| (endpoint != null && endpoint.getAuthenticationErrorCode() == clientResponse.getStatusLine().getStatusCode())) {
			forceAuthentication(args);
		}
		prepareResponse(method, request, response, clientResponse, true);
	}
}
