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

package com.ibm.xsp.sbtsdk.runtime;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.servlet.ContextServlet;
import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.context.FacesContextEx;
import com.ibm.xsp.context.RequestParameters;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.util.ManagedBeanUtil;


/**
 * SBT Context.
 *
 * This class encapsulate a context that can be reused by all the helper classes.
 *  
 * @author Philippe Riand
 */
public class XspContext extends ContextServlet {
	
	private FacesContextEx facesContext;
	private boolean deletefacesContext;
	
	protected XspContext(Application application, FacesContextEx facesContext, Object request, Object response, boolean deletefacesContext) {
		super(application, (HttpServletRequest)request,(HttpServletResponse)response);
		this.facesContext = facesContext;
		this.deletefacesContext = deletefacesContext;
	}
	
	public void close() {
		if(deletefacesContext) {
			facesContext.release();
		}
		super.close();
	}

	public FacesContextEx getFacesContext() {
		return facesContext;
	}
	
	public String getProperty(String propertyName) {
		return getProperty(propertyName,null);
	}

	public String getProperty(String propertyName, String defaultValue) {
		String s = getFacesContext().getProperty(propertyName);
		return s!=null ? s : defaultValue;
	}

	@Override
	public void setProperty(String propertyName, String value) {
        RequestParameters p = getFacesContext().getRequestParameters();
        p.setProperty(propertyName, value);
	}
	
	public Object getBean(String beanName) {
		return ManagedBeanUtil.getBean(getFacesContext(), beanName);
	}

	public void sendRedirect(String redirectUrl) throws IOException {
		getFacesContext().getExternalContext().redirect(redirectUrl);
	}
	
	public String encodeUrl(String url) {
		return getFacesContext().getExternalContext().encodeActionURL(url); 
	}

	@Override
	public String getCurrentUserId() {
		String u = getFacesContext().getExternalContext().getRemoteUser();
		if(StringUtil.isEmpty(u) || StringUtil.endsWithIgnoreCase(u, Context.ANONYMOUS)) {
			return ANONYMOUS;
		}
		return u;
	}

	@Override
	protected Map<String, Object> createSessionMap() {
		return ExtLibUtil.getSessionScope();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> createRequestMap() {
		return getFacesContext().getExternalContext().getRequestMap();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> createRequestParameterMap() {
		return getFacesContext().getExternalContext().getRequestParameterMap();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Map<String, Object> createRequestCookieMap() {
		return getFacesContext().getExternalContext().getRequestCookieMap();
	}
}
