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

package com.ibm.commons.runtime.impl.servlet;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.impl.AbstractContext;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;


/**
 * SBT Context implementation for a servlet environment.
 *
 * @author Philippe Riand
 */
public class ContextServlet extends AbstractContext {
	
	
	protected ContextServlet(Application application, HttpServletRequest request, HttpServletResponse response) {
		super(application,request,response);
	}
	
	@Override
	public String getCurrentUserId() {
		Principal p = getHttpRequest().getUserPrincipal();
		if(p!=null) {
			String name = p.getName();
			// Make sure 'anonymous' is always returned, with the right caps
			if(StringUtil.isNotEmpty(name) && !name.equalsIgnoreCase(ANONYMOUS)) {
				return name;
			}
		}
		return ANONYMOUS;
	}
	
	@Override
	public HttpServletRequest getRequest() {
		return (HttpServletRequest)super.getRequest();
	}

	@Override
	public HttpServletResponse getResponse() {
		return (HttpServletResponse)super.getResponse();
	}

	@Override
	public void sendRedirect(String redirectUrl) throws IOException {
	    getHttpResponse().sendRedirect(redirectUrl);
	}


	@Override
	public String encodeUrl(String url) {
    	return PathUtil.concat(UrlUtil.getBaseUrl(getHttpRequest()),url, '/');
	}

	@Override
	public ApplicationServlet getApplication() {
		return (ApplicationServlet)super.getApplication();
	}
	
	@Override
	public ApplicationServlet getApplicationUnchecked() {
		return (ApplicationServlet)super.getApplicationUnchecked();
	}
	
	//
	// Access to the scopes
	//
	@Override
	protected Map<String,Object> createSessionMap() {
		return new SessionMap(getHttpRequest());
	}
	@Override
	protected Map<String,Object> createRequestMap() {
		return new RequestMap(getHttpRequest());
	}
	@Override
	protected Map<String,Object> createRequestParameterMap() {
		return new RequestParameterMap(getHttpRequest());
	}
	@Override
	protected Map<String,Object> createRequestCookieMap() {
		return new RequestCookieMap(getHttpRequest());
	}
	
	private static class SessionMap extends AbstractScopeMap {

	    private HttpServletRequest request;

	    SessionMap(HttpServletRequest request) {
	        this.request = request;
	    }

	    @Override
		public Object get(Object key) {
	        HttpSession session = request.getSession(false);
	        return session!=null ? session.getAttribute((String)key) : null;
	    }

	    @Override
		public Object put(String key, Object value) {
	        HttpSession session = request.getSession(true);
	        Object result = session.getAttribute((String)key);
	        session.setAttribute((String)key, value);
	        return result;
	    }

	    @Override
		public Object remove(Object key) {
	        HttpSession session = request.getSession(false);
	        if(session!=null) {
	        	Object result = session.getAttribute((String)key);
	        	session.removeAttribute((String)key);
	        	return result;
	        }
	        return null;
	    }

	    @SuppressWarnings("unchecked")
		@Override
		public Set<Entry<String, Object>> entrySet() {
	        HttpSession session = request.getSession(false);
	        if(session!=null) {
		    	Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();
		        for (Enumeration<String> e = session.getAttributeNames(); e.hasMoreElements();) {
		            String key = e.nextElement();
		            entries.add(new MapEntry(key, session.getAttribute(key)));
		        }
		        return entries;
	        } else {
	        	return Collections.emptySet();
	        }
	    }
	}

	private static class RequestMap extends AbstractScopeMap {

	    private HttpServletRequest request = null;

	    RequestMap(HttpServletRequest request) {
	        this.request = request;
	    }

	    @Override
		public Object get(Object key) {
	        return request.getAttribute((String)key);
	    }

	    @Override
		public Object put(String key, Object value) {
	        Object result = request.getAttribute((String)key);
	        request.setAttribute((String)key, value);
	        return result;
	    }

	    @Override
		public Object remove(Object key) {
	        Object result = request.getAttribute((String)key);
	        request.removeAttribute((String)key);
	        return result;
	    }

	    @SuppressWarnings("unchecked")
		@Override
		public Set<Map.Entry<String, Object>> entrySet() {
	    	Set<Map.Entry<String, Object>> entries = new HashSet<Map.Entry<String, Object>>();
	        for (Enumeration<String> e = request.getAttributeNames(); e.hasMoreElements();) {
	            String key = e.nextElement();
	            entries.add(new MapEntry(key, request.getAttribute(key)));
	        }
	        return entries;
	    }
	}	

	private static class RequestParameterMap extends AbstractScopeMap {

	    private HttpServletRequest request = null;

	    RequestParameterMap(HttpServletRequest request) {
	        this.request = request;
	    }

	    @Override
		public Object get(Object key) {
	        return request.getParameter((String)key);
	    }

	    @Override
		public Object put(String key, Object value) {
	    	throw new IllegalArgumentException("Cannot change the request parameters");
	    }

	    @Override
		public Object remove(Object key) {
	    	throw new IllegalArgumentException("Cannot change the request parameters");
	    }

	    @SuppressWarnings("unchecked")
		@Override
		public Set<Map.Entry<String, Object>> entrySet() {
	    	Set<Map.Entry<String, Object>> entries = new HashSet<Map.Entry<String, Object>>();
	        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
	            String key = e.nextElement();
	            entries.add(new MapEntry(key, request.getParameter(key)));
	        }
	        return entries;
	    }
	}	
	
	private static class RequestCookieMap extends AbstractScopeMap {

	    private Cookie[] cookies;

	    RequestCookieMap(HttpServletRequest request) {
	        this.cookies = request.getCookies();
	    }

	    @Override
		public Object get(Object key) {
	        if(cookies==null) {
	            return null;
	        }
	        for (int i = 0; i < cookies.length; i++) {
	            if (cookies[i].getName().equals(key)) {
	                return cookies[i];
	            }
	        }
	        return null;
	    }

	    @Override
		public Set<Entry<String, Object>> entrySet() {
	        if (cookies != null) {
	        	Set<Entry<String, Object>> entries = new HashSet<Entry<String, Object>>();
	            for(int i = 0; i < cookies.length; i++) {
	            	Cookie c = cookies[i];
	                entries.add(new MapEntry(c.getName(), c));
	            }
		        return entries;
	        }
        	return Collections.emptySet();
	    }
	} 	
}
