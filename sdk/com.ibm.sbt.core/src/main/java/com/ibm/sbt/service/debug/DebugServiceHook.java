/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.service.debug;

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.sbt.service.debug.DebugServiceHookFactory.Type;


/**
 * Debug hook.
 * 
 * This class should be used solely for debugging purposes. It allows a developer to set a hook into the service classes
 * in order to track the web traffic.
 * 
 * Note that this might reveal confidential information so it should only be set when appropriate.
 * 
 * To avoid any misuse, the factory only instanciates a hook if ran from a development environment.
 * 
 * @author Philippe Riand
 */
public abstract class DebugServiceHook {

	protected static class RequestWrapper extends HttpServletRequestWrapper {
		private DumpRequest outputRequest;
		public RequestWrapper(HttpServletRequest request) {
			super(request);
			this.outputRequest = new DumpRequest();
			dump();
		}
		public DumpRequest getServiceRequest() {
			return outputRequest;
		}
		public void dump() {
			outputRequest.setMethod(getMethod());
			outputRequest.setUrl(UrlUtil.getRequestUrl(this));
			for(Enumeration<String> eh=getHeaderNames(); eh.hasMoreElements(); ) {
				String h = eh.nextElement();
				for(Enumeration<String> ev=getHeaders(h); ev.hasMoreElements(); ) {
					String v = ev.nextElement();
					outputRequest.addHeader(h, v);
				}
			}
			Cookie[] cookies = getCookies();
			if(cookies!=null) {
				for(int i=0; i<cookies.length; i++) {
					Cookie c = cookies[i];
					outputRequest.addCookie(c.getName(), c.toString());
				}
			}
			for(Enumeration<String> ep=getParameterNames(); ep.hasMoreElements(); ) {
				String h = ep.nextElement();
				String[] v = getParameterValues(h);
				if(v==null || v.length==0) {
					outputRequest.addParameter(h, null);
				} else {
					for(int i=0; i<v.length; i++) {
						outputRequest.addParameter(h, v[i]);
					}
				}
			}
		}
	}
	
	protected static class ResponseWrapper extends HttpServletResponseWrapper {
		private DumpResponse outputResponse;
		public ResponseWrapper(HttpServletResponse response) {
			super(response);
			this.outputResponse = new DumpResponse();
		}
		public DumpResponse getServiceResponse() {
			return outputResponse;
		}
		
		@Override
		public void addDateHeader(String name, long date) {
			super.addDateHeader(name, date);
			outputResponse.addHeader(name,(new Date(date)).toString());
		}
		@Override
		public void addHeader(String name, String value) {
			super.addHeader(name, value);
			outputResponse.addHeader(name,value);
		}
		@Override
		public void addIntHeader(String name, int value) {
			super.addIntHeader(name, value);
			outputResponse.addHeader(name,Integer.toString(value));
		}
		@Override
		public void setDateHeader(String name, long date) {
			super.setDateHeader(name, date);
			outputResponse.addHeader(name,(new Date(date)).toString());
		}
		@Override
		public void setHeader(String name, String value) {
			super.setHeader(name, value);
			outputResponse.addHeader(name,value);
		}
		@Override
		public void setIntHeader(String name, int value) {
			super.setIntHeader(name, value);
			outputResponse.addHeader(name,Integer.toString(value));
		}
		@Override
		public void setStatus(int sc, String sm) {
			super.setStatus(sc, sm);
			outputResponse.setStatus(sc);
		}
		@Override
		public void setStatus(int sc) {
			super.setStatus(sc);
			outputResponse.setStatus(sc);
		}
		@Override
		public void addCookie(Cookie cookie) {
			super.addCookie(cookie);
			outputResponse.addCookie(cookie.getName(),cookie.toString());
		}
	}
	
	public static class NameValue {
		private String name;
		private String value;
		public NameValue(String name, String value) {
			this.name = name;
			this.value = value;
		}
		public String getName() {
			return name;
		}
		public String getValue() {
			return value;
		}
	}

	public static class DumpRequest {
		private String url;
		private String method;
		private List<NameValue> headers = new ArrayList<NameValue>();  
		private List<NameValue> cookies = new ArrayList<NameValue>();
		private List<NameValue> parameters = new ArrayList<NameValue>();
		public DumpRequest() {
		}
		public String getMethod() {
			return method;
		}
		public void setMethod(String method) {
			this.method = method;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public List<NameValue> getHeaders() {
			return headers;
		}
		public void addHeader(String name, String value) {
			headers.add(new NameValue(name, value));
		}
		public List<NameValue> getCookie() {
			return cookies;
		}
		public void addCookie(String name, String value) {
			cookies.add(new NameValue(name, value));
		}
		public List<NameValue> getParameters() {
			return parameters;
		}
		public void addParameter(String name, String value) {
			parameters.add(new NameValue(name, value));
		}
	}

	public static class DumpResponse {
		private int status;
		private List<NameValue> headers = new ArrayList<NameValue>();  
		private List<NameValue> cookies = new ArrayList<NameValue>();
		public DumpResponse() {
		}
		public int getStatus() {
			return status;
		}
		public void setStatus(int status) {
			this.status = status;
		}
		public List<NameValue> getHeaders() {
			return headers;
		}
		public void addHeader(String name, String value) {
			headers.add(new NameValue(name, value));
		}
		public List<NameValue> getCookie() {
			return cookies;
		}
		public void addCookie(String name, String value) {
			cookies.add(new NameValue(name, value));
		}
	}


	private Type type;
	private long startTS;
	private long endTS;
	private DebugOutput debugOutput;
	private RequestWrapper request;
	private ResponseWrapper response;
	
	public DebugServiceHook(DebugOutput debugOutput, Type type, HttpServletRequest request, HttpServletResponse response) {
		this.debugOutput = debugOutput;
		this.type = type;
		this.request = new RequestWrapper(request);
		this.response = new ResponseWrapper(response);
		debugOutput.add(this);
		this.startTS = System.currentTimeMillis();
	}
	
	public RequestWrapper getRequestWrapper() {
		return request;
	}
	
	public ResponseWrapper getResponseWrapper() {
		return response;
	}
	
	public Type getType() {
		return type;
	}
	
	public long getStartTS() {
		return startTS;
	}
	
	public long getEndTS() {
		return endTS;
	}
	
	public void terminate() {
		this.endTS = System.currentTimeMillis();
		debugOutput.terminate(this);
	}
}
