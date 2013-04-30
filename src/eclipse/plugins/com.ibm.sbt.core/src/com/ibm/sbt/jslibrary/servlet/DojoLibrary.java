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

    private String minimumAmdVersion;
    private String minimumDojo2Version;

	public static final String		NAME					= "dojo";					//$NON-NLS-1$

    public static final String      MODULE_BRIDGE           = "sbt._bridge";            //$NON-NLS-1$
    public static final String      MODULE_SBT_WIDGET       = "sbt.widget";             //$NON-NLS-1$
	public static final String		MODULE_SBTX				= "sbtx";					//$NON-NLS-1$
    public static final String      MODULE_SBTX_WIDGET      = "sbtx.widget";            //$NON-NLS-1$
	public static final String		MODULE_AMDCOMPAT		= "sbt._bridge.amdcompat";	//$NON-NLS-1$
    public static final String      MODULE_BRIDGE_AMD       = "sbt/_bridge";            //$NON-NLS-1$
    public static final String      MODULE_SBT_WIDGET_AMD   = "sbt/widget";             //$NON-NLS-1$
    public static final String      MODULE_SBTX_WIDGET_AMD  = "sbtx/widget";            //$NON-NLS-1$
    public static final String      MODULE_REQUESTTRANSPORT = "sbt/_bridge/RequestTransport"; //$NON-NLS-1$

	public static final String		PATH_BRIDGE				= "_bridges/dojo";			//$NON-NLS-1$
	public static final String		PATH_BRIDGE_AMD			= "_bridges/dojo-amd";      //$NON-NLS-1$
    public static final String      PATH_DOJO               = "dojo";                   //$NON-NLS-1$
    public static final String      PATH_DOJO2              = "dojo2";                  //$NON-NLS-1$
    public static final String      PATH_SBTX_DOJO          = "dojo";                   //$NON-NLS-1$
    public static final String      PATH_SBTX_DOJO2         = "dojo2";                  //$NON-NLS-1$

	static private final String[][]	REGISTER_MODULES		= { { MODULE_SBT, PATH_SBT },
			                                                    { MODULE_BRIDGE, PATH_BRIDGE }, 
			                                                    { MODULE_SBT_WIDGET, PATH_DOJO } };

	static private final String[]	REQUIRE_MODULES			= { MODULE_AMDCOMPAT };

	static private final String[][]	REGISTER_MODULES_AMD	= { { MODULE_SBT, PATH_SBT },
			                                                    { MODULE_BRIDGE_AMD, PATH_BRIDGE_AMD }, 
			                                                    { MODULE_SBT_WIDGET_AMD, PATH_DOJO2 } };

	static private final String[]	REQUIRE_MODULES_AMD		= new String[0];
	
	// extension modules
	
    private static final String[][] REGISTER_EXT_MODULES    = { { MODULE_SBTX, PATH_SBTX }, 
                                                                { MODULE_SBTX_WIDGET, PATH_SBTX_DOJO } };
    
    private static final String[][] REGISTER_EXT_MODULES_AMD= { { MODULE_SBTX, PATH_SBTX }, 
                                                                { MODULE_SBTX_WIDGET_AMD, PATH_DOJO2 } };

	static private final String		DEFINE_MODULE			= MODULE_CONFIG;

	/**
	 * Default constructor
	 */
	public DojoLibrary() {
		// TODO remove hardcoded strings
		super(NAME, "1.4", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		minimumAmdVersion = normalizeVersion("1.7.0");
		minimumDojo2Version = normalizeVersion("1.8.0");
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
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRegisterExtModules(LibraryRequest)
	 */
	@Override
	protected String[][] getRegisterExtModules(LibraryRequest request) {
	    return REGISTER_EXT_MODULES;
	}
	
	/* (non-Javadoc)
	 * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRegisterExtModulesAmd(LibraryRequest)
	 */
	@Override
	protected String[][] getRegisterExtModulesAmd(LibraryRequest request) {
	    if (isExceedsVersion(request.getJsVersion(), minimumDojo2Version)) {
	        REGISTER_EXT_MODULES_AMD[1][1] = PATH_SBTX_DOJO2;
	    } else {
            REGISTER_EXT_MODULES_AMD[1][1] = PATH_SBTX_DOJO;
	    }
	    
	    return REGISTER_EXT_MODULES_AMD;
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
	protected String generateRegisterModulePath(LibraryRequest request, String moduleName, String moduleUrl) {
	    if (isExceedsVersion(request.getJsVersion(), minimumAmdVersion)) {
			// Dojo AMD syntax
			StringBuilder sb = new StringBuilder();
			sb.append("require({paths:{'").append(moduleName).append("': '").append(moduleUrl).append("'}});\n");
			return sb.toString();
		} else {
			// Non AMD dojo syntax
			StringBuilder sb = new StringBuilder();
			sb.append("dojo.registerModulePath('").append(moduleName).append("','").append(moduleUrl)
					.append("')").append(";\n");
			return sb.toString();
		}
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
    	// Dojo2 versions from 1.8 and above should use RequestTransport
	    String transport = request.getHttpRequest().getParameter("transport");
	    if ("request".equals(transport) && isExceedsVersion(request.getJsVersion(), minimumDojo2Version)) {
        	return endpoint.getTransport(endpointName, MODULE_REQUESTTRANSPORT);
    	}
		return endpoint.getTransport(endpointName, MODULE_TRANSPORT);
	}
}
