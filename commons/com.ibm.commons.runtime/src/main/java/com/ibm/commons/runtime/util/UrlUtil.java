/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.commons.runtime.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;

/**
 * URL Utilities.
 * 
 * @author Philippe Riand
 */
public class UrlUtil {

	public static final int	URL_SERVER		= 0;
	public static final int	URL_CONTEXTPATH	= 1;
	public static final int	URL_SERVLETPATH	= 2;
	public static final int	URL_PATHINFO	= 3;
	public static final int	URL_QUERYSTRING	= 4;

	public static String getRequestUrl(HttpServletRequest req) {
		return getRequestUrl(req, true);
	}

	public static String getRequestUrl(HttpServletRequest req, boolean querystring) {
		return getRequestUrl(req, URL_QUERYSTRING);
	}

	public static String getRequestUrl(HttpServletRequest req, int url) {
		// We cannot use request.getRequestURL() as this uses requestURI() which does not
		// include the full path after a Domino redirection (mydb.nsf/).
		// We have to recompose it entirely here
		StringBuilder b = new StringBuilder();
		String scheme = req.getScheme();
		b.append(scheme);
		b.append("://");
		b.append(req.getServerName());
		if (scheme.equals("http") && req.getServerPort() != 80) { // $NON-NLS-1$
			b.append(":");
			b.append(req.getServerPort());
		}
		if (scheme.equals("https") && req.getServerPort() != 443) { // $NON-NLS-1$
			b.append(":");
			b.append(req.getServerPort());
		}
		if (url >= URL_CONTEXTPATH) {
			String contextPath = req.getContextPath();
			if (StringUtil.isNotEmpty(contextPath)) {
				b.append(contextPath);
			}
			if (url >= URL_SERVLETPATH) {
				String servletPath = req.getServletPath();
				if (StringUtil.isNotEmpty(servletPath)) {
					b.append(servletPath);
				}
				if (url >= URL_PATHINFO) {
					String pathInfo = req.getPathInfo();
					if (StringUtil.isNotEmpty(pathInfo)) {
						b.append(pathInfo);
					}
					if (url >= URL_QUERYSTRING) {
						String qs = req.getQueryString();
						if (StringUtil.isNotEmpty(qs)) {
							b.append("?");
							b.append(qs);
						}
					}
				}
			}
		}
		return b.toString();
	}

	/**
	 * getParamsMap
	 * <p>
	 * Method to obtain a map of parameters from the request Uri containing query string parameters added to
	 * it.
	 * 
	 * @param requestUriWithQueryParams
	 *            - input is the request Uri containing the parameters added to it as Query String parameters.
	 * @return - Map of parameters, extracted from the Uri
	 */
	public static Map<String, String> getParamsMap(String requestUriWithQueryParams) {
		Map<String, String> mapOfParams = new TreeMap<String, String>();
		if (requestUriWithQueryParams.contains("?")) {
			// if parameters are a part of the request uri
			String queryStr = requestUriWithQueryParams.substring(requestUriWithQueryParams.indexOf("?") + 1,
					requestUriWithQueryParams.length());
			requestUriWithQueryParams = requestUriWithQueryParams.substring(0,
					requestUriWithQueryParams.indexOf("?"));
			String[] paramsList = queryStr.split("&");
			for (String i : paramsList) {
				String[] parameter = i.split("=");
				mapOfParams.put(parameter[0], parameter[1]);
			}
		}
		return mapOfParams;
	}

	public static String getBaseUrl(HttpServletRequest req) {
		StringBuilder b = new StringBuilder(64);
		return appendBaseUrl(b, req).toString();
	}

	public static StringBuilder appendBaseUrl(StringBuilder b, HttpServletRequest req) {
		appendServerUrl(b, req);
		String contextPath = req.getContextPath();
		b.append(contextPath);
		return b;
	}

	public static String getServerUrl(HttpServletRequest req) {
		StringBuilder b = new StringBuilder(64);
		return appendServerUrl(b, req).toString();
	}

	public static StringBuilder appendServerUrl(StringBuilder b, HttpServletRequest req) {
		String scheme = req.getScheme();
		String server = req.getServerName();
		int port = req.getServerPort();

		b.append(scheme);
		b.append("://");
		b.append(server);
		if (!((scheme.equals("http") && port == 80) || (scheme.equals("https") && port == 443))) {
			b.append(":");
			b.append(Integer.toString(port));
		}
		return b;
	}

	public static String getContextUrl(HttpServletRequest req) {
		String serverUrl = getServerUrl(req);
		return PathUtil.concat(serverUrl, req.getContextPath(), '/');
	}

	public static String makeUrlAbsolute(HttpServletRequest request, String path) {
		return makeUrlAbsolute(request, path, false);
	}
	
	public static String makeUrlAbsolute(HttpServletRequest request, String path, boolean useClientAddress) {
		if (path.indexOf("://") < 0) {
			String server = null;
			int port = -1;
			String protocol = null;
			if (useClientAddress) {
				URL clientRequest;
				try {
					clientRequest = new URL(request.getRequestURL().toString());
				} catch (MalformedURLException e) {
					//unrecoverable exception at this point - URL is generated from a valid URL.
					throw new RuntimeException("The request URL is not valid", e);
				}
				server = clientRequest.getHost();
				port = clientRequest.getPort() == -1 ? clientRequest.getDefaultPort() : clientRequest.getPort();
				protocol = clientRequest.getProtocol();
			} else {
				server = request.getServerName();
				port = request.getServerPort();
				protocol = request.getScheme();
			}
			
			// Put that in a utility!
			StringBuilder b = new StringBuilder();
			b.append(protocol);
			b.append("://");
			b.append(server);
			if (port != -1 && port != 80 && protocol.equals("http") ) { //$NON-NLS-1$
				b.append(":");
				b.append(port);
			}
			if (port != -1 && port != 443 && protocol.equals("https") ) { //$NON-NLS-1$
				b.append(":");
				b.append(port);
			}
			if (!path.startsWith("/")) {
				b.append("/");
			}
			b.append(path);
			return b.toString();
		}

		return path;
	}

	/**
	 * The following are examples of absolute URLs:
	 * <UL>
	 * <LI>http://host:80/path</LI>
	 * <LI>ftp://user:pw@host:port/path</LI>
	 * <LI>news:a.news.group</LI>
	 * <LI>file:/c:/config.sys</LI>
	 * <LI>mailto:alasdair@domain.tld</LI>
	 * </UL>
	 * This method does not provide a comprehensive test for all URL cases, but should suffice for most.
	 * 
	 * @param url
	 *            the non-null URL to test
	 * @return true if the pattern matches that of an absolute URL
	 */
	public static boolean isAbsoluteUrl(String url) {
		// see here for more: http://www.w3.org/Addressing/URL/url-spec.txt

		// most URLs will be relative
		int colon = url.indexOf(':');
		if (colon < 0) {
			return false;
		}

		// assume the protocol is a simple alphanumeric String
		for (int i = 0; i < colon; i++) {
			char ch = url.charAt(i);
			if (Character.isLetterOrDigit(ch)) {
				continue;
			} else {
				return false;
			}
		}
		return true;
	}

}
