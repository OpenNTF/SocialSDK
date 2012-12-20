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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.TestCase;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.io.json.JsonObject;
import com.ibm.commons.util.io.json.JsonReference;
import com.ibm.sbt.jslibrary.MockSBTEnvironment;
import com.ibm.sbt.servlet.MockHttpServletRequest;
import com.ibm.sbt.servlet.MockHttpServletResponse;
import com.ibm.sbt.servlet.MockServletConfig;

/**
 * Unit test for DojoLibrary
 * 
 * @author mwallace
 */
public class BaseLibraryTest extends TestCase {

	private LibraryServlet servlet;
	private HttpServletRequest httpRequest;
	private HttpServletResponse httpResponse;
	private AbstractLibrary library;
	private Context context;
	private LibraryRequest request;
	private String toolkitUrl;
	private String toolkitJsUrl;
	private String proxyUrl;
	private String iframeUrl;
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		servlet = new LibraryServlet();
		servlet.init(new MockServletConfig());
		
		httpRequest = new MockHttpServletRequest();
		httpResponse = new MockHttpServletResponse();
		
		context = Context.init(servlet.getApplication(), httpRequest, httpResponse);
		
		request = servlet.createLibraryRequest(httpRequest, httpResponse);
		request.init(new MockSBTEnvironment(), toolkitUrl, toolkitJsUrl, proxyUrl, iframeUrl);
		
		library = new DojoLibrary();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		Context.destroy(context);
		context = null;
		
		servlet.destroy();
		servlet = null;
		
		httpRequest = null;
		httpResponse = null;
	}

	/**
	 * Validate default endpoint is available
	 */
	public void testDefaultEndpoint() {
		try {
			Map<String,JsonObject> endpoints = library.populateEndpoints(request);
			
			assertFalse("Default endpoint was not created", endpoints.isEmpty());
			assertTrue("Default endpoint is not connections", endpoints.containsKey("connections"));
			
			JsonObject endpoint = endpoints.get("connections");
			assertEquals("Invalid authenticator", "new Basic({})", ((JsonReference)endpoint.getJsonProperty("authenticator")).getRef());
			assertEquals("Invalid baseUrl", "http://icsqs.ibm.com:444", endpoint.getJsonProperty("baseUrl"));
			assertEquals("Invalid transport", "new Transport()", ((JsonReference)endpoint.getJsonProperty("transport")).getRef());
			assertNull("Invalid proxyPath", endpoint.getJsonProperty("proxyPath"));
			assertNull("Invalid proxy", endpoint.getJsonProperty("proxy"));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Unable populate default endpoint: "+e.getMessage());
		}
	}
	
	/**
	 * Validate oauth endpoint is available
	 */
	public void testOAuthEndpoint() {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(LibraryRequest.PARAM_ENDPOINTS, "smartcloud");
			((MockHttpServletRequest)httpRequest).setParameterMap(parameters);
			
			Map<String,JsonObject> endpoints = library.populateEndpoints(request);
			
			assertFalse("Endpoint was not created", endpoints.isEmpty());
			assertTrue("Endpoint is not smartcloud", endpoints.containsKey("smartcloud"));
			
			JsonObject endpoint = endpoints.get("smartcloud");
			assertEquals("Invalid authenticator", "new OAuth10({\"url\":\"http:\\/\\/localhost:8080\\/sbt.mock.web\\/service\\/oauth10_jsauth\\/smartcloud\"})", ((JsonReference)endpoint.getJsonProperty("authenticator")).getRef());
			assertEquals("Invalid baseUrl", "https://apps.lotuslive.com", endpoint.getJsonProperty("baseUrl"));
			assertEquals("Invalid transport", "new Transport()", ((JsonReference)endpoint.getJsonProperty("transport")).getRef());
			assertNull("Invalid proxyPath", endpoint.getJsonProperty("proxyPath"));
			assertNull("Invalid proxy", endpoint.getJsonProperty("proxy"));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Unable populate oauth endpoint: "+e.getMessage());
		}
	}
	
	/**
	 * Validate missing endpoint is available
	 */
	public void testMissingEndpoint() {
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put(LibraryRequest.PARAM_ENDPOINTS, "myendpoint");
			((MockHttpServletRequest)httpRequest).setParameterMap(parameters);
			
			request = servlet.createLibraryRequest(httpRequest, httpResponse);
			request.init(new MockSBTEnvironment(), toolkitUrl, toolkitJsUrl, proxyUrl, iframeUrl);

			Map<String,JsonObject> endpoints = library.populateEndpoints(request);
			
			assertFalse("Endpoint was not created", endpoints.isEmpty());
			assertTrue("Endpoint is not myendpoint", endpoints.containsKey("myendpoint"));
			
			JsonObject endpoint = endpoints.get("myendpoint");
			assertNull("Invalid authenticator", endpoint.getJsonProperty("authenticator"));
			assertEquals("Invalid baseUrl", "", endpoint.getJsonProperty("baseUrl"));
			assertEquals("Invalid transport", "new ErrorTransport('myendpoint')", ((JsonReference)endpoint.getJsonProperty("transport")).getRef());
			assertNull("Invalid proxyPath", endpoint.getJsonProperty("proxyPath"));
			assertNull("Invalid proxy", endpoint.getJsonProperty("proxy"));
		}
		catch (Exception e) {
			e.printStackTrace();
			fail("Unable populate oauth endpoint: "+e.getMessage());
		}
	}
	
	/**
	 * Validate populating the properties
	 */
	public void testPopulateProperties() {
		try {
			JsonObject properties = library.populateProperties(request);
			
			assertNotNull("Properties were not created", properties);
			assertTrue("Properties should not be empty", properties.getJsonProperties().hasNext());
		}
		catch (Exception e) {
			fail("Unable populate properties: "+e.getMessage());
		}
	}
	
	/**
	 * Validate creating a depend modules
	 */
	public void testDependModules() {
		try {
			Map<String, JsonObject> endpoints = new HashMap<String, JsonObject>();
			String[] modules = library.getDependModules(request, endpoints);
			assertTrue("Depend modules doesn't handle no endpoints", modules.length == 0);
		}
		catch (Exception e) {
			fail("Unable to get dependent modules: "+e.getMessage());
		}
	}
	
}
