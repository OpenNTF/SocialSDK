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

import com.ibm.sbt.jslibrary.SBTEnvironment;

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
	
}
