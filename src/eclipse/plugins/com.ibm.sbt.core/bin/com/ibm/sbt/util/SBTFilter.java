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

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;

/**
 * HTTP servlet filter that initialize the application and the context.
 * 
 * @author priand
 */
public class SBTFilter implements Filter {

	private Application application;
	
	@Override
	public void init(FilterConfig config) throws ServletException {
        this.application = Application.init(config.getServletContext());
	}

	@Override
	public void destroy() {
        Application.destroy(application);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    	Context ctx = Context.init(application,request,response);
    	try {
    		chain.doFilter(request, response);
    	} finally {
    		Context.destroy(ctx);
    	}
	}
}
