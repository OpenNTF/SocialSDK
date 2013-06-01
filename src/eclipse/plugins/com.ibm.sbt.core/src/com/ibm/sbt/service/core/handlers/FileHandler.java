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
package com.ibm.sbt.service.core.handlers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.service.basic.ProxyService;
import com.ibm.sbt.service.ext.DefaultProxyEndpointServiceProvider;
import com.ibm.sbt.service.ext.ProxyEndpointServiceProvider;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class FileHandler extends AbstractServiceHandler {	

	private static final long serialVersionUID = -4063343007626745356L;
	
	static final String	sourceClass	= FileHandler.class.getName();
	static final Logger	logger		= Logger.getLogger(sourceClass);
	
	public static final String URL_PATH = "files";


	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		ProxyEndpointService proxyEndpointService = null;
		// /files/<<endpointName>>/<<serviceType>>?param1=value1&parm2=value2
		String pathinfo = request.getPathInfo().substring(request.getPathInfo().indexOf("/files")); 
		String[] pathTokens = pathinfo.split("/");		
		if (pathTokens.length > 4) {
			String serviceType = pathTokens[3];
			Application application = Application.get();
			// ProxyEndpointServiceProvider.PROXY_SERVICE_TYPE is the extension ID
			List<Object> proxyServiceProviders = application.findServices(ProxyEndpointServiceProvider.PROXY_SERVICE_TYPE);
			if(proxyServiceProviders != null && !proxyServiceProviders.isEmpty()){
				for(Object o : proxyServiceProviders){
					ProxyEndpointServiceProvider pvdr = (ProxyEndpointServiceProvider)o;
					proxyEndpointService = pvdr.createProxyEndpointService(serviceType);
					if(proxyEndpointService != null){
						break;
					}
				}
			}
			else {
				DefaultProxyEndpointServiceProvider proxyEndpointServiceProvider = new DefaultProxyEndpointServiceProvider();
				proxyEndpointService = proxyEndpointServiceProvider.createProxyEndpointService(serviceType);
			}
		}
		if(proxyEndpointService != null){
			proxyEndpointService.service(request, response);
		}
		else{
			logger.log(Level.SEVERE, "ProxyEndpoint Service could not be retrieved for PathInfo {0}", pathinfo);
			ProxyService.writeErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "ProxyEndpoint Service could not be retrieved", new String[] {}, new String[] {}, response, request);			
		}
	}
}