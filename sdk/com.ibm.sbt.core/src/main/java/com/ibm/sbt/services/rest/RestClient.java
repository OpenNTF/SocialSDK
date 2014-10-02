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
package com.ibm.sbt.services.rest;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.Request;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author mwallace
 *
 */
public class RestClient {
	
	private ClientService clientService;
	
	/**
	 * Construct a RestClient instance which uses basic authentication.
	 * 
	 * @param url
	 * @param user
	 * @param password
	 */
	public RestClient(String url, String user, String password) {
		BasicEndpoint endpoint = createBasicEndpoint(url, user, password);
		clientService = new RestClientService(endpoint);
	}

	/**
	 * Construct a RestClient instance which uses the specified endpoint.
	 * 
	 * @param endpointName
	 */
	public RestClient(String endpointName) {
		// perform standalone intialization if needed
		if (Context.getUnchecked() == null) {
			RuntimeFactoryStandalone runtimeFactory = new RuntimeFactoryStandalone();
			Application application = runtimeFactory.initApplication(null);
			Context.init(application, null, null);
		}
		
		clientService = new RestClientService(endpointName);
	}

	/**
	 * Construct a RestClient instance which uses the specified endpoint.
	 * 
	 * @param endpoint
	 */
	public RestClient(Endpoint endpoint) {
		clientService = new RestClientService(endpoint);
	}

	/**
	 * Construct a RestClient instance.
	 * 
	 * @param endpoint
	 */
	public RestClient() {
	}

	//
	// HTTP call support
	//
	
	public final Request get(String serviceUrl) { 
		return new Request(getClientService(serviceUrl), ClientService.METHOD_GET, serviceUrl);
	}
	
	public final Request delete(String serviceUrl) { 
		return new Request(getClientService(serviceUrl), ClientService.METHOD_DELETE, serviceUrl);
	}
	
	public final Request post(String serviceUrl) { 
		return new Request(getClientService(serviceUrl), ClientService.METHOD_POST, serviceUrl);
	}
	
	public final Request put(String serviceUrl) { 
		return new Request(getClientService(serviceUrl), ClientService.METHOD_PUT, serviceUrl);
	}
	
	//
	// Internals
	//
	
	protected ClientService getClientService(String serviceUrl) {
		if (clientService == null) {
			BasicEndpoint endpoint = createBasicEndpoint(serviceUrl, null, null);
			clientService = new RestClientService(endpoint);
		}
		return clientService;
	}
	
	/*
	 * 
	 */
	private BasicEndpoint createBasicEndpoint(String url, String user, String password) {
		ConnectionsBasicEndpoint basicEndpoint = new ConnectionsBasicEndpoint();
		basicEndpoint.setUrl(url);
		basicEndpoint.setUser(user);
		basicEndpoint.setPassword(password);
		basicEndpoint.setForceTrustSSLCertificate(true);
		return basicEndpoint;
	}
	
	/*
	 * 
	 */
	private class RestClientService extends ClientService {
		
		RestClientService(Endpoint endpoint) {
			super(endpoint);
		}
		
		RestClientService(String endpointName) {
			super(endpointName);
		}
		
	}

}
