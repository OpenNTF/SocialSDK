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
package com.ibm.commons.runtime.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.servlet.BaseHttpServlet;

/**
 * Provides a base class for toolkit HTTP servlets. <code>BaseToolkitServlet</code> manages the global
 * <code>Application</code> instance and the per request <code>Context</code> instance.
 * 
 * @author priand
 * @author mwallace
 *
 */
abstract public class BaseToolkitServlet extends BaseHttpServlet {

	private Application application;
	
	private static final long serialVersionUID = 1L;
	
	public BaseToolkitServlet() {
	}
	
	public boolean useApplication() {
		return true;
	}
	
	public boolean useContext() {
		return true;
	}

	/**
	 * Return the <code>Application</code> instance associated with this servlet.
	 * 
	 * @return the <code>Application</code> instance associated with this servlet.
	 */
	public Application getApplication() {
		return application;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		// create the application instance
		if(useApplication()) {
			this.application = Application.init(config.getServletContext());
		}
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.GenericServlet#destroy()
	 */
	@Override
	public void destroy() {
		// destroy the application instance
		if(application!=null) {
			Application.destroy(application);
			application = null;
		}

		super.destroy();
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 */
	@Override
	public void service(ServletRequest request, ServletResponse response)
			throws ServletException, IOException {
		// create the context instance
		if(useContext()) {
	    	Context context = Context.init(application,request, response);
	    	try {
	    		super.service(request,response);
	    	} finally {
	    		Context.destroy(context);
	    	}
		} else {
    		super.service(request,response);
		}
	}
}
