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

    private LibraryRequestParams defaultParams = new LibraryRequestParams();

    private final Object createEnvironmentLock = new Object();

    private List<Object> libraries;

    /**
     * Name of the service creating the libraries.
     */
    static public final String LIBRARY_SERVICE_TYPE = "com.ibm.sbt.jslibrary"; //$NON-NLS-1$
    
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
        String defaultToolkitUrl = getAppParameter(application, LibraryRequestParams.PARAM_TOOLKIT_URL, LibraryRequestParams.DEFAULT_TOOLKIT_URL);
        String defaultToolkitExtUrl = getAppParameter(application, LibraryRequestParams.PARAM_TOOLKIT_EXT_URL, LibraryRequestParams.DEFAULT_TOOLKIT_EXT_URL);
        String defaultJsLibraryUrl = getAppParameter(application, LibraryRequestParams.PARAM_JS_LIBRARY_URL, LibraryRequestParams.DEFAULT_JS_LIBRARY_URL);
        String defaultJavaScriptPath = getAppParameter(application, LibraryRequestParams.PARAM_JAVASCRIPT_PATH, LibraryRequestParams.DEFAULT_JAVASCRIPT_PATH);
        String defaultServiceUrl = getAppParameter(application, LibraryRequestParams.PARAM_SERVICE_URL, LibraryRequestParams.DEFAULT_SERVICE_URL);
        String defaultLibraryUrl = getAppParameter(application, LibraryRequestParams.PARAM_LIBRARY_URL, LibraryRequestParams.DEFAULT_LIBRARY_URL);
        String defaultIFramePath = getAppParameter(application, LibraryRequestParams.PARAM_IFRAME_PATH, LibraryRequestParams.DEFAULT_IFRAME_PATH);

        // load initialisation parameters
        defaultParams.setToolkitUrl(getInitParameter(config, LibraryRequestParams.PARAM_TOOLKIT_URL, defaultToolkitUrl));
        defaultParams.setToolkitJsUrl(PathUtil.concat(defaultParams.getToolkitUrl(), getInitParameter(config, LibraryRequestParams.PARAM_JAVASCRIPT_PATH, defaultJavaScriptPath), '/'));
        defaultParams.setToolkitExtUrl(getInitParameter(config, LibraryRequestParams.PARAM_TOOLKIT_EXT_URL, defaultToolkitExtUrl));
        defaultParams.setToolkitExtJsUrl(PathUtil.concat(defaultParams.getToolkitExtUrl(), getInitParameter(config, LibraryRequestParams.PARAM_JAVASCRIPT_PATH, defaultJavaScriptPath), '/'));
        defaultParams.setServiceUrl(getInitParameter(config, LibraryRequestParams.PARAM_SERVICE_URL, defaultServiceUrl));
        defaultParams.setLibraryUrl(getInitParameter(config, LibraryRequestParams.PARAM_LIBRARY_URL, defaultLibraryUrl));
        defaultParams.setJsLibraryUrl(getInitParameter(config, LibraryRequestParams.PARAM_JS_LIBRARY_URL, defaultJsLibraryUrl));
        defaultParams.setIframeUrl(PathUtil.concat(defaultParams.getToolkitUrl(), getInitParameter(config, LibraryRequestParams.PARAM_IFRAME_PATH, defaultIFramePath), '/'));

        // create the libraries
        libraries = readLibraries(application);
    }
    
	/**
	 * 
	 * @param application
	 * @return
	 */
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
            request.init(defaultParams);

            AbstractLibrary library = createLibrary(request);
            if (library == null) {
                service400(req, resp, "Unable to handle request for library={0}, version={1}", request.getJsLib(), request.getJsVersion());
            } else {
                // handle the request
                library.doGet(request);
            }
        } catch (Throwable thrown) {
            // send 500 response and display causing exception
            serviceException(req, resp, thrown, null, false); 

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
        if (defaultParams.getEnvironment() != null) {
            return defaultParams.getEnvironment();
        }

        synchronized (createEnvironmentLock) {
            // create a default environment if needed
            if (defaultParams.getEnvironment() == null) {
                Application application = context.getApplication();
                String environmentName = getAppParameter(application, LibraryRequestParams.PARAM_ENVIRONMENT, LibraryRequestParams.DEFAULT_ENVIRONMENT);
                SBTEnvironment environment = (SBTEnvironment) context.getBean(environmentName);
                if (environment == null) {
                    ServletConfig config = getServletConfig();
                    String defaultEndpoints = getAppParameter(application, LibraryRequestParams.PARAM_ENDPOINTS, LibraryRequestParams.DEFAULT_ENDPOINTS);
                    String endpoints = getInitParameter(config, LibraryRequestParams.PARAM_ENDPOINTS, defaultEndpoints);
                    String defaultClientProps = getAppParameter(application, LibraryRequestParams.PARAM_CLIENT_PROPERTIES, LibraryRequestParams.DEFAULT_CLIENT_PROPERTIES);
                    String clientProps = getInitParameter(config, LibraryRequestParams.PARAM_CLIENT_PROPERTIES, defaultClientProps);

                    environmentName = getInitParameter(config, LibraryRequestParams.PARAM_ENVIRONMENT, LibraryRequestParams.DEFAULT_ENVIRONMENT);
                    environment = new SBTEnvironment();
                    environment.setName(environmentName);
                    environment.setEndpoints(endpoints);
                    environment.setProperties(clientProps);
                }
                defaultParams.setEnvironment(environment);
            }
        }

        return defaultParams.getEnvironment();
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
