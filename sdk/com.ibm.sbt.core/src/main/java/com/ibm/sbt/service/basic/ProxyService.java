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
package com.ibm.sbt.service.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpOptions;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.ByteStreamCache;
import com.ibm.commons.util.io.ReaderInputStream;
import com.ibm.commons.util.io.StreamUtil;
import com.ibm.commons.util.io.TraceOutputStream;
import com.ibm.commons.util.io.WriterOutputStream;
import com.ibm.commons.util.io.base64.Base64;
import com.ibm.commons.util.io.json.JsonGenerator;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.profiler.Profiler;
import com.ibm.commons.util.profiler.ProfilerAggregator;
import com.ibm.commons.util.profiler.ProfilerType;
import com.ibm.sbt.service.debug.DebugProxyHook;
import com.ibm.sbt.service.debug.DebugServiceHookFactory;
import com.ibm.sbt.service.debug.DebugServiceHookFactory.Type;

/**
 * Basic proxy.
 * <p>
 * This is the base class handling proxy requests. It has to be instanciated and called
 * from a servlet, or from a proxy handler from the library.
 * </p>
 * @author Dan Dumont
 * @author Philippe Riand
 */
public class ProxyService {

	/// TODO: use a group here...
	private static boolean TRACE = false;

	private static final ProfilerType profilerRequest           = new ProfilerType("Proxy request, "); //$NON-NLS-1$

	/**
	 * The wrapper for cookie domain and path.  These are stored in the cookie value
	 * when the cookie is passed to the browser.  So that the proxy domain and path
	 * can be used when the cookie is passed back to the browser.
	 */
	private static final String PASSTHRUID               = "sbtsdkck";

	private DebugProxyHook debugHook;
	
	public ProxyService() {
	}

	public DebugProxyHook getDebugHook() {
		return debugHook;
	}
	
	public int getSocketReadTimeout() {
		return 120; // default, in seconds
	}

	protected DefaultHttpClient getClient(HttpServletRequest request, int timeout) {
		// Should we manage a connection pool here?
		//BasicHttpParams params = new BasicHttpParams();
		//params.setRedirecting(params, false);
		//DefaultHttpClient httpClient = new DefaultHttpClient(params);
		DefaultHttpClient httpClient = new DefaultHttpClient();
		return httpClient;
	}

	//
	// Security options - defensive mode by default
	//
	protected void checkRequestAllowed(HttpServletRequest request) throws ServletException {
		String method = request.getMethod();
		if(!isMethodAllowed(method)) {
			throw new ServletException(StringUtil.format("Invalid request method {0}",method));
		}
	}

	protected boolean isMethodAllowed(String method) throws ServletException {
		return false;
	}
	protected boolean forwardHeaders(HttpRequestBase method, HttpServletRequest request) {
		return true;
	}
	protected boolean isHeaderAllowed(String headerName) throws ServletException {
		// content-length header will be automatically added by the HTTP client
		// host header causes request failures when it does not match the proxy host see x-forwaded-for comment above
	    // Origin is added by chrome / safari in post/put/delete same-origin requests.
		if(StringUtil.equalsIgnoreCase(headerName, "content-length")
				|| StringUtil.equalsIgnoreCase(headerName, "host") || StringUtil.equalsIgnoreCase(headerName, "origin")) {
			return false;
		}
		return true;
	}
	protected boolean forwardCookies(HttpRequestBase method, HttpServletRequest request) {
		return true;
	}
	protected boolean isCookieAllowed(String cookieName) throws ServletException {
		//if(cookieName.equalsIgnoreCase("JSESSIONID")) {
		//    return false;
		//}
		return true;
	}
	protected boolean isMimeTypeAllowed(String cookieName) throws ServletException {
		return true;
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (Profiler.isEnabled()) {
			StringBuffer b = request.getRequestURL();
			String url = b.toString();
			ProfilerAggregator agg = Profiler.startProfileBlock(profilerRequest, request.getMethod().toUpperCase() + " " + url);
			long ts = Profiler.getCurrentTime();
			try {
				serviceProxy(request, response);
			} finally {
				Profiler.endProfileBlock(agg, ts);
			}
		}
		else {
			serviceProxy(request, response);
		}
	}
	protected void serviceProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.debugHook = (DebugProxyHook)DebugServiceHookFactory.get().get(Type.PROXY,request,response);
		if(debugHook!=null) {
			request = debugHook.getRequestWrapper();
			response = debugHook.getResponseWrapper();
		}
		try {
			try {
				initProxy(request, response);
				try {
					checkRequestAllowed(request);
					String smethod = request.getMethod();
					DefaultHttpClient client = getClient(request, getSocketReadTimeout());
					URI url = getRequestURI(request);
					HttpRequestBase method = createMethod(smethod, url, request);
					if (prepareForwardingMethod(method, request, client)) {
						HttpResponse clientResponse = executeMethod(client, method);
						prepareResponse(method, request, response, clientResponse, true);
					}
					response.flushBuffer();
				} catch (Exception e) {
					writeErrorResponse("Unexpected Exception", new String[] {"exception"},new String[] {e.toString()}, response, request);
				}finally {
					termProxy(request, response);
				}
			} catch (Exception e) {
				writeErrorResponse("Unexpected Exception", new String[] {"exception"},new String[] {e.toString()}, response, request);
			}
		} finally {
			if(debugHook!=null) {
				debugHook.terminate();
				debugHook = null;
			}
		}
	}

	private HttpRequestBase prepareMethodWithUpdatedContent(
			HttpRequestBase method, HttpServletRequest request) throws ServletException {
		// PHIL, 2/28/2013
		// We should use the length when available, for HTTP 1.1
		// -> it optimizes with HTTP 1.1
		// -> it prevents the HTTP client to use Transfert-Encoding: chunked, which doesn't
		//    seem to work will all HTTP servers (ex: Domino)
		// A browser should anyway provide the length.
		long length = -1;
		String slength = request.getHeader("Content-Length");
		if(StringUtil.isNotEmpty(slength)) {
			length = Long.parseLong(slength);
		}
		try {
			// When no length is specified, the HTTP client core forces Transfert-Encoding: chunked
			// The code bellow shows a workaround, although we should avoid that
			if(false) {
				if(length<0) {
					ByteStreamCache bs = new ByteStreamCache();
					bs.copyFrom(request.getInputStream());
					HttpEntity payloadEntity = new InputStreamEntity(bs.getInputStream(), bs.getLength());
					((HttpEntityEnclosingRequest) method).setEntity(payloadEntity);
					return method;
				}
			}
			// Regular code
			HttpEntity payloadEntity = new InputStreamEntity(request.getInputStream(), length);
			((HttpEntityEnclosingRequest) method).setEntity(payloadEntity);
		}catch(Exception e){
			throw new ServletException("Error while parsing the payload");
		}
		return method;
	}

	protected void initProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	}

	protected void termProxy(HttpServletRequest request, HttpServletResponse response) throws ServletException {
	}

	protected HttpRequestBase createMethod(String smethod, URI uri,HttpServletRequest request) throws ServletException {
		if(getDebugHook()!=null) {
			getDebugHook().getDumpRequest().setMethod(smethod);
			getDebugHook().getDumpRequest().setUrl(uri.toString());
		}
		if (smethod.equalsIgnoreCase("get")) {
			HttpGet method = new HttpGet(uri);
			return method;
		} else if (smethod.equalsIgnoreCase("put")) {
			HttpPut method = new HttpPut(uri);
			method = (HttpPut) prepareMethodWithUpdatedContent(method, request);
			return method;
		} else if (smethod.equalsIgnoreCase("post")) {
			HttpPost method = new HttpPost(uri);
			method = (HttpPost) prepareMethodWithUpdatedContent(method, request);
			return method;
		} else if (smethod.equalsIgnoreCase("delete")) {
			HttpDelete method = new HttpDelete(uri);
			return method;
		} else if (smethod.equalsIgnoreCase("head")) {
			HttpHead method = new HttpHead(uri);
			return method;
		} else if (smethod.equalsIgnoreCase("options")) {
			HttpOptions method = new HttpOptions(uri);
			return method;
		} else {
			throw new ServletException("Illegal method, should be GET, PUT, POST, DELETE or HEAD");
		}
	}

	protected boolean prepareForwardingMethod(HttpRequestBase method, HttpServletRequest request, DefaultHttpClient client) throws ServletException {
		boolean retval =   (!forwardCookies(method, request) || prepareForwardingCookies(method, request, client))
		&& (!forwardHeaders(method, request) || prepareForwardingHeaders(method, request));
		return retval;
	}

	protected boolean prepareForwardingCookies(HttpRequestBase method, HttpServletRequest request, DefaultHttpClient httpClient) throws ServletException {
		Cookie[] cookies = request.getCookies();
		BasicCookieStore cs = new BasicCookieStore();
		httpClient.setCookieStore(cs);
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie != null) {
					String cookiename = cookie.getName();
					if(StringUtil.isNotEmpty(cookiename)) {
						String cookieval = cookie.getValue();
						if (cookiename.startsWith(PASSTHRUID)) {
							cookiename = cookiename.substring(PASSTHRUID.length());
							if(isCookieAllowed(cookiename)) {
								String[] parts = decodeCookieNameAndPath(cookiename);
								if (parts!=null && parts.length==3) {
									cookiename = parts[0];
									String path = parts[1];
									String domain = parts[2];

									// Got stored domain now see if it matches destination
									BasicClientCookie methodcookie = new BasicClientCookie(cookiename,cookieval);
									methodcookie.setDomain(domain);
									methodcookie.setPath(path);
									cs.addCookie(methodcookie);
									if(getDebugHook()!=null) {
										getDebugHook().getDumpRequest().addCookie(methodcookie.getName(), methodcookie.toString());
									}
								}
							}
						} else if(isCookieAllowed(cookiename)) {
							BasicClientCookie methodcookie = new BasicClientCookie(cookiename,cookieval);
							String domain = cookie.getDomain();
							if (domain == null) {
								try {
									domain = method.getURI().getHost();
									domain = domain.substring(domain.indexOf('.'));
								} catch (Exception e) {
									domain = "";
								}
							}
							methodcookie.setDomain(domain);
							String path = cookie.getPath();
							if (path == null) {
								path = "/";
							}
							methodcookie.setPath(path);
							cs.addCookie(methodcookie);
							if(getDebugHook()!=null) {
								getDebugHook().getDumpRequest().addCookie(methodcookie.getName(), methodcookie.toString());
							}
						}
					}
				}
			}
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	protected boolean prepareForwardingHeaders(HttpRequestBase method, HttpServletRequest request) throws ServletException {
		// Forward any headers that should be forwarded. Except cookies.
		StringBuffer xForwardedForHeader = new StringBuffer();
		for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
			String headerName = e.nextElement();
			// Ignore cookie - treat them separately
			if (headerName.equalsIgnoreCase("cookie")) { // $NON-NLS-1$
				continue;
			}
			String headerValue = request.getHeader(headerName);
			// This is to be investigated - Should the X-Forwarded-For being passed? Ryan.
			//            if(headerName.equalsIgnoreCase("X-Forwarded-For") || headerName.equalsIgnoreCase("host")) {
			//                addXForwardedForHeader(method, headerValue, xForwardedForHeader);
			//                continue;
			//            }
			// Ensure that the header is allowed
			if(isHeaderAllowed(headerName)) {
				method.addHeader(headerName, headerValue);
				if(getDebugHook()!=null) {
					getDebugHook().getDumpRequest().addHeader(headerName, headerValue);
				}
			}
		}
		String xForward = xForwardedForHeader.toString();
		if(StringUtil.isNotEmpty(xForward)) {
			method.addHeader("X-Forwarded-For", xForward);
			if(getDebugHook()!=null) {
				getDebugHook().getDumpRequest().addHeader("X-Forwarded-For", xForward);
			}
		}
		return true;
	}

	/**
	 * Adds a host to the x-Forwarded-For header.  This method is called a host or
	 * x-Forwarded-For header is encountered on the proxied request.
	 * @param method The request being made by the proxy.
	 * @param headerValue The header value from the proxied request.
	 * @param xForwardedForHeader The current valye of the x-Forward-For header that will be
	 * added to the request made by the proxy.
	 */
	protected void addXForwardedForHeader(HttpRequestBase method, String headerValue,
			StringBuffer xForwardedForHeader) {
		String[] forwards = headerValue.trim().split(",");
		for (String forward : forwards) {
			String host = forward.trim();
			if(!host.equals("")) {
				if(xForwardedForHeader.length() == 0) {
					xForwardedForHeader.append(host);
				} else {
					xForwardedForHeader.append("," + host);
				}
			}
		}
	}

	public HttpResponse executeMethod(HttpClient client, HttpRequestBase method) throws ServletException {
		try {
			HttpResponse response = client.execute(method);
			return response;
		} catch(IOException ex) {
			throw new ServletException(ex);
		}
	}

	public void prepareResponse(HttpRequestBase method, HttpServletRequest request, HttpServletResponse response, HttpResponse clientResponse, boolean isCopy) throws ServletException {
		try {
			int statusCode = clientResponse.getStatusLine().getStatusCode();
			if(statusCode == 401 || statusCode == 403){
				clientResponse.setHeader("WWW-Authenticate", "");
			}
			response.setStatus(statusCode);
			if(getDebugHook()!=null) {
				getDebugHook().getDumpResponse().setStatus(statusCode);
			}

			// Passed back all heads, but process cookies differently.
			Header[] headers = clientResponse.getAllHeaders();
			for (Header header : headers) {
				String headername = header.getName();

				if (headername.equalsIgnoreCase("Set-Cookie") ) { // $NON-NLS-1$
					if(forwardCookies(method, request)) {
						// If cookie, have to rewrite domain/path for browser.
						String setcookieval = header.getValue();
	
						if (setcookieval != null) {
							String thisserver = request.getServerName();
	
							String thisdomain;
							if (thisserver.indexOf('.') == -1) {
								thisdomain = "";
							}
							else {
								thisdomain = thisserver.substring(thisserver.indexOf('.'));
							}
							String domain = null;
	
							// path info = /protocol/server/path-on-server
							//Matcher m = cookiePathPattern.matcher(request.getPathInfo());
							String thispath = request.getContextPath() + request.getServletPath();
							String path = null;
	
							String[][] cookparams = getCookieStrings(setcookieval);
	
							for (int j = 1; j < cookparams.length; j++) {
								if ("domain".equalsIgnoreCase(cookparams[j][0])) { // $NON-NLS-1$
									domain = cookparams[j][1];
									cookparams[j][1] = null;
								} else if ("path".equalsIgnoreCase(cookparams[j][0])) { // $NON-NLS-1$
									path = cookparams[j][1];
									cookparams[j][1] = null;
								}
							}
	
							if (domain == null) {
								domain = method.getURI().getHost();
							}
	
							// Set cookie name
							String encoded = encodeCookieNameAndPath(cookparams[0][0], path, domain);
							if(encoded!=null) {
								String newcookiename = PASSTHRUID + encoded;
	
								StringBuilder newset = new StringBuilder(newcookiename);
								newset.append('=');
								newset.append(cookparams[0][1]);
	
								for (int j = 1; j < cookparams.length; j++) {
									String settingname = cookparams[j][0];
									String settingvalue = cookparams[j][1];
									if (settingvalue != null) {
										newset.append("; ").append(settingname); // $NON-NLS-1$
										newset.append('=').append(settingvalue); // $NON-NLS-1$
									}
								}
	
								newset.append("; domain=").append(thisdomain); // $NON-NLS-1$
								newset.append("; path=").append(thispath); // $NON-NLS-1$
	
								String newsetcookieval = newset.toString();
								// this implementation of HttpServletRequest seems to have issues... setHeader works as I would
								// expect addHeader to.
								response.setHeader(headername, newsetcookieval);
								if(getDebugHook()!=null) {
									getDebugHook().getDumpResponse().addCookie(headername, newsetcookieval);
								}
							}
						}
					}
				}
				else if (!headername.equalsIgnoreCase("Transfer-Encoding")) { // $NON-NLS-1$
					String headerval = header.getValue();

					if (headername.equalsIgnoreCase("content-type")) {
						int loc = headerval.indexOf(';');
						String type;
						if (loc > 0) {
							type = headerval.substring(0, loc).trim();
						} else {
							type = headerval;
						}
						if (!isMimeTypeAllowed(type)) {
							isCopy = false;
							break;
						} else {
							response.setHeader(headername, headerval);
							if(getDebugHook()!=null) {
								getDebugHook().getDumpResponse().addHeader(headername, headerval);
							}
						}
					} else if ( (statusCode == 401 || statusCode == 403)  && headername.equalsIgnoreCase("WWW-Authenticate")) { // $NON-NLS-1$
						if (headerval.indexOf("Basic") != -1) { // $NON-NLS-1$
							String pathInfo = request.getPathInfo();
							String[] pathParts = (pathInfo.startsWith("/") ? pathInfo.substring(1) : pathInfo).split("/");
							if (pathParts.length > 1) {
								StringBuilder strb = new StringBuilder("Basic realm=\""); // $NON-NLS-1$
								strb.append(request.getContextPath());
								strb.append(request.getServletPath());
								strb.append('/');
								strb.append(pathParts[0]);
								strb.append('/');
								strb.append(pathParts[1]);
								strb.append('"');
								headerval = strb.toString();
								response.setHeader(headername, headerval);
								if(getDebugHook()!=null) {
									getDebugHook().getDumpResponse().addHeader(headername, headerval);
								}
							}
						}
					} else {
						response.setHeader(headername, headerval);
						if(getDebugHook()!=null) {
							getDebugHook().getDumpResponse().addHeader(headername, headerval);
						}
					}
				}
			}

			// Need to move response body over too
			if(statusCode == HttpServletResponse.SC_NO_CONTENT || statusCode == HttpServletResponse.SC_NOT_MODIFIED) {
				response.setHeader("Content-Length", "0");
				if(getDebugHook()!=null) {
					getDebugHook().getDumpResponse().addHeader("Content-Length", "0");
				}
			} else if(isCopy) {
				HttpEntity entity = clientResponse.getEntity();
				InputStream inStream = entity.getContent();
				if (inStream != null) {
					OutputStream os = response.getOutputStream();
					if(TRACE) {
						OutputStream tos = new TraceOutputStream(os, System.out, false);
						os = tos;
					}
					StreamUtil.copyStream(inStream, os);
					os.flush();
				}
				else {
					response.setHeader("Content-Length", "0");
					if(getDebugHook()!=null) {
						getDebugHook().getDumpResponse().addHeader("Content-Length", "0");
					}
				}
			}
		} catch(IOException ex) {
			throw new ServletException(ex);
		}
	}

	protected void writeErrorResponse(String errorMessage, String[] parameters, String[] values, HttpServletResponse response, HttpServletRequest request) throws ServletException {
		writeErrorResponse(404, errorMessage, parameters, values, response, request);
	}
	public static void writeErrorResponse(int httpstatus, String errorMessage, String[] parameters, String[]values,
			HttpServletResponse response, HttpServletRequest request) throws ServletException {
		JsonJavaObject o = new JsonJavaObject();
		o.putString("Message", errorMessage);
		List<JsonJavaObject> params = new ArrayList<JsonJavaObject>();
		if (parameters != null && parameters.length > 0) {
			for (int i = 0; i < parameters.length; i++) {
				JsonJavaObject e = new JsonJavaObject();
				e.putString(parameters[i],values[i]);
				params.add(e);
			}
		}
		o.putObject("Parameters", params);

		try {
			response.setStatus(httpstatus);
			//response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.addHeader("content-type", "application/json"); // $NON-NLS-1$ $NON-NLS-2$
			response.addHeader("Server", "SBT"); // $NON-NLS-1$ $NON-NLS-2$
			response.addHeader("Connection", "close"); // $NON-NLS-1$ $NON-NLS-2$
			response.addDateHeader("Date", System.currentTimeMillis()); // $NON-NLS-1$
			response.getOutputStream().print(JsonGenerator.toJson(JsonJavaFactory.instanceEx, o));
		}catch (Exception e) {}
	}

	private String[][] getCookieStrings(String set_cookie) {
		String[] pairs = set_cookie.split(";");
		String[][] ret = new String[pairs.length][];

		for (int i = 0; i < pairs.length; i++) {
			String p = pairs[i];
			ret[i] = new String[2];

			int eqloc = p.indexOf('=');
			if (eqloc < 0) {
				ret[i][0] = p;
				ret[i][1] = null;
			}
			else {
				ret[i][0] = p.substring(0, eqloc).trim();
				ret[i][1] = p.substring(eqloc + 1).trim();
			}
		}

		return ret;
	}

	protected URI getRequestURI(HttpServletRequest request) throws ServletException {
		try {
			String orgUrl = getRequestURIPath(request);
			String queryargs = getRequestURLQueryString(request);
			if(StringUtil.isNotEmpty(queryargs)) {
				orgUrl += '?' + queryargs;
			}
			URI url = new URI(orgUrl);
			return url;
		} catch (URISyntaxException ex) {
			throw new ServletException(ex);
		}
	}
	protected String getRequestURIPath(HttpServletRequest request) throws ServletException {
		String pathinfo = request.getPathInfo();
		int pos = pathinfo.indexOf("/http/"); // $NON-NLS-1$
		if (pos == -1) {
			pos = pathinfo.indexOf("/https/"); // $NON-NLS-1$
		}
		if (pos > 0) {
			pathinfo = pathinfo.substring(pos);
		}
		String orgUrl = pathinfo.substring(1).replaceFirst("\\/", "://");
		return orgUrl;
	}

	protected String getRequestURLQueryString(HttpServletRequest request) throws ServletException {
		String queryargs = request.getQueryString();
		return queryargs;
	}


	private static String encodeBase64(byte[] b) {
		try {
			StringWriter sw = new StringWriter();
			Base64.OutputStream b64 = new Base64.OutputStream(new WriterOutputStream(sw));

			int len = b.length;
			for( int i=0; i<len; i++) {
				int c = b[i];
				b64.write(c);
			}
			b64.flushBuffer();

			return sw.toString();
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	private static byte[] decodeBase64(String s) {
		try {
			Base64.InputStream b64 = new Base64.InputStream(new ReaderInputStream(new StringReader(s)));
			ByteBuffer bb = ByteBuffer.allocate(1024*4); // max cookie size
			int byt;
			while( (byt = b64.read()) >= 0)  {
				bb.put((byte) (byt&0xFF));
			}
			return bb.array();
		} catch(IOException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	protected static String encodeCookieNameAndPath(String name, String path, String domain) throws ServletException {
		try {
			String s = new StringBuilder(name)
			.append(';')
			.append(path)
			.append(';')
			.append(domain)
			.toString();
			String encoded = URLEncoder.encode(s, "UTF-8");
			return encoded;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	protected static String[] decodeCookieNameAndPath(String encoded) throws ServletException {
		try {
			String s = URLDecoder.decode(encoded, "UTF-8");
			return StringUtil.splitString(s, ';');
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}



	//    protected static String encodeCookieNameAndPath(String name, String path, String domain) throws ServletException {
	//        final ByteBuffer bb = ByteBuffer.allocate(1024*4); // max cookie size
	//        final byte[] buffer = new byte[512];
	//        final Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);
	//        String ret = null;
	//        try {
	//            // Join name and path strings with ';' and get the UTF-8 bytes.
	//            byte[] in = new StringBuilder(name)
	//                            .append(';')
	//                            .append(path)
	//                            .append(';')
	//                            .append(domain)
	//                            .toString().getBytes("UTF-8");
	//            deflater.setInput(in);
	//            deflater.finish();
	//            int written = 0;
	//
	//            // Deflate the byte array into out
	//            while ( (written = deflater.deflate(buffer)) == buffer.length ) {
	//                bb.put(buffer, bb.position(), written);
	//            }
	//            bb.put(buffer, bb.position(), written);
	//            byte[] out = new byte[bb.position()];
	//            System.arraycopy(bb.array(), 0, out, 0, out.length);
	//
	//            // Base64Encode the out byte array and replace unsafe characters.
	//            // Replace padding '=' with a single number of how many padding chars there were.
	//            ret = encodeBase64(out).replaceAll("\\s", "").replaceAll("[/]", "_").replaceAll("[+]", "-"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	//            int i = ret.indexOf('=');
	//            if (i < 0)
	//                i = ret.length();
	//            int len = ret.length() - i;
	//            ret = ret.substring(0, i);
	//            ret = ret + len;
	//        } catch (Throwable t) {}
	//        return ret;
	//    }
	//    protected static String[] decodeCookieNameAndPath(String encoded) throws ServletException {
	//        final Inflater inflater = new Inflater(true);
	//        final ByteBuffer bb = ByteBuffer.allocate(1024*4); // max cookie size
	//        final byte[] buffer = new byte[512];
	//
	//        String[] ret = null;
	//        try {
	//            // Replace the safe characters with the proper Base64 alphabet characters.
	//            encoded = encoded.replaceAll("[_]", "/").replaceAll("[-]", "_"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	//
	//            // Replace the padding digit with the correct number of padding characters.
	//            int len = 0;
	//            try {
	//                len = Integer.parseInt(encoded.substring(encoded.length() - 1));
	//                encoded = encoded.substring(0, encoded.length() - 1);
	//            } catch (Exception e) {
	//            }
	//            final String padding = "==="; // $NON-NLS-1$
	//            encoded = encoded + padding.substring(0, len);
	//
	//            // Base64Decode the string into a byte array 'in'.
	//            byte[] in = decodeBase64(encoded);
	//            inflater.setInput(in);
	//
	//            // Inflate 'in' into the byte buffer.
	//            int written = 0;
	//            while ( (written = inflater.inflate(buffer)) == buffer.length ) {
	//                bb.put(buffer, bb.position(), written);
	//            }
	//            bb.put(buffer, bb.position(), written);
	//
	//            // Make a new string from the inflated bytebuffer, using UTF-8 charset
	//            String namepath = new String(bb.array(), 0, bb.position(), "UTF-8");
	//
	//            // split the name/path pair into the return array value.
	//            ret = namepath.split(";");
	//        } catch (Throwable t) {}
	//        return ret;
	//    }
}