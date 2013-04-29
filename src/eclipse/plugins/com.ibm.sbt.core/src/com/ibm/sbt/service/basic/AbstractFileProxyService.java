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
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientService.Args;
import com.ibm.sbt.services.client.ClientService.Content;
import com.ibm.sbt.services.client.ClientService.Handler;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.services.util.SSLUtil;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public abstract class AbstractFileProxyService extends ProxyEndpointService {
	protected String uploadUrl;

	@Override
	protected void initProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String pathinfo = request.getPathInfo().substring(request.getPathInfo().indexOf("/files")); 
		String[] pathTokens = pathinfo.split("/");
		if (pathTokens.length > 3) {
			String endPointName = pathTokens[2];
			this.endpoint = (Endpoint) EndpointFactory.getEndpoint(endPointName);
			if (!endpoint.isAllowClientAccess()) {
				throw new ServletException(StringUtil.format("Client access forbidden for the specified endpoint {0}", endPointName));
			}
			int start = pathinfo.indexOf(pathTokens[4] + "/" + pathTokens[5]) - 1;
			requestURI = pathinfo.substring(start);
			return;
		}
		StringBuffer b = request.getRequestURL();
		String q = request.getQueryString();
		if (StringUtil.isNotEmpty(q)) {
			b.append('?');
			b.append(q);
		}
		throw new ServletException(StringUtil.format("Invalid url {0}", b.toString()));
	}

	@Override
	public void serviceProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		InputStream inputStream = request.getInputStream();
		OutputStream out = null;
		try {
			initProxy(request, response);

			String smethod = request.getMethod();
			DefaultHttpClient client = getClient(request, getSocketReadTimeout());
			URI url = getRequestURI(request);
			HttpRequestBase method = createMethod(smethod, url, request);

			if (prepareForwardingMethod(method, request, client)) {

				String name = request.getHeader("Slug").substring(request.getHeader("Slug").lastIndexOf("\\") + 1);

				@SuppressWarnings("unchecked")
				Map<String, String[]> params = request.getParameterMap() != null ? request.getParameterMap() : new HashMap<String, String[]>();

				File file = new File(name);
				out = new FileOutputStream(file);

				long length = 0;
				byte[] bytes = new byte[1024];
				int read = 0;
				while ((read = inputStream.read(bytes)) != -1) {
					length += read;
					out.write(bytes, 0, read);
					out.flush();
				}

				inputStream.close();
				out.close();

				Content content = getFileContent(file, length, name);

				Map<String, String> headers = new HashMap<String, String>();
				// this.endpoint.getClientService().post(uploadUrl, params, headers, content, ClientService.FORMAT_XML);
				xhr(request, response, url.getPath(), params, headers, content, ClientService.FORMAT_XML);
			}
		} catch (Exception e) {
			if (e instanceof ClientServicesException) {
				writeErrorResponse(((ClientServicesException) e).getResponseStatusCode(), "Unexpected Exception", new String[] { "exception" },
						new String[] { e.toString() }, response, request);
			} else {
				writeErrorResponse("Unexpected Exception", new String[] { "exception" }, new String[] { e.toString() }, response, request);
			}
		} finally {
			termProxy(request, response);
			inputStream.close();
			if (out != null) {
				out.close();
			}
		}

	}

	protected abstract Content getFileContent(File file, long length, String name);

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

	private void xhr(HttpServletRequest request, HttpServletResponse response, String serviceUrl, Map<String, String[]> parameters,
			Map<String, String> headers, Content content, Handler format) throws ClientServicesException, ClientProtocolException, IOException,
			ServletException, URISyntaxException {

		Args args = new Args();
		args.setServiceUrl(serviceUrl);
		// args.setParameters(parameters);
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

		HttpResponse clientResponse = httpClient.execute(method);

		prepareResponse(method, request, response, clientResponse, true);

	}

}
