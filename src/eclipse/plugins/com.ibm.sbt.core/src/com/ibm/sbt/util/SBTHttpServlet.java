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
package com.ibm.sbt.util;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;

/**
 * Base HTTP servlet that initialize the application and the context.
 * 
 * @author priand
 */
public class SBTHttpServlet extends BaseHttpServlet {

	private static final long serialVersionUID = 1L;

	private Application application;
	
	public Application getApplication() {
		return application;
	}
    
    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
    	super.init(servletConfig);
        this.application = Application.init(servletConfig.getServletContext());
    }
    
    @Override
	public void destroy() {
        Application.destroy(application);
		super.destroy();
	}
    
    @Override
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
    	Context ctx = Context.init(application,request, response);
    	try {
    		super.service(request,response);
    	} finally {
    		Context.destroy(ctx);
    	}
	}   
}
