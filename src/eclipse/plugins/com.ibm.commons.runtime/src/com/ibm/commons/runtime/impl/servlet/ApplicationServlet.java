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

import java.io.InputStream;

import javax.servlet.ServletContext;

import com.ibm.commons.runtime.impl.AbstractApplication;



/**
 * SBT Application implementation for a servlet environment.
 *
 * This class encapsulates an application (J2EE application).
 *  
 * @author Philippe Riand
 */
public class ApplicationServlet extends AbstractApplication {

	protected ApplicationServlet(ServletContext servletContext) {
		super(servletContext);
		// For J2EE apps, the application name is coming from the context path
		String cp = servletContext.getContextPath();
		if(cp!=null) {
			setName(cp);
		}
	}

	@Override
	public ServletContext getApplicationContext() {
		return (ServletContext)super.getApplicationContext();
	}
	
	@Override
	public InputStream getResourceAsStream(String name) {
		return getApplicationContext().getResourceAsStream(name);
	}

}
