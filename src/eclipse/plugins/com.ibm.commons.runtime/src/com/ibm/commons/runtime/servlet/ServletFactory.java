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
	 * Factory that manages a unique servlet with its full life cycle.
	 */
	public static abstract class SingleServlet extends ServletFactory {
		private Object clazz;
		private HttpServlet servlet;
		public SingleServlet(Object clazz) {
			this.clazz = clazz;
		}
		@Override
		public void destroy() {
			if(servlet!=null) {
				servlet.destroy();
				servlet = null;
			}
		}
		protected synchronized void createServlet() throws ServletException {
	        if(servlet==null) {
	        	servlet = newServletInstance(clazz);
	        	servlet.init(getServletConfig());
	        }
		}
	}
	public static class SingleServletMatcher implements ServletMatcher {
		private SingleServlet matcher;
		private int matchLength;
		public SingleServletMatcher(SingleServlet matcher, int matchLength) {
			this.matcher = matcher;
			this.matchLength = matchLength;
		}
		@Override
		public int matchLengh() {
			return matchLength;
		}
		@Override
		public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			if(matcher.servlet==null) {
				matcher.createServlet();
			}
			matcher.servlet.service(request,response);
		}
	}

	/**
	 * Concrete factory based on the PathInfo.
	 */
	public static class PathInfoFactory extends SingleServlet {
		private String pathInfo;
		public PathInfoFactory(Object clazz, String pathInfo) {
			super(clazz);
			this.pathInfo = pathInfo;
	        if(!this.pathInfo.startsWith("/")) {
	        	// A path info, as returned by the servlet container, always starts with a '/'
	           this.pathInfo = "/"+this.pathInfo;
	        }
		}
		@Override
		public ServletMatcher match(HttpServletRequest request) throws ServletException {
	        String pi = request.getPathInfo();
        	// Warn: the pathinfo can be null on certain web app server
	        if(StringUtil.isNotEmpty(pi)) {
		        int matchLength = pi.startsWith(this.pathInfo) ? this.pathInfo.length() : -1;
		        if(matchLength>=0) {
		        	// Ensure that the pathinfo actual matches
		        	if(matchLength==pi.length() || pi.charAt(matchLength)=='/') {
		        		return new SingleServletMatcher(this,matchLength);
		        	}
		        }
	        }
	        return null;
		}
	}

	private ServletConfig servletConfig;
		
	public ServletFactory() {
	}

	public ServletFactory(Object clazz) {
	}
	
	public ServletConfig getServletConfig() {
		return servletConfig;
	}

	public void init(ServletConfig servletConfig) throws ServletException {
		this.servletConfig = servletConfig; 
	}

	public void destroy() {
	}

	public abstract ServletMatcher match(HttpServletRequest request) throws ServletException;

    protected HttpServlet newServletInstance(Object clazz) throws ServletException {
    	if(clazz!=null) {
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
    	} else {
        	String msg = StringUtil.format("Cannot instanciate empty servlet class");
        	throw new ServletException(msg);
    	}
    }
}
