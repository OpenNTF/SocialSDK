/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.jslibrary;

import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.jslibrary.SBTEnvironment.Endpoint;
import com.ibm.sbt.jslibrary.SBTEnvironment.Property;
import com.ibm.sbt.jslibrary.servlet.DojoLibrary;
import com.ibm.sbt.jslibrary.servlet.LibraryException;
import com.ibm.sbt.jslibrary.servlet.LibraryRequest;
import com.ibm.sbt.jslibrary.servlet.LibraryRequestParams;

/**
 * @author mwallace
 *
 */
@SuppressWarnings("deprecation")
public class LibraryTest {

	private RuntimeFactory runtimeFactory;
	private Context context;
	private Application application;
	
    static final String DEFAULT_TOOLKIT_URL = "%local_server%/sbt"; //$NON-NLS-1$
    static final String DEFAULT_TOOLKIT_EXT_URL = null;
    static final String DEFAULT_JS_LIBRARY_URL = "%local_server%/sbt/js/libs"; //$NON-NLS-1$
    static final String DEFAULT_JAVASCRIPT_PATH = "/js/sdk"; //$NON-NLS-1$
    static final String DEFAULT_SERVICE_URL = "%local_application%/service"; //$NON-NLS-1$
    static final String DEFAULT_LIBRARY_URL = "%local_application%/library";
    static final String DEFAULT_IFRAME_PATH = "/xhr/IFrameContent.html"; //$NON-NLS-1$
	
	@Test
    public void testDojoLibrary() throws Exception {
    	try {
    		createContext();
    		
	    	DojoLibrary library = new DojoLibrary();
	    	
	    	SBTEnvironment environment = SBTEnvironmentFactory.get("defaultEnvironment");
	    	
	        // load initialization parameters
	    	LibraryRequestParams params = new LibraryRequestParams();
	    	params.setEnvironment(environment);
	    	params.setParameter("debug", "true");
	    	params.setParameter("_js", "true");
	        params.setToolkitUrl(DEFAULT_TOOLKIT_URL);
	        params.setToolkitJsUrl(DEFAULT_JS_LIBRARY_URL);
	        params.setServiceUrl(DEFAULT_SERVICE_URL);
	        params.setLibraryUrl(DEFAULT_LIBRARY_URL);
	        params.setJsLibraryUrl(DEFAULT_JS_LIBRARY_URL);
	        params.setIframeUrl(DEFAULT_IFRAME_PATH);
	        params.setRequestUrl("https://localhost:8443/sbt.sample.web/library?lib=dojo&ver=1.4.3&debug=true");
	        params.setServerUrl("https://localhost:8443");
	        params.setContextUrl("https://localhost:8443/sbt.sample.web");
	        params.setPathInfo("/init.js");
	    	
	    	LibraryRequest request = new LibraryRequest();
	    	request.init(params);
	    	
	    	String javascript = library.generateJavaScript(request);
	    	System.out.println(javascript);
	    	Assert.assertNotNull(javascript);
    	} catch (Exception e) {
    		e.printStackTrace(System.err);
    		Assert.fail("Error generating javascript caused by:"+e.getMessage());
    	} finally {
    		//destroyContext();
    	}
    }
	
	/**
	 * Initialise the Context, needed for Services and Endpoints.
	 */
	public void createContext() {
		runtimeFactory = new RuntimeFactoryStandalone();
		application = runtimeFactory.initApplication(null);
		context = Context.init(application, null, null);
	}

	/**
	 * Destroy the Context.
	 */
	public void destroyContext() {
		if (context != null) {
			Context.destroy(context);
		}
		if (application != null) {
			Application.destroy(application);
		}
	}

    
}
