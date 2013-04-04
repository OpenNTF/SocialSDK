package com.ibm.sbt.service.basic;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/** @author Vineet Kanwal */
public class ConnectionsFileProxyService extends ProxyEndpointService {

	public static final String TYPE = "connections";

	public String getType() {
		return TYPE;
	}

	@Override
	protected void initProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		String pathinfo = request.getPathInfo();
		// Skip the URL_PATH of the proxy
		// /[proxy root]/[URL_PATH]/[endpointname]/[http/...]
		int startEndPoint = getProxyUrlPath().length() + 2;
		if (startEndPoint < pathinfo.length()) {
			int startProxyUrl = pathinfo.indexOf('/', startEndPoint);
			if (startProxyUrl >= 0) {
				String endPointName = pathinfo.substring(startEndPoint, startProxyUrl);
				this.endpoint = (Endpoint) EndpointFactory.getEndpoint(endPointName);
				if (!endpoint.isAllowClientAccess()) {
					throw new ServletException(StringUtil.format("Client access forbidden for the specified endpoint {0}", endPointName));
				}

				String url = pathinfo.substring(startProxyUrl + 1).replaceFirst("\\/", "://");

				requestURI = url;
				return;
			}
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
		try {
			initProxy(request, response);
			try {
				String smethod = request.getMethod();
				DefaultHttpClient client = getClient(request, getSocketReadTimeout());
				URI url = getRequestURI(request);
				HttpRequestBase method = createMethod(smethod, url, request);

				if (prepareForwardingMethod(method, request, client)) {

					String name = request.getHeader("Slug").substring(request.getHeader("Slug").lastIndexOf("\\") + 1);

					String uploadUrl = request.getRequestURI().substring(request.getRequestURI().indexOf("files/basic"));

					HashMap<String, String> params = new HashMap<String, String>();

					InputStream inputStream = request.getInputStream();
					// write the inputStream to a FileOutputStream
					File file = new File(name);
					OutputStream out = new FileOutputStream(file);

					int read = 0;
					byte[] bytes = new byte[1024];

					while ((read = inputStream.read(bytes)) != -1) {
						out.write(bytes, 0, read);
					}

					inputStream.close();
					out.flush();
					out.close();

					Map<String, String> headers = new HashMap<String, String>();
					@SuppressWarnings("unchecked")
					Enumeration<String> headerNames = request.getHeaderNames();
					while (headerNames.hasMoreElements()) {
						String headerName = headerNames.nextElement();
						if (isHeaderAllowed(headerName) || "Slug".equals(headerName)) {
							headers.put(headerName, request.getHeader(headerName));
						}

					}

					this.endpoint.getClientService().post(uploadUrl, params, headers, file, ClientService.FORMAT_XML);
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
			}
		} catch (Exception e) {
			writeErrorResponse("Unexpected Exception", new String[] { "exception" }, new String[] { e.toString() }, response, request);
		}

	}

}
