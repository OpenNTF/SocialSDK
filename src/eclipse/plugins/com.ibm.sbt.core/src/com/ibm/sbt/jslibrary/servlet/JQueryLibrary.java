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

import java.util.Map;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonObject;

/**
 * jQuery specific library implementation
 * 
 * @author mwallace
 */
public class JQueryLibrary extends AbstractLibrary {

	public static final String		NAME					= "jquery";										//$NON-NLS-1$

	public static final String		MODULE_BRIDGE			= "sbt/_bridge";
	public static final String		MODULE_JQUERY			= "jquery";										//$NON-NLS-1$
	public static final String		MODULE_HAS				= "has";											//$NON-NLS-1$
	public static final String		MODULE_SBTX				= "sbtx";											//$NON-NLS-1$
	public static final String		MODULE_JQUERY_UI		= "jquery/ui";										//$NON-NLS-1$
	public static final String		PLUGIN_I18N				= "requirejs/i18n";								//$NON-NLS-1$
	public static final String		PLUGIN_TEXT				= "requirejs/text";								//$NON-NLS-1$
	public static final String		PATH_HAS				= "/sbt/js/libs/has";								//$NON-NLS-1$
	public static final String		PATH_BRIDGE				= "_bridges/jquery";								//$NON-NLS-1$
	public static final String		PATH_JQUERY				= "/sbt.jquery182/js/jquery-1.8.0.min";			//$NON-NLS-1$
	public static final String		PATH_JQUERY_UI			= "/sbt.jquery182/js/jquery-ui-1.8.23.custom.min";	//$NON-NLS-1$
	public static final String		PATH_SBTX				= "/sbtx/js/sdk/sbtx";
	public static final String		PATH_BASEURL			= "/sbt/js/sdk";
	public static final String		PATH_I18N				= "/sbt/js/libs/requirejsPlugins/i18n";
	public static final String		PATH_TEXT				= "/sbt/js/libs/requirejsPlugins/text";

	// TODO Do these need to be dynamic
	private static final String[][]	REGISTER_MODULES		= { { MODULE_BRIDGE, PATH_BRIDGE } };
	/*
	 * , { MODULE_HAS, PATH_HAS }, { MODULE_JQUERY, PATH_JQUERY }, { MODULE_JQUERY_UI, PATH_JQUERY_UI }, { PLUGIN_I18N, PATH_I18N }, { PLUGIN_TEXT, PATH_TEXT } }
	 */

	private static final String[][]	REGISTER_EXT_MODULES	= { { MODULE_SBTX, PATH_SBTX } };
	private static final String[]	REQUIRE_MODULES			= new String[0];

	private static final String		DEFINE_MODULE			= MODULE_CONFIG;

	/**
	 * Default constructor
	 */
	public JQueryLibrary() {
		// TODO remove hardcoded strings
		super(NAME, "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#enableDefineCheck(java. lang.String)
	 */
	@Override
	public boolean enableDefineCheck(String version) {
		return false;
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
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getDefineModule()
	 */
	@Override
	protected String getDefineModule() {
		return DEFINE_MODULE;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getRequireModules()
	 */
	@Override
	protected String[] getRequireModules() {
		return REQUIRE_MODULES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRegisterExtModules(LibraryRequest)
	 */
	@Override
	protected String[][] getRegisterExtModules(LibraryRequest request) {
		return REGISTER_EXT_MODULES;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRequireModulesAmd()
	 */
	@Override
	protected String[] getRequireModulesAmd() {
		return REQUIRE_MODULES;
	}

	protected String generateRequireJSConfig(int indentationLevel) {
		StringBuilder sb = new StringBuilder();
		sb.append("requirejs.config({\n");
		indentationLevel += 1;
		indent(sb, indentationLevel).append("baseUrl: '/sbt/js/sdk',\n");
		indent(sb, indentationLevel).append("paths: {\n");
		indentationLevel += 1;
		indent(sb, indentationLevel).append("'sbt': 'sbt',\n");
		indent(sb, indentationLevel).append("'sbt._bridge': '_bridges/jquery',\n");
		indent(sb, indentationLevel).append("'sbtx': '/sbtx/js/sdk/stbx'\n");
		indentationLevel -= 1;
		indent(sb, indentationLevel).append("}\n");
		indentationLevel -= 1;
		indent(sb, indentationLevel).append("});\n");
		return sb.toString();
	}

	/*
	 * We redefine the isExtension boolean parameter. The meaning here is that there are extension modules pending to be added (so we need to append a comma) (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRegisterModules(java.lang.StringBuilder, int, com.ibm.sbt.jslibrary.servlet.LibraryRequest, java.lang.String[][], boolean)
	 */
	@Override
	protected void generateRegisterModules(StringBuilder sb, int indentationLevel, LibraryRequest request,
			String[][] registerModules, boolean isExtension) {

		if (registerModules == null) {
			return;
		}
		int numModules = registerModules.length;
		for (int i = 0; i < numModules; i++) {
			String[] registerModule = registerModules[i];
			indent(sb, indentationLevel).append("'").append(registerModule[0]).append("' : '")
					.append(registerModule[1]).append("'");
			if (i < numModules - 1 || isExtension) {
				sb.append(",");
			}
			sb.append("\n");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateSbtConfigDefine(com.ibm.sbt.jslibrary.servlet.LibraryRequest, java.util.Map, com.ibm.commons.util.io.json.JsonObject, int)
	 */
	@Override
	protected StringBuilder generateSbtConfigDefine(LibraryRequest request,
			Map<String, JsonObject> endpoints, JsonObject properties, int indentationLevel)
			throws LibraryException {
		StringBuilder sb = super.generateSbtConfigDefine(request, endpoints, properties, indentationLevel);

		return sb;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRegisterModulePath (java.lang.String, java.lang.String)
	 */
	@Override
	protected String generateRegisterModulePath(String moduleName, String moduleUrl) {
		return "";
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRequire(java.lang .String)
	 */
	@Override
	protected String generateRequire(String module) {
		StringBuilder sb = new StringBuilder();
		sb.append("require('").append(module).append("')");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateModuleBlock(com.ibm.sbt.jslibrary.servlet.LibraryRequest, java.lang.String[][], java.lang.String[], int)
	 */
	@Override
	protected StringBuilder generateModuleBlock(LibraryRequest request, String[][] registerModules,
			String[][] registerExtModules, String[] requireModules, int indentationLevel) {
		StringBuilder sb = new StringBuilder();
		indent(sb, indentationLevel).append("requirejs.config({\n"); // begin requirejs.config
		indentationLevel += 1;
		Boolean enforceDefine = true;
		int waitSeconds = 0;
		indent(sb, indentationLevel).append("enforceDefine: '").append(enforceDefine).append("',\n");
		indent(sb, indentationLevel).append("waitSeconds: '").append(waitSeconds).append("',\n");
		indent(sb, indentationLevel).append("baseUrl: '").append(PATH_BASEURL).append("',\n");
		indent(sb, indentationLevel).append("paths: {\n"); // begin paths
		indentationLevel += 1;

		// register the module paths and required modules
		boolean hasExtension = StringUtil.isNotEmpty(request.getToolkitExtUrl());
		generateRegisterModules(sb, indentationLevel, request, registerModules, hasExtension);
		if (registerExtModules != null) {
			generateRegisterModules(sb, indentationLevel, request, registerExtModules, false);
		}

		generateRequireModules(sb, indentationLevel, requireModules);
		indentationLevel -= 1;
		indent(sb, indentationLevel).append("}\n"); // end paths

		// indent(sb, indentationLevel).append("shim: {\n"); // begin shim
		// indentationLevel += 1;
		//
		// indent(sb, indentationLevel).append("'jquery/ui': {\n");
		// indentationLevel += 1;
		// indent(sb, indentationLevel).append("deps: ['jquery'],\n"); // begin jquery/ui
		// indent(sb, indentationLevel).append("exports: '$'\n");
		// indentationLevel -= 1;
		// indent(sb, indentationLevel).append("},\n"); // end jquery/ui
		//
		// indentationLevel -= 1;
		// indent(sb, indentationLevel).append("}\n"); // end shim

		indentationLevel -= 1;
		indent(sb, indentationLevel).append("});\n"); // end requirejs.config

		return sb;
	}
}
