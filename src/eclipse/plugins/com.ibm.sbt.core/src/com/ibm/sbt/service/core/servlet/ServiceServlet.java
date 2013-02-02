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
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.runtime.servlet.BaseToolkitServlet;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.service.core.ServiceHandlerFactory;

/**
 * Proxy servlet.
 * @author priand
 */
public class ServiceServlet extends BaseToolkitServlet {

	public static String getServletPath() {
		return RuntimeConstants.get().getConstant(RuntimeConstants.SERVICE_BASEURL);
	}
	
	private static final long serialVersionUID = 1L;
	
	private String[] services;
	private HashMap<String,HttpServlet>	servlets = new HashMap<String, HttpServlet>();
	
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
	public void destroy() {
		// Destroy the servlets
		for(HttpServlet s: servlets.values()) {
			try {
				s.destroy();
			} catch(Exception ex) {
				Platform.getInstance().log(ex);
			}
		}
	
		super.destroy();
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Find the servlet corresponding to the request
        HttpServlet servlet = findServlet(request, response);
        
        // Then, delegate to it
        if(servlet!=null) {
        	servlet.service(request, response);
            return;
        }
        
        // The handler is not available so it is a 404
        String message = "Invalid proxy handler {0}";  // $NLX-ProxyServlet.Invalidproxyhandler0-1$
        service404(request,response,message,request.getPathInfo());
    }

	protected HttpServlet findServlet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        
        return findServletByName(request, response, pathInfo);
	}

	protected HttpServlet findServletByName(HttpServletRequest request, HttpServletResponse response, String name) throws ServletException, IOException {
		if(StringUtil.isEmpty(name)) {
			return null;
		}
		
        // Look if the service is enabled
        if(services!=null && services.length>0) {
        	if(!contains(services, name)) {
        		return null;
        	}
        }
        
        HttpServlet servlet = servlets.get(name);
        if(servlet==null) {
        	servlet = createServlet(request, response, name);
        }
        
        return servlet;
	}
	
	protected synchronized HttpServlet createServlet(HttpServletRequest request, HttpServletResponse response, String name) throws ServletException {
        HttpServlet servlet = servlets.get(name);
        if(servlet==null) {
        	ServiceHandlerFactory factory = getServletFactory();
        	if(factory!=null) {
	        	servlet = factory.createServlet(name);
	        	if(servlet!=null) {
	        		servlet.init(getServletConfig());
	        		servlets.put(name, servlet);
	        	}
        	}
        }
        return servlet;
	}
	
	protected ServiceHandlerFactory getServletFactory() {
		return ServiceHandlerFactory.get();
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