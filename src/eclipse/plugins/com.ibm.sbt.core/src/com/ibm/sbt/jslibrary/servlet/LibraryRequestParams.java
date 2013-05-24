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

import com.ibm.sbt.jslibrary.SBTEnvironment;

/**
 * @author Mark Wallace
 * @date 16 Jan 2013
 */
public class LibraryRequestParams {

    private SBTEnvironment _environment;
    private String _toolkitUrl;
    private String _toolkitJsUrl;
    private String _serviceUrl;
    private String _iframeUrl;
    private String _toolkitExtUrl;
    private String _toolkitExtJsUrl;
    private String _libraryUrl;
    private String _jsLibraryUrl;

    /**
     * @return the defaultEnvironment
     */
    public SBTEnvironment getEnvironment() {
        return _environment;
    }

    /**
     * @param defaultEnvironment
     *            the defaultEnvironment to set
     */
    public void setEnvironment(SBTEnvironment environment) {
        _environment = environment;
    }

    /**
     * @return the toolkitUrl
     */
    public String getToolkitUrl() {
        return _toolkitUrl;
    }

    /**
     * @param toolkitUrl
     *            the toolkitUrl to set
     */
    public void setToolkitUrl(String toolkitUrl) {
        _toolkitUrl = toolkitUrl;
    }

    /**
     * @return the toolkitJsUrl
     */
    public String getToolkitJsUrl() {
        return _toolkitJsUrl;
    }

    /**
     * @param toolkitJsUrl
     *            the toolkitJsUrl to set
     */
    public void setToolkitJsUrl(String toolkitJsUrl) {
        _toolkitJsUrl = toolkitJsUrl;
    }

    /**
     * @return the serviceUrl
     */
    public String getServiceUrl() {
        return _serviceUrl;
    }

    /**
     * @param serviceUrl
     *            the serviceUrl to set
     */
    public void setServiceUrl(String serviceUrl) {
        _serviceUrl = serviceUrl;
    }

    /**
     * @return the iframeUrl
     */
    public String getIframeUrl() {
        return _iframeUrl;
    }

    /**
     * @param iframeUrl
     *            the iframeUrl to set
     */
    public void setIframeUrl(String iframeUrl) {
        _iframeUrl = iframeUrl;
    }

    /**
     * @return the toolkitExtUrl
     */
    public String getToolkitExtUrl() {
        return _toolkitExtUrl;
    }

    /**
     * @param toolkitExtUrl
     *            the toolkitExtUrl to set
     */
    public void setToolkitExtUrl(String toolkitExtUrl) {
        _toolkitExtUrl = toolkitExtUrl;
    }

    /**
     * @return the toolkitExtJsUrl
     */
    public String getToolkitExtJsUrl() {
        return _toolkitExtJsUrl;
    }

    /**
     * @param toolkitExtJsUrl
     *            the toolkitExtJsUrl to set
     */
    public void setToolkitExtJsUrl(String toolkitExtJsUrl) {
        _toolkitExtJsUrl = toolkitExtJsUrl;
    }

	/**
	 * @return the libraryUrl
	 */
	public String getLibraryUrl() {
		return _libraryUrl;
	}

	/**
	 * @return the jsLibraryUrl
	 */
	public String getJsLibraryUrl() {
		return _jsLibraryUrl;
	}
	
	/**
	 * @param _libraryUrl the libraryUrl to set
	 */
	public void setLibraryUrl(String _libraryUrl) {
		this._libraryUrl = _libraryUrl;
	}

	/**
	 * @param _jsLibraryUrl the jsLibraryUrl to set
	 */
	public void setJsLibraryUrl(String _jsLibraryUrl) {
		this._jsLibraryUrl = _jsLibraryUrl;
	}
}
