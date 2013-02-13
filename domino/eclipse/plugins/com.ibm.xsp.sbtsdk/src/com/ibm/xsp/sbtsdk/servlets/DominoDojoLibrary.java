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

import com.ibm.sbt.jslibrary.servlet.DojoLibrary;

/**
 * Dojo domino specific library.
 *
 * @authro priand
 */
public class DominoDojoLibrary extends DojoLibrary {
	
	static private final String[][]	REGISTER_MODULES_AMD	= {
			{ MODULE_SBT, PATH_SBT }
	};
	
	public static final String	NAME	= "dojod";					//$NON-NLS-1$
	public static final String	VERSION	= "1.7";					//$NON-NLS-1$
	
	public DominoDojoLibrary() {
		// Use the XPages version # here
		super(NAME,"1.6","1.8");
	}

	protected String[][] getRegisterModules() {
		return REGISTER_MODULES_AMD;
	}	
}
