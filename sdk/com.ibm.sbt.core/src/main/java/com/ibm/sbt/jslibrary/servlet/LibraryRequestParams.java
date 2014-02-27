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

import java.util.HashMap;
import java.util.Map;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.util.PathUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.jslibrary.SBTEnvironmentFactory;

/**
 * @author Mark Wallace
 * @date 16 Jan 2013
 */
public class LibraryRequestParams {

    private SBTEnvironment environment;
    private String toolkitUrl;
    private String toolkitJsUrl;
    private String serviceUrl;
    private String iframeUrl;
    private String toolkitExtUrl;
    private String toolkitExtJsUrl;
    private String libraryUrl;
    private String jsLibraryUrl;
    private String serverUrl;
    private String contextUrl;
    private String requestUrl;
    private String pathInfo;
    private Map<String, String> parameters = new HashMap<String, String>();

    /**
     * Servlet parameter which allows the location of the toolkit to be
     * overridden, the default value is %local_server%/sbt. %local_server% is
     * dynamically replaced by http://<server>:<port>
     */
    static public final String PARAM_TOOLKIT_URL = "toolkitUrl"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the location of the toolkit extensions to
     * be overridden, the default value is %local_server%/sbtx. %local_server%
     * is dynamically replaced by http://<server>:<port>
     */
    static public final String PARAM_TOOLKIT_EXT_URL = "toolkitExtUrl"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the location of the javascript libraries to
     * be overridden, the default value is %local_server%/sbt/js/libs. %local_server%
     * is dynamically replaced by http://<server>:<port>
     */
    static public final String PARAM_JS_LIBRARY_URL = "jsLibraryUrl"; //$NON-NLS-1$
    
    /**
     * Servlet parameter which allows the path to the JavaScript library to be
     * overridden, the default value is /js/sdk
     */
    static public final String PARAM_JAVASCRIPT_PATH = "javaScriptPath"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the location of the service servlet to be
     * overridden, the default value is %local_application%/service
     */
    static public final String PARAM_SERVICE_URL = "serviceUrl"; //$NON-NLS-1$
    
    /**
     * Servlet parameter which allows the location of the service servlet to be
     * overridden, the default value is %local_application%/library
     */
    static public final String PARAM_LIBRARY_URL = "libraryUrl"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the location of the IFrame content
     * template to be overridden, the default value is /xhr/IFrameContent.html
     */
    static public final String PARAM_IFRAME_PATH = "iframePath"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the list of default endpoints to be
     * overridden, the default value is connections,smartcloud,domino,sametime
     */
    static public final String PARAM_ENDPOINTS = "endpoints"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the list of default client properties to
     * be overridden, the default value is a null string
     */
    static public final String PARAM_CLIENT_PROPERTIES = "clientProperties"; //$NON-NLS-1$

    /**
     * Servlet parameter which allows the name of the default environment to be
     * overridden, the default value is defaultEnvironment
     */
    static public final String PARAM_ENVIRONMENT = "environment"; //$NON-NLS-1$
    
    //
    // Default values for library parameters
    //
    static public final String DEFAULT_TOOLKIT_URL = "%local_server%/sbt"; //$NON-NLS-1$
    static public final String DEFAULT_TOOLKIT_EXT_URL = null;
    static public final String DEFAULT_JS_LIBRARY_URL = "%local_server%/sbt/js/libs"; //$NON-NLS-1$
    static public final String DEFAULT_JAVASCRIPT_PATH = "/js/sdk"; //$NON-NLS-1$
    static public final String DEFAULT_SERVICE_URL = "%local_application%/service"; //$NON-NLS-1$
    static public final String DEFAULT_LIBRARY_URL = "%local_application%/library";
    static public final String DEFAULT_IFRAME_PATH = "/xhr/IFrameContent.html"; //$NON-NLS-1$
    static public final String DEFAULT_ENDPOINTS = "connections,smartcloud,domino,sametime"; //$NON-NLS-1$
    static public final String DEFAULT_CLIENT_PROPERTIES = null; //$NON-NLS-1$
    static public final String DEFAULT_ENVIRONMENT = "defaultEnvironment"; //$NON-NLS-1$
    
    /**
     * Initialize with the default values
     */
    public void initDefaults() {
        // default parameters
        Application application = Application.get();
        String defaultToolkitUrl = getAppParameter(application, LibraryRequestParams.PARAM_TOOLKIT_URL, LibraryRequestParams.DEFAULT_TOOLKIT_URL);
        String defaultToolkitExtUrl = getAppParameter(application, LibraryRequestParams.PARAM_TOOLKIT_EXT_URL, LibraryRequestParams.DEFAULT_TOOLKIT_EXT_URL);
        String defaultJsLibraryUrl = getAppParameter(application, LibraryRequestParams.PARAM_JS_LIBRARY_URL, LibraryRequestParams.DEFAULT_JS_LIBRARY_URL);
        String defaultJavaScriptPath = getAppParameter(application, LibraryRequestParams.PARAM_JAVASCRIPT_PATH, LibraryRequestParams.DEFAULT_JAVASCRIPT_PATH);
        String defaultServiceUrl = getAppParameter(application, LibraryRequestParams.PARAM_SERVICE_URL, LibraryRequestParams.DEFAULT_SERVICE_URL);
        String defaultLibraryUrl = getAppParameter(application, LibraryRequestParams.PARAM_LIBRARY_URL, LibraryRequestParams.DEFAULT_LIBRARY_URL);
        String defaultIFramePath = getAppParameter(application, LibraryRequestParams.PARAM_IFRAME_PATH, LibraryRequestParams.DEFAULT_IFRAME_PATH);

        // load initialisation parameters
        setToolkitUrl(defaultToolkitUrl);
        setToolkitJsUrl(PathUtil.concat(getToolkitUrl(), defaultJavaScriptPath, '/'));
        setToolkitExtUrl(defaultToolkitExtUrl);
        setToolkitExtJsUrl(PathUtil.concat(getToolkitExtUrl(), defaultJavaScriptPath, '/'));
        setServiceUrl(defaultServiceUrl);
        setLibraryUrl(defaultLibraryUrl);
        setJsLibraryUrl(defaultJsLibraryUrl);
        setIframeUrl(PathUtil.concat(getToolkitUrl(), defaultIFramePath, '/'));
            	
        // load initialization parameters
    	SBTEnvironment environment = SBTEnvironmentFactory.get("defaultEnvironment");
    	setEnvironment(environment);
    }
    
    /**
     * @return the defaultEnvironment
     */
    public SBTEnvironment getEnvironment() {
        return environment;
    }

    /**
     * @param defaultEnvironment
     *            the defaultEnvironment to set
     */
    public void setEnvironment(SBTEnvironment environment) {
        this.environment = environment;
    }

    /**
     * @return the toolkitUrl
     */
    public String getToolkitUrl() {
        return toolkitUrl;
    }

    /**
     * @param toolkitUrl
     *            the toolkitUrl to set
     */
    public void setToolkitUrl(String toolkitUrl) {
        this.toolkitUrl = toolkitUrl;
    }

    /**
     * @return the toolkitJsUrl
     */
    public String getToolkitJsUrl() {
        return toolkitJsUrl;
    }

    /**
     * @param toolkitJsUrl
     *            the toolkitJsUrl to set
     */
    public void setToolkitJsUrl(String toolkitJsUrl) {
        this.toolkitJsUrl = toolkitJsUrl;
    }

    /**
     * @return the serviceUrl
     */
    public String getServiceUrl() {
        return serviceUrl;
    }

    /**
     * @param serviceUrl
     *            the serviceUrl to set
     */
    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    /**
     * @return the iframeUrl
     */
    public String getIframeUrl() {
        return iframeUrl;
    }

    /**
     * @param iframeUrl
     *            the iframeUrl to set
     */
    public void setIframeUrl(String iframeUrl) {
        this.iframeUrl = iframeUrl;
    }

    /**
     * @return the toolkitExtUrl
     */
    public String getToolkitExtUrl() {
        return toolkitExtUrl;
    }

    /**
     * @param toolkitExtUrl
     *            the toolkitExtUrl to set
     */
    public void setToolkitExtUrl(String toolkitExtUrl) {
        this.toolkitExtUrl = toolkitExtUrl;
    }

    /**
     * @return the toolkitExtJsUrl
     */
    public String getToolkitExtJsUrl() {
        return toolkitExtJsUrl;
    }

    /**
     * @param toolkitExtJsUrl
     *            the toolkitExtJsUrl to set
     */
    public void setToolkitExtJsUrl(String toolkitExtJsUrl) {
        this.toolkitExtJsUrl = toolkitExtJsUrl;
    }

	/**
	 * @return the libraryUrl
	 */
	public String getLibraryUrl() {
		return libraryUrl;
	}

	/**
	 * @return the jsLibraryUrl
	 */
	public String getJsLibraryUrl() {
		return jsLibraryUrl;
	}
	
	/**
	 * @param libraryUrl the libraryUrl to set
	 */
	public void setLibraryUrl(String libraryUrl) {
		this.libraryUrl = libraryUrl;
	}

	/**
	 * @param jsLibraryUrl the jsLibraryUrl to set
	 */
	public void setJsLibraryUrl(String jsLibraryUrl) {
		this.jsLibraryUrl = jsLibraryUrl;
	}

	/**
	 * @return the serverUrl
	 */
	public String getServerUrl() {
		return serverUrl;
	}

	/**
	 * @param serverUrl the serverUrl to set
	 */
	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * @return the contextUrl
	 */
	public String getContextUrl() {
		return contextUrl;
	}

	/**
	 * @param contextUrl the contextUrl to set
	 */
	public void setContextUrl(String contextUrl) {
		this.contextUrl = contextUrl;
	}

	/**
	 * @return the requestUrl
	 */
	public String getRequestUrl() {
		return requestUrl;
	}

	/**
	 * @param requestUrl the requestUrl to set
	 */
	public void setRequestUrl(String requestUrl) {
		this.requestUrl = requestUrl;
	}

	/**
	 * @return the pathInfo
	 */
	public String getPathInfo() {
		return pathInfo;
	}

	/**
	 * @param pathInfo the pathInfo to set
	 */
	public void setPathInfo(String pathInfo) {
		this.pathInfo = pathInfo;
	}
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void setParameter(String name, String value) {
		parameters.put(name, value);
	}
	
	/**
	 * 
	 * @param application
	 * @param name
	 * @param defaultValue
	 * @return
	 */
	protected String getAppParameter(Application application, String name, String defaultValue) {
		String value = application.getProperty(name);
		return StringUtil.isEmpty(value) ? defaultValue : value;
	}
	
}
