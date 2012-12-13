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
package com.ibm.sbt.service.core.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.core.IServiceHandler;
import com.ibm.sbt.service.core.ServiceHandlerFactory;
import com.ibm.sbt.util.SBTHttpServlet;

/**
 * Proxy servlet.
 * @author priand
 */
public class ServiceServlet extends SBTHttpServlet {

	public static String getServletPath() {
		return RuntimeConstants.get().getConstant(RuntimeConstants.SERVICE_BASEURL);
	}
	
	private static final long serialVersionUID = 1L;
	
	private String[] services;
	
    public ServiceServlet() {
    }
    
    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		
		String s = servletConfig.getInitParameter("services");
		if(StringUtil.isNotEmpty(s)) {
			this.services = StringUtil.splitString(s, ',', true);
		}
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Find the proxy handler
        String pathInfo = request.getPathInfo();
        if(pathInfo==null) {
        	// Warn: the pathinfo can be null on certain web app server
        	pathInfo = "";
        }
        if(pathInfo.startsWith("/")) {
            pathInfo = pathInfo.substring(1);
        }
        
        int pos = pathInfo.indexOf('/');
        if(pos >=0) {
           pathInfo = pathInfo.substring(0, pos);
        }
        
        // Look if the service is enabled
        if(services!=null && services.length>0) {
        	if(!contains(services, pathInfo)) {
        		service404(request, response, "Unknown service {0}", pathInfo);
        		return;
        	}
        }
        
        // Find and delegate to the proxy handler
        IServiceHandler handler = ServiceHandlerFactory.get().get(pathInfo);
        if(handler!=null) {
            handler.service(request, response);
            return;
        }
        
        // The proxy is not available so it is a 404
        String message = "Invalid proxy handler {0}";  // $NLX-ProxyServlet.Invalidproxyhandler0-1$
        service404(request,response,message,pathInfo);
    }

	private static boolean contains(String[] a, String s) {
		for(int i=0; i<a.length; i++) {
			if(a[i].equals(s)) {
				return true;
			}
		}
		return false;
	}
}