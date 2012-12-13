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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.AbstractRuntimeFactory;



/**
 * Runtime factory implementation for a servlet environment.
 * 
 * @author Philippe Riand
 */
public class RuntimeFactoryServlet extends AbstractRuntimeFactory {
	
	@Override
	public Application createApplication(Object context) {
		return new ApplicationServlet((ServletContext)context);
	}

	@Override
	public Context createContext(Application application, Object request, Object response) {
		return new ContextServlet(application,(HttpServletRequest)request, (HttpServletResponse)response);
	}
}
