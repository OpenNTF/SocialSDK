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
package com.ibm.sbt.service.core.servlet;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.commons.runtime.servlet.ServletDispatcher;
import com.ibm.commons.runtime.servlet.ServletFactory;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OA2Callback;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OACallback;
import com.ibm.sbt.security.authentication.oauth.consumer.servlet.OAClientAuthentication;
import com.ibm.sbt.service.core.handlers.AuthenticationHandler;
import com.ibm.sbt.service.core.handlers.BasicAuthCredsHandler;
import com.ibm.sbt.service.core.handlers.EmailHandler;
import com.ibm.sbt.service.core.handlers.FileHandler;
import com.ibm.sbt.service.core.handlers.PingHandler;
import com.ibm.sbt.service.core.handlers.ProxyHandler;

/**
 * Default service servlet used by SBT runtime.
 * 
 * @author priand
 */
public class ServiceServlet extends ServletDispatcher {

	public static String getServletPath() {
		return RuntimeConstants.get().getConstant(RuntimeConstants.SERVICE_BASEURL);
	}

	private static final long	serialVersionUID	= 1L;

	public ServiceServlet() {
		// Register the default servlets
		register(new ServletFactory.PathInfoFactory(PingHandler.class, PingHandler.URL_PATH));
		register(new ServletFactory.PathInfoFactory(ProxyHandler.class, ProxyHandler.URL_PATH));
		register(new ServletFactory.PathInfoFactory(BasicAuthCredsHandler.class,
				BasicAuthCredsHandler.URL_PATH));
		register(new ServletFactory.PathInfoFactory(OACallback.class, OACallback.URL_PATH));
		register(new ServletFactory.PathInfoFactory(OAClientAuthentication.class,
				OAClientAuthentication.URL_PATH));
		register(new ServletFactory.PathInfoFactory(OA2Callback.class, OA2Callback.URL_PATH));
		register(new ServletFactory.PathInfoFactory(EmailHandler.class, EmailHandler.URL_PATH));
		register(new ServletFactory.PathInfoFactory(FileHandler.class, FileHandler.URL_PATH));
		register(new ServletFactory.PathInfoFactory(AuthenticationHandler.class,
				AuthenticationHandler.URL_PATH));
	}

}