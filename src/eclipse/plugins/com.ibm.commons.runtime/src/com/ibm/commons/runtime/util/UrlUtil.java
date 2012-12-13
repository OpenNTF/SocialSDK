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
package com.ibm.commons.runtime.util;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;

/**
 * URL Utilities.
 * 
 * @author Philippe Riand
 */
public class UrlUtil {
	
    public static String getRequestUrl(HttpServletRequest req) {
    	return getRequestUrl(req, true);
    }
    public static String getRequestUrl(HttpServletRequest req, boolean querystring) {
    	// We cannot use request.getRequestURL() as this uses requestURI() which does not
    	// include the full path after a Domino redirection (mydb.nsf/).
    	// We have to recompose it entirely here
    	StringBuilder b = new StringBuilder();
        String scheme = req.getScheme();
        b.append(scheme);
        b.append("://");
        b.append(req.getServerName());
        if(scheme.equals("http") && req.getServerPort() != 80) { // $NON-NLS-1$
            b.append(":");
            b.append(req.getServerPort());
        }
        if(scheme.equals("https") && req.getServerPort() != 443) { // $NON-NLS-1$
            b.append(":");
            b.append(req.getServerPort());
        }
        String contextPath =  req.getContextPath();
        if(StringUtil.isNotEmpty(contextPath)) {
        	b.append(contextPath);
        }
        String servletPath =  req.getServletPath();
        if(StringUtil.isNotEmpty(servletPath)) {
        	b.append(servletPath);
        }
        String pathInfo =  req.getPathInfo();
        if(StringUtil.isNotEmpty(pathInfo)) {
        	b.append(pathInfo);
        }
        if(querystring) {
			String qs = req.getQueryString();
			if(StringUtil.isNotEmpty(qs)) {
	            b.append("?");
				b.append(qs);
			}
        }
        return b.toString();
    }

    public static String getBaseUrl(HttpServletRequest req) {
    	StringBuilder b = new StringBuilder(64);
    	return appendBaseUrl(b,req).toString();
    }
    public static StringBuilder appendBaseUrl(StringBuilder b, HttpServletRequest req) {
    	appendServerUrl(b,req);
    	String contextPath = req.getContextPath();
    	b.append(contextPath);
    	return b;
    }

    public static String getServerUrl(HttpServletRequest req) {
    	StringBuilder b = new StringBuilder(64);
    	return appendServerUrl(b,req).toString();
    }
    public static StringBuilder appendServerUrl(StringBuilder b, HttpServletRequest req) {
    	String scheme = req.getScheme();
    	String server = req.getServerName();
    	int port = req.getServerPort();

    	b.append(scheme);
    	b.append("://");
    	b.append(server);
    	if(!((scheme.equals("http") && port==80)||(scheme.equals("https") && port==443))) {
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
		if (path.indexOf("://") < 0) {
			// Put that in a utility!
			StringBuilder b = new StringBuilder();
			String scheme = request.getScheme();
			b.append(scheme);
			b.append("://");
			b.append(request.getServerName());
			if (scheme.equals("http") && request.getServerPort() != 80) { //$NON-NLS-1$
				b.append(":");
				b.append(request.getServerPort());
			}
			if (scheme.equals("https") && request.getServerPort() != 443) { //$NON-NLS-1$
				b.append(":");
				b.append(request.getServerPort());
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
	 * This method does not provide a comprehensive test for all URL cases, but
	 * should suffice for most.
	 * 
	 * @param url the non-null URL to test
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
