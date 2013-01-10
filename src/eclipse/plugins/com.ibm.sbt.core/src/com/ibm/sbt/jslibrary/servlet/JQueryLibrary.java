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
public class JQueryLibrary extends AbstractLibrary {

    public static final String NAME = "jquery"; //$NON-NLS-1$

    public static final String MODULE_BRIDGE = "sbt._bridge";
    public static final String MODULE_JQUERY = "sbt.jquery"; //$NON-NLS-1$

    public static final String PATH_BRIDGE = "_bridges/jquery"; //$NON-NLS-1$
    public static final String PATH_JQUERY = "jquery"; //$NON-NLS-1$

    // TODO Do these need to be dynamic
    static private final String[][] REGISTER_MODULES = { { MODULE_SBT, PATH_SBT }, { MODULE_BRIDGE, PATH_BRIDGE } };

    static private final String[] REQUIRE_MODULES = new String[0];

    static private final String DEFINE_MODULE = MODULE_CONFIG;

    /**
     * Default constructor
     */
    public JQueryLibrary() {
        // TODO remove hardcoded strings
        super(NAME, "", ""); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.jslibrary.servlet.AbstractLibrary#enableDefineCheck(java.
     * lang.String)
     */
    @Override
    public boolean enableDefineCheck(String version) {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getName()
     */
    @Override
    public String getName() {
        return NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getRegisterModules()
     */
    @Override
    protected String[][] getRegisterModules() {
        return REGISTER_MODULES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRegisterModulesAmd()
     */
    @Override
    protected String[][] getRegisterModulesAmd() {
        return REGISTER_MODULES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getDefineModule()
     */
    @Override
    protected String getDefineModule() {
        return DEFINE_MODULE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.jslibrary.servlet.BaseLibrary#getRequireModules()
     */
    @Override
    protected String[] getRequireModules() {
        return REQUIRE_MODULES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.ibm.sbt.jslibrary.servlet.AbstractLibrary#getRequireModulesAmd()
     */
    @Override
    protected String[] getRequireModulesAmd() {
        return REQUIRE_MODULES;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRegisterModulePath
     * (java.lang.String, java.lang.String)
     */
    @Override
    protected String generateRegisterModulePath(String moduleName, String moduleUrl) {
        StringBuilder sb = new StringBuilder();
        sb.append("{'").append(moduleName).append("':'").append(moduleUrl).append("'}");
        String paths = sb.toString();
        sb = new StringBuilder();
        sb.append("require({").append(paths).append(":").append(paths).append("})");
        return sb.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.jslibrary.servlet.AbstractLibrary#generateRequire(java.lang
     * .String)
     */
    @Override
    protected String generateRequire(String module) {
        StringBuilder sb = new StringBuilder();
        sb.append("require('").append(module).append("')");
        return sb.toString();
    }

}
