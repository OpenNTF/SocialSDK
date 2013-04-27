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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sbt.service.basic.AbstractFileProxyService;
import com.ibm.sbt.service.basic.ConnectionsFileProxyService;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.service.basic.SmartCloudFileProxyService;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class FileHandler extends AbstractServiceHandler {

	private static final long serialVersionUID = -4063343007626745356L;
	public static final String URL_PATH = "files";
	private Map<String, AbstractFileProxyService> fileProxyMap = new HashMap<String, AbstractFileProxyService>();

	public FileHandler() {
		fileProxyMap.put("connections", new ConnectionsFileProxyService());
		fileProxyMap.put("smartcloud", new SmartCloudFileProxyService());
	}

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		String pathinfo = request.getPathInfo().substring(request.getPathInfo().indexOf("/files")); 
		String[] pathTokens = pathinfo.split("/");		
		if (pathTokens.length > 4) {
			String serviceType = pathTokens[3];
			ProxyEndpointService proxyEndpointService = fileProxyMap.get(serviceType);
			if (proxyEndpointService != null) {
				proxyEndpointService.service(request, response);
			}
		}

	}
}