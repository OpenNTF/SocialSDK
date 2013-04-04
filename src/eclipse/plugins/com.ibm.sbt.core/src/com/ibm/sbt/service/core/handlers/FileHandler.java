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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.sbt.service.basic.ConnectionsFileProxyService;
import com.ibm.sbt.service.basic.ProxyEndpointService;
import com.ibm.sbt.service.basic.SmartcloudFileProxyService;

/**
 * 
 * @author Vineet Kanwal
 *
 */
public class FileHandler extends AbstractServiceHandler {

	private static final long serialVersionUID = -4063343007626745356L;
	public static final String URL_PATH = "files";

	@Override
	public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		String pathinfo = request.getPathInfo();
		String[] pathTokens = pathinfo.split("/");
		ProxyEndpointService proxyEndpointService = null;
		if (pathTokens.length > 4) {
			String fileType = pathTokens[3];
			if ("connections".equals(fileType)) {
				proxyEndpointService = new ConnectionsFileProxyService();
			} else if ("smartcloud".equals(fileType)) {
				proxyEndpointService = new SmartcloudFileProxyService();
			}
			proxyEndpointService.service(request, response);

		}

	}
}