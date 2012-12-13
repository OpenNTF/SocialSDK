/*
 * © Copyright IBM Corp. 2010
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

package com.ibm.xsp.sbtsdk.servlets;

import com.ibm.commons.runtime.RuntimeConstants;
import com.ibm.xsp.extlib.servlet.ServletFactory;

/**
 * Service factory for the proxy servlet.
 */
public class ProxyServletFactory extends ServletFactory {

	public ProxyServletFactory() {
		super(RuntimeConstants.AbstractNotesDominoConstants.SERVICE_PATHINFO,DominoServiceServlet.class.getName(),"SBT Proxy Servlet");
	}
 }