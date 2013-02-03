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
package com.ibm.sbt.service.core;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.servlet.BaseToolkitServlet;

/**
 * Servlet dispatcher.
 * 
 * This servlet handles a request from a servlet container and dynamically dispatches it to another servlet
 * using a series of ServletFactory factories.
 * It allows a Java developer to execute servlets without having to get them registered in web.xml
 * 
 * @author priand
 */
public class ServletDispatcher extends BaseToolkitServlet {

	private static final long serialVersionUID = 1L;
	
	private ArrayList<ServletFactory> factories = new ArrayList<ServletFactory>();
	
    public ServletDispatcher() {
    }
    
    public synchronized void register(ServletFactory factory) {
    	factories.add(factory);
    	if(getServletConfig()!=null) {
			try {
				factory.init(getServletConfig());
			} catch(Exception ex) {
				// Make sure that all the factories had been initialized
				Platform.getInstance().log(ex);
			}
    	}
    }
    
    public synchronized void unregister(ServletFactory factory) {
    	if(factories.remove(factory)) {
        	if(getServletConfig()!=null) {
    			try {
    	    		factory.destroy();
    			} catch(Exception ex) {
    				// Make sure that all the factories had been initialized
    				Platform.getInstance().log(ex);
    			}
        	}
    	}
    }
    
    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		// Init the factories already registered
		if(!factories.isEmpty()) {
			for(ServletFactory f: factories) {
				try {
					f.init(servletConfig);
				} catch(Exception ex) {
					// Make sure that all the factories had been initialized
					Platform.getInstance().log(ex);
				}
			}
		}
	}

	@Override
	public void destroy() {
		// Destroy the factories
		if(!factories.isEmpty()) {
			for(ServletFactory f: factories) {
				try {
					f.destroy();
				} catch(Exception ex) {
					// Make sure that all the factories had been destroyed
					Platform.getInstance().log(ex);
				}
			}
		}
		super.destroy();
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int match = -1;
		ServletFactory factory = null;
		
		// Select the best factory for this request
		int count = factories.size();
		for(int i=0; i<count; i++) {
			int m = factories.get(i).match(request);
			if(m>match) {
				match = m;
				factory = factories.get(i);
			}
		}
		
		// If there is a matching factory, then delegate the request
		if(factory!=null) {
			factory.service(request, response);
		} else {
	        // No factory is not available so it is a 404
	        String message = "Invalid proxy handler {0}";
	        service404(request,response,message,request.getPathInfo());
		}
    }
}