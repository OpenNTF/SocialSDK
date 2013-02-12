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

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.ibm.commons.runtime.impl.servlet.ApplicationServlet;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;



/**
 * SBT Application for a Domino context, including XPages.
 *
 * @author Philippe Riand
 */
public class XspApplication extends ApplicationServlet {
	
	private ApplicationEx jsfApplication;
	
	public XspApplication(ApplicationEx app, ServletContext context) {
		super(context);
		this.jsfApplication = app;
	}

	public ClassLoader getClassLoader() {
		return Thread.currentThread().getContextClassLoader();
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> getScope() {
		return ExtLibUtil.getApplicationScope();
	}
		
	public String getProperty(String name) {
		return jsfApplication.getProperty(name, null);
	}
	
	public void setProperty(String name, String value) {
		throw new IllegalStateException("Cannot set an application wide property for XPages");
	}
	
	public InputStream getResourceAsStream(String name) {
		return FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(name);
	}
	
	@SuppressWarnings("unchecked")
	public List<Object> findServices(String serviceName) {
		return jsfApplication.findServices(serviceName);
	}	
	
}
