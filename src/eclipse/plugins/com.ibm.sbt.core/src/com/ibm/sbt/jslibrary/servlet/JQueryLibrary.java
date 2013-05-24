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
 * @author cmanias
 */
public class JQueryLibrary extends RequireJSLibrary {

	private static final String		NAME				= "jquery";			//$NON-NLS-1$

	private static final String		PATH_BRIDGE			= "_bridges/jquery";	//$NON-NLS-1$

	// TODO Do these need to be dynamic
	private static final String[][]	REGISTER_MODULES	= { { MODULE_SBT, PATH_SBT },
			{ MODULE_BRIDGE, PATH_BRIDGE }				};

	/**
	 * Default constructor
	 */
	public JQueryLibrary() {
		this(NAME); 
	}
	public JQueryLibrary(String name) {
		super(NAME);
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
}
