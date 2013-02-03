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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.util.StringUtil;

/**
 * Servlet Factory.
 * 
 * Used to dispatch a request from a ServletDispatcher to an actual servlet. This class is also
 * responsible for creating the instance of the servlet when appropriate (generally lazily) and 
 * get it initialized with the proper servlet config object.
 * 
 * The created servlet has to be held by the factory if it should be re-used later within a subsequent
 * request. Also, its destroy() method should be called when it is about to be released, generally when
 * the factory is unregistered from the dispatcher servlet.
 * 
 * @author priand
 */
public abstract class ServletFactory {
	
	/**
	 * Concrete factory based on the path info seen as a name.
	 */
	public static class PathInfoName extends ServletFactory {
		private String name;
		public PathInfoName(Object clazz, String name) {
			super(clazz);
			this.name = name;
		}
		@Override
		public int match(HttpServletRequest request) throws ServletException {
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
	        return StringUtil.equals(this.name, pathInfo) ? this.name.length() : -1;
		}
	}

	private ServletConfig servletConfig;
	
	private Object clazz;
	private HttpServlet servlet;
		
	public ServletFactory(Object clazz) {
		this.clazz = clazz;
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig; 
	}

	public void destroy() {
		if(servlet!=null) {
			servlet.destroy();
			servlet = null;
		}
	}

	public abstract int match(HttpServletRequest request) throws ServletException;

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(servlet==null) {
			createServlet();
		}
		servlet.service(request,response);
	}
	
	protected synchronized void createServlet() throws ServletException {
        if(servlet==null) {
        	servlet = newServletInstance(clazz);
        	servlet.init(servletConfig);
        }
	}

    protected HttpServlet newServletInstance(Object clazz) throws ServletException {
        try {
            if(clazz instanceof Class<?>) {
                return (HttpServlet)((Class<?>)clazz).newInstance();
            }
            if(clazz instanceof String) {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                if(cl==null) {
                    cl = getClass().getClassLoader();
                }
                Class<?> c = cl.loadClass((String)clazz);
                return (HttpServlet)c.newInstance();
            }
        	String msg = StringUtil.format("Invalid servlet class object {0}", clazz);
        	throw new ServletException(msg);
        } catch(Exception ex) {
        	String msg = StringUtil.format("Cannot instanciate servlet class {0}", clazz);
        	throw new ServletException(msg,ex);
        }
    }
}
