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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.sbt.jslibrary.MockSBTEnvironment;
import com.ibm.sbt.servlet.MockHttpServletRequest;
import com.ibm.sbt.servlet.MockHttpServletResponse;
import com.ibm.sbt.servlet.MockServletConfig;

/**
 * Unit test for LibraryServlet
 * 
 * @author mwallace
 */
public class LibraryServletTest extends TestCase {

	private LibraryServlet servlet;
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private HttpServletRequest gadgetRequest;
	private Application application;
	private Context context;
	private Map<String, String> gadgetParameters = new HashMap<String, String>();
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		gadgetParameters.put("context", "gadget");
		
		servlet = new LibraryServlet();
		servlet.init(new MockServletConfig());
		
		httpRequest = new MockHttpServletRequest();
		httpResponse = new MockHttpServletResponse();
		gadgetRequest = new MockHttpServletRequest(gadgetParameters);
		application = Application.get();
		context = Context.init(application, httpRequest, httpResponse);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		
		servlet.destroy();
		servlet = null;
		
		httpRequest = null;
		httpResponse = null;
		gadgetRequest = null;
		gadgetParameters = null;
	}

	/**
	 * Validate creating a LibraryRequest
	 */
	public void testCreateLibraryRequest() {
		try {
			LibraryRequest request = servlet.createLibraryRequest(httpRequest, httpResponse);
			assertNotNull("Unable to create library request", request);
		}
		catch (Exception e) {
			fail("Unable to create library request: "+e.getMessage());
		}
	}
	
	/**
	 * Validate creating a BaseLibrary
	 */
	public void testCreateDefaultLibrary() {
		try {
			AbstractLibrary library = createDefaultLibrary();
			assertNotNull("Unable to create default library", library);
			assertTrue("Library is wrong type", library instanceof DojoLibrary);
		}
		catch (Exception e) {
			fail("Unable to create library request: "+e.getMessage());
		}
	}

	/**
	 * Validate creating a dojo library
	 */
	public void testCreateDojoLibrary() {
		try {
			AbstractLibrary library = createLibrary("dojo", "");
			assertNotNull("Unable to create default library", library);
			assertTrue("Library is wrong type", library instanceof DojoLibrary);
		}
		catch (Exception e) {
			fail("Unable to create library request: "+e.getMessage());
		}
	}

	/**
	 * Validate populating endpoints
	 */
	public void testDefaultEndpoint() {
		try {
			LibraryRequest request = servlet.createLibraryRequest(httpRequest, httpResponse);			
			request.init(new MockSBTEnvironment(), "http://localhost:8080/sbt", "http://localhost:8080/sbt/js/sdk", 
					"http://localhost:8080/service", "http://localhost:8080/sbt/xhr/IFrameContent.html");
			AbstractLibrary library = servlet.createLibrary(request);
			
			Map<String,JsonObject> endpoints = library.populateEndpoints(request);
			assertNotNull("Unable to populate endpoints", (endpoints != null) && !endpoints.isEmpty());
		}
		catch (Exception e) {
			fail("Unable to create library request: "+e.getMessage());
		}
	}
	
	//
	// Internals
	//

	/* 
	 * Create default library
	 */
	private AbstractLibrary createDefaultLibrary() throws ServletException, IOException {
		LibraryRequest request = servlet.createLibraryRequest(httpRequest, httpResponse);	
		request.init(new MockSBTEnvironment(), "http://localhost:8080/sbt", "http://localhost:8080/sbt/js/sdk", 
				"http://localhost:8080/service", "http://localhost:8080/sbt/xhr/IFrameContent.html");
		return servlet.createLibrary(request);
	}
	
	/* 
	 * Create dojo library
	 */
	private AbstractLibrary createLibrary(String libraryName, String libraryVersion) throws ServletException, IOException {
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put(LibraryRequest.PARAM_JSLIB, libraryName);
		parameters.put(LibraryRequest.PARAM_JSVERSION, libraryVersion);
		((MockHttpServletRequest)httpRequest).setParameterMap(parameters);
		
		LibraryRequest request = servlet.createLibraryRequest(httpRequest, httpResponse);			
		request.init(new MockSBTEnvironment(), "http://localhost:8080/sbt", "http://localhost:8080/sbt/js/sdk", 
				"http://localhost:8080/service", "http://localhost:8080/sbt/xhr/IFrameContent.html");
		return servlet.createLibrary(request);
	}

}
