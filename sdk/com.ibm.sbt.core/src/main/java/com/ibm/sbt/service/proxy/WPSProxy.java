/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.service.proxy;

/**
 * Class representing WPS proxy ( to be used with WebSphere Portal Server )
 * @author Manish Kataria
 */


import java.util.logging.Logger;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.proxy.SBTProxy;

public class WPSProxy extends AbstractProxy {
	
	static final String	sourceClass	= SBTProxy.class.getName();
	static final Logger	logger = Logger.getLogger(sourceClass);
	private final String module = "WPSProxy";


	@Override
	public String rewriteUrl(String apiUrl) {
		
		/*
		 * Traffic should flow through configured Portal proxy
		 * Eg : http://qs.renovations.com:10039/wps/proxy/https/qs.renovations.com:444/connections/opensocial/basic/rest/people/@me/
		 */
		
		// return same parameter value if either proxy or api url is null
		if(StringUtil.isEmpty(getProxyUrl()) || StringUtil.isEmpty(apiUrl)){
			return apiUrl;
		}
		
		// Take care of last url seperator "/"
		StringBuffer proxyUrl = new StringBuffer(getProxyUrl());
		
		if(!(UrlUtil.isAbsoluteUrl(proxyUrl.toString()))){ // if proxy url specified in proxy config is relative, assume server same as of SBT
			// Url is relative, append server to it
			proxyUrl = new StringBuffer(UrlUtil.makeUrlAbsolute(Context.get().getHttpRequest(),proxyUrl.toString()));
		}
		
		if(!(StringUtil.endsWithIgnoreCase(proxyUrl.toString(), "/"))){
			proxyUrl.append("/");
		}
		
		//Replace http protocol in proxy format
		if(apiUrl.contains("http://")){
			apiUrl = apiUrl.replace("http://", "http/");
		}else if(apiUrl.contains("https://")){
			apiUrl = apiUrl.replace("https://", "https/");
		}
		
		proxyUrl.append(apiUrl);
		return proxyUrl.toString();
	}


	@Override
	public String getProxyModule() {
		return module;
	}

}
