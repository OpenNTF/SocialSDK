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
import com.ibm.commons.util.StringUtil;
import com.ibm.designer.runtime.Application;
import com.ibm.xsp.extlib.servlet.ServletFactory;

/**
 * Service factory for the toolkit servlet.
 * 
 * The actual service to create can be parameterized using a property, defined in the database.
 */
public class ToolkitServletFactory extends ServletFactory {

	private static String findServletClassName() {
		Application app = Application.getRuntimeApplicationObject();
		if(app!=null) {
			String s = app.getProperty("sbt.servlet.toolkit");
			if(StringUtil.isNotEmpty(s)) {
				return s;
			}
		}
		return ToolkitServlet.class.getName();
	}
	
	public ToolkitServletFactory() {
		super(RuntimeConstants.AbstractNotesDominoConstants.LIBRARY_PATHINFO,findServletClassName(),"SBT Library Servlet");
	}
 }