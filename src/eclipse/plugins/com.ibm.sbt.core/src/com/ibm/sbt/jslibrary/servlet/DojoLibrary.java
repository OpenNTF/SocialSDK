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

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.js.JSReference;

/**
 * Dojo specific library implementation for versions that do not support AMD
 * 
 * @author mwallace
 */
public class DojoLibrary extends AbstractLibrary {

	private String					minimumAmdVersion;

	public static final String		NAME					= "dojo";					//$NON-NLS-1$

	public static final String		MODULE_BRIDGE			= "sbt._bridge";
	public static final String		MODULE_DOJO				= "sbt.dojo";				//$NON-NLS-1$
	public static final String		MODULE_SBTX				= "sbtx";					//$NON-NLS-1$
	public static final String		MODULE_AMDCOMPAT		= "sbt._bridge.amdcompat";	//$NON-NLS-1$
	public static final String		MODULE_BRIDGE_AMD		= "sbt/_bridge";
	public static final String		MODULE_DOJO_AMD			= "sbt/dojo";				//$NON-NLS-1$

	public static final String		PATH_BRIDGE				= "_bridges/dojo";			//$NON-NLS-1$
	public static final String		PATH_DOJO				= "dojo";					//$NON-NLS-1$
	public static final String		PATH_BRIDGE_AMD			= "_bridges/dojo-amd";

	static private final String[][]	REGISTER_MODULES		= { { MODULE_SBT, PATH_SBT },
			{ MODULE_BRIDGE, PATH_BRIDGE }, { MODULE_DOJO, PATH_DOJO } };

	static private final String[]	REQUIRE_MODULES			= { MODULE_AMDCOMPAT };

	static private final String[][]	REGISTER_MODULES_AMD	= { { MODULE_SBT, PATH_SBT },
			{ MODULE_BRIDGE_AMD, PATH_BRIDGE_AMD }, { MODULE_DOJO_AMD, PATH_DOJO } };

	static private final String[]	REQUIRE_MODULES_AMD		= new String[0];

	static private final String		DEFINE_MODULE			= MODULE_CONFIG;

	/**
	 * Default constructor
	 */
	public DojoLibrary() {
		// TODO remove hardcoded strings
		super(NAME, "1.4", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		minimumAmdVersion = normalizeVersion("1.7.2");
	}

	/**
	 * @param libraryName
	 * @param minimumVersion
	 * @param maximumVersion
	 * @throws ClassNotFoundException
	 */
	public DojoLibrary(String libraryName, String minimumVersion, String maximumVersion) {
		super(libraryName, minimumVersion, maximumVersion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#enableDefineCheck(java. lang.String)
	 */
	@Override
	public boolean enableDefineCheck(String version) {
		if (!StringUtil.isEmpty(version)) {
			return !isExceedsVersion(version, minimumAmdVersion);
		}
		return true;
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
		return REGISTER_MODULES_AMD;
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
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRequireModulesAmd()
	 */
	@Override
	protected String[] getRequireModulesAmd() {
		return REQUIRE_MODULES_AMD;
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRegisterModulePath (java.lang.String, java.lang.String)
	 */
	@Override
	protected String generateRegisterModulePath(String moduleName, String moduleUrl) {
		StringBuilder sb = new StringBuilder();
		sb.append("dojo.registerModulePath('").append(moduleName).append("','").append(moduleUrl)
				.append("')").append(";\n");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRequire(java.lang .String)
	 */
	@Override
	protected String generateRequire(String module) {
		StringBuilder sb = new StringBuilder();
		sb.append("dojo.require('").append(module).append("')").append(";\n");
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getTransport(LibraryRequest, Endpoint, java.lang.String)
	 */
	@Override
	protected JSReference getTransport(LibraryRequest request, Endpoint endpoint, String endpointName) {
		// Dojo versions from 1.8 and above should use dojo/Request
		if (request.getJsVersion().startsWith("1.8")) {
			return endpoint.getTransport(endpointName, MODULE_REQUESTTRANSPORT);
		}
		return endpoint.getTransport(endpointName, MODULE_TRANSPORT);
	}
}
