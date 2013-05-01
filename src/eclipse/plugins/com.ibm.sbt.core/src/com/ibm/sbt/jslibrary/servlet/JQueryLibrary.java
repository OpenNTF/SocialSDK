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
package com.ibm.sbt.jslibrary.servlet;

/**
 * jQuery specific library implementation
 * 
 * @author mwallace
 */
public class JQueryLibrary extends RequireJSLibrary {

	public static final String		NAME				= "jquery";			//$NON-NLS-1$

	public static final String		PATH_BRIDGE			= "_bridges/jquery";	//$NON-NLS-1$

	// TODO Do these need to be dynamic
	private static final String[][]	REGISTER_MODULES	= { { MODULE_SBT, PATH_SBT },
			{ MODULE_BRIDGE, PATH_BRIDGE }				};

	/**
	 * Default constructor
	 */
	public JQueryLibrary() {
		super(NAME); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getName()
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getRegisterModules()
	 */
	@Override
	protected String[][] getRegisterModules() {
		return REGISTER_MODULES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRegisterModulesAmd()
	 */
	@Override
	protected String[][] getRegisterModulesAmd() {
		return REGISTER_MODULES;
	}

	/*
	 * We redefine the isExtension boolean parameter. The meaning here is that there are extension modules pending to be added (so we need to append a comma) (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRegisterModules(java.lang.StringBuilder, int, com.ibm.sbt.jslibrary.servlet.LibraryRequest, java.lang.String[][], boolean)
	 */
	/*
	 * @Override protected void generateRegisterModules(StringBuilder sb, int indentationLevel, LibraryRequest request, String[][] registerModules, boolean isExtension) { if (registerModules == null) { return; } int numModules = registerModules.length; for (int i = 0; i < numModules; i++) { String[]
	 * registerModule = registerModules[i];}
	 */
	/*
	 * indent(sb, indentationLevel).append("'").append(registerModule[0]).append("' : '") .append(registerModule[1]).append("'"); if (i < numModules - 1 || isExtension) { sb.append(","); } sb.append("\n");
	 */
	/*
	 * if (i == 0) { if (isExtension) { // delimit from standard module paths sb.append(","); } } String moduleUrl = getModuleUrl(request, registerModule[1], isExtension); sb.append("'").append(registerModule[0]).append("':'").append(moduleUrl).append("'"); if (i < numModules - 1) { sb.append(",");
	 * } } }
	 */

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateModuleBlock(com.ibm.sbt.jslibrary.servlet.LibraryRequest, java.lang.String[][], java.lang.String[], int)
	 */
	/*
	 * @Override protected StringBuilder generateModuleBlock(LibraryRequest request, String[][] registerModules, String[][] registerExtModules, String[] requireModules, int indentationLevel) { StringBuilder sb = new StringBuilder(); indent(sb, indentationLevel).append("requirejs.config({\n"); //
	 * begin requirejs.config indentationLevel += 1; Boolean enforceDefine = true; int waitSeconds = 0; indent(sb, indentationLevel).append("enforceDefine: '").append(enforceDefine).append("',\n"); indent(sb, indentationLevel).append("waitSeconds: '").append(waitSeconds).append("',\n"); //
	 * indent(sb, indentationLevel).append("baseUrl: '").append(PATH_BASEURL).append("',\n"); indent(sb, indentationLevel).append("paths: {"); // begin paths // register the module paths and required modules generateRegisterModules(sb, indentationLevel, request, registerModules, false); if
	 * (registerExtModules != null) { generateRegisterModules(sb, indentationLevel, request, registerExtModules, true); } generateRequireModules(sb, indentationLevel, requireModules); sb.append("}\n"); // end paths indentationLevel -= 1; indent(sb, indentationLevel).append("});\n"); // end
	 * requirejs.config return sb; }
	 */
}
