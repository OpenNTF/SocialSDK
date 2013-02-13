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

package com.ibm.xsp.sbtsdk.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.sbt.jslibrary.servlet.LibraryRequest;
import com.ibm.sbt.jslibrary.servlet.LibraryRequestParams;
import com.ibm.sbt.jslibrary.servlet.LibraryServlet;
import com.ibm.xsp.sbtsdk.resources.SbtResourceProvider;

public class ToolkitServlet extends LibraryServlet {

	private static final long serialVersionUID = 1L;

	public static class DominoLibraryRequest extends LibraryRequest {
		public DominoLibraryRequest(HttpServletRequest req, HttpServletResponse resp) {
			super(req, resp);
		}
	    public void init(LibraryRequestParams params) throws ServletException, IOException {
			//public void init(SBTEnvironment defaultEnvironment, String toolkitUrl, String toolkitJsUrl, String proxyUrl, String iframeUrl) throws ServletException, IOException {
			// Calculate the toolkit URL
	    	//http://priand2/xsp/.ibmxspres/.sbtsdk/sbt/Cache.js
			String toolkitUrl = UrlUtil.getServerUrl(getHttpRequest())+"/xsp"+SbtResourceProvider.RESOURCE_PATH+"/";
			String toolkitJsUrl = toolkitUrl;
			
			// Calculate the proxy URL
			String serviceUrl = RuntimeConstants.get().getBaseProxyUrl(getHttpRequest());
			
			params.setToolkitUrl(toolkitUrl);
			params.setToolkitJsUrl(toolkitJsUrl);
			params.setServiceUrl(serviceUrl);
			
			super.init(params);
		}
	    protected String getDefaultJsLib() {
	    	return DominoDojoLibrary.NAME;
	    }
	    
	    protected String getDefaultJsVersion() {
	    	return "1.7";
	    }
}
	
	protected LibraryRequest createLibraryRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		return new DominoLibraryRequest(req, resp);
	}
}
