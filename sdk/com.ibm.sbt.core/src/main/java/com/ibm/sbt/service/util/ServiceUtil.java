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
package com.ibm.sbt.service.util;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.sbt.security.authentication.oauth.OAuthException;
import com.ibm.sbt.service.core.servlet.ServiceServlet;


/**
 * Proxy related Utilities.
 * 
 * @author Philippe Riand
 */
public class ServiceUtil {

	/**
	 * Get the URL to the proxy.
	 * 
	 * @param context
	 * @return
	 * @throws OAuthException
	 */
    public static String getProxyUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String proxyBaseUrl = UrlUtil.makeUrlAbsolute(request,PathUtil.concat(contextPath,ServiceServlet.getServletPath(),'/'));
        return proxyBaseUrl;
    }
	
	/**
	 * Encode a URL to go through a proxy.
	 * This method create a URL going through a proxy, based on a proxy base URL and
	 * the actual URL to encode
	 * 
	 */
	public static String encodeUrl(String proxyBaseUrl, String url) {
		StringBuilder b = new StringBuilder();
		b.append(proxyBaseUrl);
		if(!proxyBaseUrl.endsWith("/")) {
			b.append('/');
		}
		if(url.startsWith("http://")) {
			b.append("http/");
			b.append(url.substring("http://".length()));
		} else if(url.startsWith("https://")) {
			b.append("https/");
			b.append(url.substring("https://".length()));
		} else {
			b.append(url);
		}
		return b.toString();
	}
}
