/*
 * � Copyright IBM Corp. 2012
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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.Platform;
import com.ibm.commons.runtime.util.UrlUtil;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.jslibrary.SBTEnvironmentFactory;

/**
 * Wrapper object to encapsulate a request to the library servlet.
 * 
 * @author priand
 * @author mwallace
 */
public class LibraryRequest {

    private boolean inited;

    protected HttpServletRequest httpRequest;
    protected HttpServletResponse httpResponse;

    protected String jsLib;
    protected String jsVersion;
    protected String toolkitUrl;
    protected String toolkitJsUrl;
    protected String serviceUrl;
    protected String libraryUrl;
    protected String iframeUrl;
    protected String toolkitExtUrl;
    protected String toolkitExtJsUrl;
    protected String jsLibraryUrl;
    protected boolean debug;
    protected boolean debugTransport;
    protected boolean mockTransport;
    protected boolean layer;
    protected boolean regPath;
    protected boolean initJs;

    protected SBTEnvironment environment;

    // List of the default endpoints being added by default.
    //public static final String INIT_JS = "/init.js";
    public static final String DEFAULT_JSLIB = "dojo";
    public static final String DEFAULT_VERSION = "1.4";
    public static final Boolean DEFAULT_DEBUG = false;
    public static final Boolean DEFAULT_LAYER = false;
    public static final Boolean DEFAULT_REGPATH = true;
    public static final Boolean DEFAULT_INITJS = false;
    public static final Boolean DEFAULT_DEBUG_TRANSPORT = false;
    public static final Boolean DEFAULT_MOCK_TRANSPORT = false;

    // Definition of the servlet parameters (query string)

    /**
     * List of endpoints to generate
     */
    public static final String PARAM_ENDPOINTS = "endpoints";

    /**
     * List of client properties to generate
     */
    public static final String PARAM_CLIENT_PROPERTIES = "props";

    /**
     * List of client runtimes
     */
    public static final String PARAM_CLIENT_RUNTIMES = "runtimes";
    
    /**
     * Underlying library to be used, default is 'dojo'
     */
    public static final String PARAM_JSLIB = "lib";

    /**
     * Version of the library to use default is '1.4'
     */
    public static final String PARAM_JSVERSION = "ver";

    /**
     * Name of the environment to use
     */
    public static final String PARAM_ENVIRONMENT = "env";

    /**
     * Sets debug mode if true
     */
    public static final String PARAM_DEBUG = "debug";
    
    /**
     * Sets debug transport mode if true
     */
    public static final String PARAM_DEBUG_TRANSPORT = "debugTransport";
    
    /**
     * Sets mock transport mode if true
     */
    public static final String PARAM_MOCK_TRANSPORT = "mockTransport";
    
    /**
     * Sets javascript output for layer consumption
     */
    public static final String PARAM_LAYER = "layer";

    /**
     * Sets javascript output for aggregation with Connections' _js
     */
    public static final String PARAM_REGPATH = "regPath";
    
    /**
     * Sets 
     */
    public static final String PARAM_INITJS = "initJs";

    /**
     * Enables/Disables the aggregator (default is false)
     */
    public static final String PARAM_AGGREGATOR = "aggregator";

    /**
     * List of modules/layers to load, '*' means everything
     */
    public static final String PARAM_MODULE = "modules";

    /**
     * The context in which the library is being loaded into. This is an
     * optional param on the request. Currently the only valid value is
     * <code>gadget</code>.
     * 
     * @see GADGET_CONTEXT
     */
    public static final String PARAM_CONTEXT = "context";

    static final String sourceClass = LibraryRequest.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);

    /**
     * Default constructor
     */
    public LibraryRequest() {
    }

    /**
     * 
     * @param req
     * @param resp
     */
    public LibraryRequest(HttpServletRequest req, HttpServletResponse resp) {
        this.httpRequest = req;
        this.httpResponse = resp;
    }

    /**
     * Initialise the LibraryRequest
     * 
     * @param params
     * 
     * @throws ServletException
     * @throws IOException
     */
    public void init(LibraryRequestParams params) throws LibraryException {
    	try {
	        this.toolkitUrl = StringUtil.replace(params.getToolkitUrl(), "%local_server%", getServerUrl(params));
	        this.toolkitJsUrl = StringUtil.replace(params.getToolkitJsUrl(), "%local_server%", getServerUrl(params));
	        this.toolkitExtUrl = StringUtil.replace(params.getToolkitExtUrl(), "%local_server%", getServerUrl(params));
	        this.toolkitExtJsUrl = StringUtil.replace(params.getToolkitExtJsUrl(), "%local_server%", getServerUrl(params));
	        this.serviceUrl = StringUtil.replace(params.getServiceUrl(), "%local_application%", getContextUrl(params));
	        this.libraryUrl = params.getLibraryUrl().indexOf("%")>-1 ? getRequestUrl(params) : params.getLibraryUrl();
	        this.jsLibraryUrl = StringUtil.replace(params.getJsLibraryUrl(), "%local_server%", getServerUrl(params));
	        this.iframeUrl = StringUtil.replace(params.getIframeUrl(), "%local_server%", getServerUrl(params));
	
	        readFromRequest(params, params.getEnvironment());
	
	        inited = true;
	
	        if (logger.isLoggable(Level.FINE)) {
	            logger.fine("Created library request: " + toString());
	        }
    	} catch (Exception e) {
    		throw new LibraryException("Error initialising library request caused by: "+e.getMessage(), e);
    	}
    }

    /**
     * @return the inited
     */
    public boolean isInited() {
        return inited;
    }

    /**
     * @return the httpRequest
     */
    public HttpServletRequest getHttpRequest() {
        return httpRequest;
    }

    /**
     * @return the httpResponse
     */
    public HttpServletResponse getHttpResponse() {
        return httpResponse;
    }
    
    /**
     * 
     * @param name
     * @return
     */
    public String getParameter(String name) {
    	if (httpRequest != null) {
    		return httpRequest.getParameter(name);
    	}
    	return null;
    }

    /**
     * 
     * @return
     */
    public boolean isProxyDomain() {
        return true;
    }

    /**
     * 
     * @return
     */
    public String getServiceUrl() {
        return serviceUrl;
    }
    
    /**
     * 
     * @return
     */
    public String getLibraryUrl() {
        return libraryUrl;
    }

    /**
     * 
     * @return
     */
    public String getJsLibraryUrl() {
        return jsLibraryUrl;
    }
    
    /**
     * 
     * @return
     */
    public boolean useIFrame() {
        return false;
    }

    /**
	 * @param initJS the initJs to set
	 */
	public void setInitJs(boolean initJs) {
		this.initJs = initJs;
	}

    /**
	 * @return the js
	 */
	public boolean isInitJs() {
		return initJs;
	}
    
    /**
     * 
     * @return
     */
    public boolean isDebug() {
        return debug;
    }

    /**
	 * @param debug the debug to set
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

    /**
     * 
     * @return
     */
    public boolean isLayer() {
        return layer;
    }

    /**
     * 
     * @return
     */
    public boolean isRegPath() {
        return regPath;
    }


    /**
	 * @param regPath the regPath to set
	 */
	public void setRegPath(boolean regPath) {
		this.regPath = regPath;
	}

    /**
     * 
     * @return
     */
    public boolean isDebugTransport() {
        return debugTransport;
    }

    /**
     * 
     * @return
     */
    public boolean isMockTransport() {
        return mockTransport;
    }

    /**
     * 
     * @return
     */
    public String getIFrameUrl() {
        return iframeUrl;
    }

    /**
     * @return the jsLib
     */
    public String getJsLib() {
        return jsLib;
    }

    /**
     * @return the jsVersion
     */
    public String getJsVersion() {
        return jsVersion;
    }

    /**
     * @return the toolkit URL
     */
    public String getToolkitUrl() {
        return toolkitUrl;
    }

    /**
     * @return the toolkit JavaScript URL
     */
    public String getToolkitJsUrl() {
        return toolkitJsUrl;
    }

    /**
     * @return the toolkit extention URL
     */
    public String getToolkitExtUrl() {
        return toolkitExtUrl;
    }

    /**
     * @return the toolkit extention URL
     */
    public String getToolkitExtJsUrl() {
        return toolkitExtJsUrl;
    }

    /**
     * @return the environment
     */
    public SBTEnvironment getEnvironment() {
        return environment;
    }

    /**
     * Return the specified header
     * 
     * @return
     */
    public String getHeader(String name) {
        return httpRequest.getHeader(name);
    }

	/**
	 * @return
	 */
	protected String getServerUrl(LibraryRequestParams params) {
		if (httpRequest != null) {
			return UrlUtil.getServerUrl(httpRequest);
		} else {
			return params.getServerUrl();
		}
	}

	/**
	 * @return
	 */
	protected String getContextUrl(LibraryRequestParams params) {
		if (httpRequest != null) {
			return UrlUtil.getContextUrl(httpRequest);
		} else {
			return params.getContextUrl();
		}
	}

	/**
	 * @return
	 */
	protected String getRequestUrl(LibraryRequestParams params) {
		if (httpRequest != null) {
			return UrlUtil.getRequestUrl(httpRequest);
		} else {
			return params.getRequestUrl();
		}
	}

    /**
     * 
     * @param req
     * 
     * @throws ServletException
     * @throws IOException
     */
    protected void readFromRequest(LibraryRequestParams params, SBTEnvironment defaultEnvironment) throws ServletException, IOException {
        jsLib = readString(params, PARAM_JSLIB, getDefaultJsLib());
        jsVersion = readString(params, PARAM_JSVERSION, DEFAULT_JSLIB.equals(jsLib) ? getDefaultJsVersion() : "");
        debug = Boolean.parseBoolean(readString(params, PARAM_DEBUG, getDefaultDebug()));
        layer = Boolean.parseBoolean(readString(params, PARAM_LAYER, getDefaultLayer()));
        regPath = Boolean.parseBoolean(readString(params, PARAM_REGPATH, getDefaultRegPath()));
        initJs = Boolean.parseBoolean(readString(params, PARAM_INITJS, getDefaultInitJs()));
        debugTransport = Boolean.parseBoolean(readString(params, PARAM_DEBUG_TRANSPORT, getDefaultDebugTransport()));
        mockTransport = Boolean.parseBoolean(readString(params, PARAM_MOCK_TRANSPORT, getDefaultMockTransport()));
        String environmentName = readString(params, PARAM_ENVIRONMENT, null);
        if (!StringUtil.isEmpty(environmentName)) {
            SBTEnvironment parentEnvironment = SBTEnvironmentFactory.get(environmentName);
            if (parentEnvironment == null) {
                String message = MessageFormat.format("Unable to load environment: {0}", environmentName);
                throw new ServletException(message);
            }
            parentEnvironment.prepareEndpoints();
            environment = new RequestEnvironment(params, parentEnvironment);
        }
        if (environment == null) {
        	if(defaultEnvironment != null) {
        		defaultEnvironment.prepareEndpoints();
        	}
            environment = new RequestEnvironment(params, defaultEnvironment);
        }
    }
    
    protected String getPathInfo(LibraryRequestParams params) {
    	if (httpRequest != null) {
    		return httpRequest.getPathInfo();
    	} else {
    		return params.getPathInfo();
    	}
    }
    
    protected String getDefaultJsLib() {
    	return DEFAULT_JSLIB;
    }
    
    protected String getDefaultJsVersion() {
    	return DEFAULT_VERSION;
    }
    
    protected String getDefaultDebug() {
    	return DEFAULT_DEBUG.toString();
    }

    protected String getDefaultLayer() {
    	return DEFAULT_LAYER.toString();
    }

    protected String getDefaultRegPath() {
    	return DEFAULT_REGPATH.toString();
    }

    protected String getDefaultInitJs() {
    	return DEFAULT_INITJS.toString();
    }

    protected String getDefaultDebugTransport() {
    	return DEFAULT_DEBUG_TRANSPORT.toString();
    }

    protected String getDefaultMockTransport() {
    	return DEFAULT_MOCK_TRANSPORT.toString();
    }

    /**
     * Read a parameter from the request and return the value if it's a valid
     * value i.e. not null and not an empty string. Otherwise return the default
     * value.
     * 
     * @param req
     * @param paramName
     * @param defValue
     * 
     * @return
     * 
     * @throws ServletException
     * @throws IOException
     */
    private String readString(LibraryRequestParams params, String paramName, String defaultValue) {
        String val = null;
        if (httpRequest != null) {
        	val = httpRequest.getParameter(paramName);
        } else {
        	val = params.getParameter(paramName);
        }
        if (val != null && val.length() > 0) {
            return val;
        }
        return defaultValue;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("jsLib=").append(jsLib);
        sb.append(";jsVersion=").append(jsVersion);
        sb.append(";environment=").append(environment);
        sb.append(";toolkitUrl=").append(toolkitUrl);
        sb.append(";toolkitJsUrl=").append(toolkitJsUrl);
        sb.append(";jsLibraryUrl=").append(jsLibraryUrl);
        sb.append(";layer=").append(layer);
        sb.append(";regPath=").append(regPath);
        sb.append("}");
        return sb.toString();
    }

    /**
     * Class representing the SBTEnvironment for this request
     * 
     * @author mwallace
     */
    class RequestEnvironment extends SBTEnvironment {

        private SBTEnvironment parent;

        /**
         * 
         * @param parent
         */
        RequestEnvironment(LibraryRequestParams params, SBTEnvironment parent) {
            this.parent = parent;

            String endpointsStr = readString(params, PARAM_ENDPOINTS, null);
            if (!StringUtil.isEmpty(endpointsStr)) {
                Endpoint[] requestEndpoints = parseEndpoints(endpointsStr);
                if (requestEndpoints != null && requestEndpoints.length > 0) {
                    Endpoint[] defaultEndpoints = parent.getEndpointsArray();
                    List<Endpoint> endpoints = new ArrayList<Endpoint>();
                    for (int i = 0; i < requestEndpoints.length; i++) {
                        endpoints.add(requestEndpoints[i]);
                    }
                    for (int i = 0; i < defaultEndpoints.length; i++) {
                        endpoints.add(defaultEndpoints[i]);
                    }
                    setEndpointsArray(endpoints.toArray(new Endpoint[endpoints.size()]));
                }
            }

            String propertiesStr = readString(params, PARAM_CLIENT_PROPERTIES, null);
            if (!StringUtil.isEmpty(propertiesStr)) {
                try {
                    Property[] requestProperties = parseProperties(propertiesStr);
                    if (requestProperties != null && requestProperties.length > 0) {
                        Property[] defaultProperties = parent.getPropertiesArray();
                        List<Property> properties = new ArrayList<Property>();
                        for (int i = 0; i < requestProperties.length; i++) {
                            properties.add(requestProperties[i]);
                        }
                        for (int i = 0; i < defaultProperties.length; i++) {
                            properties.add(defaultProperties[i]);
                        }
                        setPropertiesArray(properties.toArray(new Property[properties.size()]));
                    }
                } catch (IOException ex) {
                    Platform.getInstance().log(ex);
                }
            }
            
            String runtimesStr = readString(params, PARAM_CLIENT_RUNTIMES, null);
            if(!StringUtil.isEmpty(runtimesStr)){
                setRuntimes(runtimesStr);
            }
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.ibm.sbt.jslibrary.SBTEnvironment#getName()
         */
        @Override
        public String getName() {
            return parent.getName();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.ibm.sbt.jslibrary.SBTEnvironment#getEndpointsArray()
         */
        @Override
        public Endpoint[] getEndpointsArray() {
            Endpoint[] endpoints = super.getEndpointsArray();
            if (endpoints != null) {
                return endpoints;
            }
            return parent.getEndpointsArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see com.ibm.sbt.jslibrary.SBTEnvironment#getPropertiesArray()
         */
        @Override
        public Property[] getPropertiesArray() {
            Property[] properties = super.getPropertiesArray();
            if (properties != null) {
                return properties;
            }
            return parent.getPropertiesArray();
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.ibm.sbt.jslibrary.SBTEnvironment#getPropertyByName(java.lang.
         * String)
         */
        @Override
        public Property getPropertyByName(String name) {
            Property property = super.getPropertyByName(name);
            if (property != null) {
                return property;
            }
            return parent.getPropertyByName(name);
        }

        /*
         * (non-Javadoc)
         * 
         * @see
         * com.ibm.sbt.jslibrary.SBTEnvironment#getPropertyValueByName(java.
         * lang.String)
         */
        @Override
        public String getPropertyValueByName(String name) {
            String value = super.getPropertyValueByName(name);
            if (value != null) {
                return value;
            }
            return parent.getPropertyValueByName(name);
        }

    }

}
