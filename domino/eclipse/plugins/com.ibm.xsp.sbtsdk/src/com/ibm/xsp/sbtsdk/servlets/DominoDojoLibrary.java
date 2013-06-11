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

import com.ibm.commons.util.PathUtil;
import com.ibm.sbt.jslibrary.servlet.DojoLibrary;
import com.ibm.sbt.jslibrary.servlet.LibraryRequest;

/**
 * Dojo domino specific library.
 *
 * @author priand
 */
public class DominoDojoLibrary extends DojoLibrary {
	
	public static final String	NAME	= "dojo";					//$NON-NLS-1$
	
	public DominoDojoLibrary() {
		// Use the XPages version # here
		super(NAME,"1.4","1.9.9");
	}

	@Override
	protected String[][] getRegisterExtModules(LibraryRequest request) {
		String[][] s = super.getRegisterExtModules(request);
		return addExtraModules(s);
	}
	
	@Override
	protected String[][] getRegisterExtModulesAmd(LibraryRequest request) {
		String[][] s = super.getRegisterExtModulesAmd(request);
		return addExtraModules(s);
	}
	
	protected String[][] addExtraModules(String[][] s) {
		if(true) {
			String[][] ns = new String[s.length+1][];
			System.arraycopy(s, 0, ns, 0, s.length);
			return new String[][] {
				{"com/ibm/watson", "/.watson/com/ibm/watson"}
			};
		}
		return s;
	}
	
	protected String getModuleUrl(LibraryRequest request, String modulePath, ModuleType type) {
		if(type==ModuleType.SBTX_MODULE && modulePath.startsWith("/.watson/")) {
			String jsUrl = "https://priand64.swg.usma.ibm.com/xsp/.ibmxspres/";
			return PathUtil.concat(jsUrl, modulePath, '/');
		}
		return super.getModuleUrl(request, modulePath, type);
	}
	
}
