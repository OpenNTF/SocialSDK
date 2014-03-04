/*
 * � Copyright IBM Corp. 2014
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
package com.ibm.sbt.web.resources;

import java.io.IOException;

import net.jazz.ajax.model.GeneratedJavaScriptResource;
import net.jazz.ajax.model.RenderContext;
import net.jazz.ajax.model.RenderContext.Mode;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.jslibrary.SBTEnvironment;
import com.ibm.sbt.jslibrary.SBTEnvironment.Endpoint;
import com.ibm.sbt.jslibrary.SBTEnvironment.Property;
import com.ibm.sbt.jslibrary.servlet.DojoLibrary;
import com.ibm.sbt.jslibrary.servlet.LibraryException;
import com.ibm.sbt.jslibrary.servlet.LibraryRequest;
import com.ibm.sbt.jslibrary.servlet.LibraryRequestParams;

public class LibraryResource extends GeneratedJavaScriptResource {
	
	private RuntimeFactory runtimeFactory;
	private Application application;
	
	protected LibraryResource(String id) {
		super(id);
	}
	
	/**
	 * Initialise the Context, needed for Services and Endpoints.
	 */
	private Context createContext() {
		if (runtimeFactory == null) {
			runtimeFactory = new RuntimeFactoryStandalone();
			application = runtimeFactory.initApplication(null);
		}
		return Context.init(application, null, null);
	}

	/**
	 * Destroy the Context.
	 */
	private void destroyContext(Context context) {
		Context.destroy(context);
	}

	/*
	 * Create default environment
	 */
	private SBTEnvironment createDefaultEnvironment(RenderContext renderContext) {
		Endpoint[] endpoints = { createDefaultEndpoint(renderContext) };
		Property[] properties = null;
		SBTEnvironment environment = new SBTEnvironment(LibraryRequestParams.DEFAULT_ENVIRONMENT, endpoints, properties);
		return environment;
	}
	
	/*
	 * Create default endpoint
	 */
	private Endpoint createDefaultEndpoint(RenderContext renderContext) {
		Endpoint endpoint = new Endpoint("connections", null);
		return endpoint;
	}
	
	/**
	 * Generate the JavaScript for the library initialization.
	 * 
	 * @param renderContext
	 * @return
	 * @throws IOException
	 */
	private String generateJavaScript(RenderContext renderContext) throws IOException {
		System.out.println("LibraryResource.generateJavaScript: " + renderContext.request.getRequestURL());
		String output = "";
		Context context = null;
		
		Thread current = Thread.currentThread();
		ClassLoader loader = current.getContextClassLoader();
    	try {
    		current.setContextClassLoader(getClass().getClassLoader());
    		context = createContext();
    		
	    	DojoLibrary library = new DojoLibrary();
	    	
	        // load initialization parameters
	    	LibraryRequestParams params = new LibraryRequestParams();
	    	params.initDefaults();
	    	if (params.getEnvironment() == null) {
	    		params.setEnvironment(createDefaultEnvironment(renderContext));
	    	}
	    	
	    	LibraryRequest request = new LibraryRequest(renderContext.request, null);
	    	request.init(params);
	    	request.setRegPath(false);
	    	request.setInitJs(true);
	    	request.setDebug(renderContext.mode == Mode.DEBUG);
	    	
	    	output = library.generateJavaScript(request);
    	} catch (LibraryException e) {
    		e.printStackTrace();
    		throw new IOException("Error writing library init", e);
    	} finally {
    		destroyContext(context);
    		current.setContextClassLoader(loader);
    	}
    	System.out.println("LibraryResource.generateJavaScript: " + output);
    	return output;
	}

	/* (non-Javadoc)
	 * @see net.jazz.ajax.model.GeneratedJavaScriptResource#content(net.jazz.ajax.model.RenderContext)
	 */
	@Override
	protected CharSequence content(RenderContext context) throws IOException {
		System.out.println("LibraryResource.write: " + context.request.getRequestURI());
		String libraryJS = generateJavaScript(context);
		System.out.println("LibraryResource.write: " + libraryJS);
		return libraryJS.subSequence(0, libraryJS.length());
	}
}
