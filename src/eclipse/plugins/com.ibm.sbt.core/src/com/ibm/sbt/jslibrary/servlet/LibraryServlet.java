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

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.servlet.BaseToolkitServlet;
import com.ibm.commons.util.PathUtil;
import com.ibm.sbt.jslibrary.SBTEnvironment;

/**
 * Servlet used to initialize the toolkit JavaScript library.
 * 
 * @author mwallace
 */
public class LibraryServlet extends BaseToolkitServlet {

    private LibraryRequestParams _defaultParams = new LibraryRequestParams();

    private final Object createEnvironmentLock = new Object();

    private List<Object> libraries;

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

    /**
     * Name of the service creating the libraries.
     */
    static public final String LIBRARY_SERVICE_TYPE = "com.ibm.sbt.jslibrary"; //$NON-NLS-1$
    
    //
    // Default values for library servlet parameters
    //
    static final String DEFAULT_TOOLKIT_URL = "%local_server%/sbt"; //$NON-NLS-1$
    static final String DEFAULT_TOOLKIT_EXT_URL = null;
    static final String DEFAULT_JS_LIBRARY_URL = null;
    static final String DEFAULT_JAVASCRIPT_PATH = "/js/sdk"; //$NON-NLS-1$
    static final String DEFAULT_SERVICE_URL = "%local_application%/service"; //$NON-NLS-1$
    static final String DEFAULT_LIBRARY_URL = "%local_application%/library";
    static final String DEFAULT_IFRAME_PATH = "/xhr/IFrameContent.html"; //$NON-NLS-1$
    static final String DEFAULT_ENDPOINTS = "connections,smartcloud,domino,sametime,dropbox"; //$NON-NLS-1$
    static final String DEFAULT_CLIENT_PROPERTIES = null; //$NON-NLS-1$
    static final String DEFAULT_ENVIRONMENT = "defaultEnvironment"; //$NON-NLS-1$

    static final String sourceClass = LibraryServlet.class.getName();
    static final Logger logger = Logger.getLogger(sourceClass);

    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.ibm.sbt.servlet.BaseToolkitServlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // default parameters
        Application application = Application.get();
        String defaultToolkitUrl = getAppParameter(application, PARAM_TOOLKIT_URL, DEFAULT_TOOLKIT_URL);
        String defaultToolkitExtUrl = getAppParameter(application, PARAM_TOOLKIT_EXT_URL, DEFAULT_TOOLKIT_EXT_URL);
        String defaultJsLibraryUrl = getAppParameter(application, PARAM_JS_LIBRARY_URL, DEFAULT_JS_LIBRARY_URL);
        String defaultJavaScriptPath = getAppParameter(application, PARAM_JAVASCRIPT_PATH, DEFAULT_JAVASCRIPT_PATH);
        String defaultServiceUrl = getAppParameter(application, PARAM_SERVICE_URL, DEFAULT_SERVICE_URL);
        String defaultLibraryUrl = getAppParameter(application, PARAM_LIBRARY_URL, DEFAULT_LIBRARY_URL);
        String defaultIFramePath = getAppParameter(application, PARAM_IFRAME_PATH, DEFAULT_IFRAME_PATH);

        // load initialisation parameters
        _defaultParams.setToolkitUrl(getInitParameter(config, PARAM_TOOLKIT_URL, defaultToolkitUrl));
        _defaultParams.setToolkitJsUrl(PathUtil.concat(_defaultParams.getToolkitUrl(), getInitParameter(config, PARAM_JAVASCRIPT_PATH, defaultJavaScriptPath), '/'));
        _defaultParams.setToolkitExtUrl(getInitParameter(config, PARAM_TOOLKIT_EXT_URL, defaultToolkitExtUrl));
        _defaultParams.setToolkitExtJsUrl(PathUtil.concat(_defaultParams.getToolkitExtUrl(), getInitParameter(config, PARAM_JAVASCRIPT_PATH, defaultJavaScriptPath), '/'));
        _defaultParams.setServiceUrl(getInitParameter(config, PARAM_SERVICE_URL, defaultServiceUrl));
        _defaultParams.setLibraryUrl(getInitParameter(config, PARAM_LIBRARY_URL, defaultLibraryUrl));
        _defaultParams.setJsLibraryUrl(getInitParameter(config, PARAM_JS_LIBRARY_URL, defaultJsLibraryUrl));
        _defaultParams.setIframeUrl(PathUtil.concat(_defaultParams.getToolkitUrl(), getInitParameter(config, PARAM_IFRAME_PATH, defaultIFramePath), '/'));
        // create the libraries
        libraries = readLibraries(application);
    }
    
    protected List<Object> readLibraries(Application application) {
        return application.findServices(LIBRARY_SERVICE_TYPE);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (logger.isLoggable(Level.FINEST)) {
            logger.entering(sourceClass, "doGet", new Object[] { req, resp });
        }

        try {
            Context context = Context.get();
            SBTEnvironment environment = getDefaultEnvironment(context);
            LibraryRequest request = createLibraryRequest(req, resp);
            request.init(_defaultParams);

            AbstractLibrary library = createLibrary(request);
            if (library == null) {
                service400(req, resp, "Unable to handle request for {0} version:{1}", request.getJsLib(), request.getJsVersion());
            } else {
                // handle the request
                library.doGet(request);
            }
        } catch (Throwable thrown) {
            // send 500 response and display causing exception
            serviceException(req, resp, thrown, null, false); // (no lang, no
                                                              // RTL for now)

            if (logger.isLoggable(Level.WARNING)) {
                logger.log(Level.WARNING, "Error servicing library GET request", thrown);
            }
        }

        if (logger.isLoggable(Level.FINEST)) {
            logger.exiting(sourceClass, "doGet");
        }
    }

    /**
     * Create a library request instance for this HTTP request.
     * 
     * @param req
     * @param resp
     * @return
     * @throws IOException
     * @throws ServletException
     */
    protected LibraryRequest createLibraryRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        return new LibraryRequest(req, resp);
    }

    /**
     * Create a default environment.
     * 
     * @param config
     * @param application
     * @return
     */
    protected SBTEnvironment getDefaultEnvironment(Context context) {
        if (_defaultParams.getEnvironment() != null) {
            return _defaultParams.getEnvironment();
        }

        synchronized (createEnvironmentLock) {
            // create a default environment if needed
            if (_defaultParams.getEnvironment() == null) {
                Application application = context.getApplication();
                String environmentName = getAppParameter(application, PARAM_ENVIRONMENT, DEFAULT_ENVIRONMENT);
                SBTEnvironment environment = (SBTEnvironment) context.getBean(environmentName);
                if (environment == null) {
                    ServletConfig config = getServletConfig();
                    String defaultEndpoints = getAppParameter(application, PARAM_ENDPOINTS, DEFAULT_ENDPOINTS);
                    String endpoints = getInitParameter(config, PARAM_ENDPOINTS, defaultEndpoints);
                    String defaultClientProps = getAppParameter(application, PARAM_CLIENT_PROPERTIES, DEFAULT_CLIENT_PROPERTIES);
                    String clientProps = getInitParameter(config, PARAM_CLIENT_PROPERTIES, defaultClientProps);

                    environmentName = getInitParameter(config, PARAM_ENVIRONMENT, DEFAULT_ENVIRONMENT);
                    environment = new SBTEnvironment();
                    environment.setName(environmentName);
                    environment.setEndpoints(endpoints);
                    environment.setProperties(clientProps);
                }
                _defaultParams.setEnvironment(environment);
            }
        }

        return _defaultParams.getEnvironment();
    }

    /**
     * Create a library instance which can handle this request.
     * 
     * @param request
     * @return
     */
    protected AbstractLibrary createLibrary(LibraryRequest request) {
        if (!request.isInited()) {
            throw new IllegalStateException("Access to LibraryRequest before it has been initialised");
        }

        for (Object next : libraries) {
            AbstractLibrary library = (AbstractLibrary) next;
            if (library.isMatch(request)) {
                return library;
            }
        }
        return null;
    }
}
