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
package com.ibm.xsp.sbtsdk.servlets;

import com.ibm.sbt.jslibrary.servlet.JQueryLibrary;

/**
 * JQuery domino specific library.
 *
 * @author priand
 */
public class DominoJQueryLibrary extends JQueryLibrary {
	
	public static final String	NAME	= "jquery";					//$NON-NLS-1$
	
	public DominoJQueryLibrary() {
		// Use the XPages version # here
		super(NAME);
	}

//	protected String getModuleUrl(LibraryRequest request, String modulePath, MODULE_TYPE type) {
//		String moduleUrl = super.getModuleUrl(request, modulePath, type == MODULE_TYPE.EXTENSION);
//		return moduleUrl;
//	}
	
//	protected String getModuleUrl(LibraryRequest request, String modulePath, MODULE_TYPE type) {
//		String moduleUrl = "";
//		switch (type) {
//			case MODULE:
//			case EXTENSION:
//				moduleUrl = super.getModuleUrl(request, modulePath, type == MODULE_TYPE.EXTENSION);
//				break;
//			case LIBRARY:
//			default:
//				moduleUrl = modulePath;
//				break;
//		}
//		return moduleUrl;
//	}
	
}
