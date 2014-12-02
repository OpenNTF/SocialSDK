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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.nameSpaceCtx;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Node;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.xml.xpath.XPathExpression;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Request;
import com.ibm.sbt.services.client.base.AtomEntity;
import com.ibm.sbt.services.client.base.AtomFeedHandler;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.JsonFeedHandler;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author mwallace
 *
 */
public class RestClient {
	
	private RestService restService;
	
	/**
	 * Construct a RestClient instance which uses basic authentication.
	 * 
	 * @param url The URL of the target server.
	 * @param user The username to authenticate with.
	 * @param password The Password to authenticate with.
	 * @return RestClient An instance of the RestClient
	 */
	public RestClient(String url, String user, String password) {
		BasicEndpoint endpoint = createBasicEndpoint(url, user, password);
		restService = new RestService(endpoint);
	}

	/**
	 * Construct a RestClient instance which uses the specified endpoint.
	 * 
	 * @param String endpointName The name of the endpoint in Strinf format.
	 * @return RestClient An instance of the RestClient
	 */
	public RestClient(String endpointName) {
		initContext();
		restService = new RestService(endpointName);
	}

	/**
	 * Construct a RestClient instance which uses the specified endpoint.
	 * 
	 * @param Endpoint The endpoint which the rest client will use
	 * @return RestClient An instance of the RestClient
	 */
	public RestClient(Endpoint endpoint) {
		restService = new RestService(endpoint);
	}

	/**
	 * Construct a RestClient instance.
	 * 
	 * @return RestClient - An instance of the RestClient
	 */
	public RestClient() {
	}

	//
	// HTTP call support
	//
	
	public static RestClient endpoint(String endpointName){
		return new RestClient(endpointName);
	}

	public final RestClient useEndpoint(String endpointName) { 
		initContext();
		restService = new RestService(endpointName);
		return this;
	}

	public static RestClient endpoint(Endpoint endpoint){
		return new RestClient(endpoint);
	}

	public final RestClient useEndpoint(Endpoint endpoint) { 
		restService = new RestService(endpoint);
		return this;
	}
	
	/**
	 * Method to send an HTTP GET request to a server, Using a static instance of RestClient .
	 * @param serviceUrl - The url that the GET request is sent to.
	 * @return Request - The Request Object.
	 */
	public static Request get(String serviceUrl) { 
		RestClient restClient = new RestClient();
		return new Request(restClient.getService(serviceUrl), ClientService.METHOD_GET, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP GET request to a server.
	 * @param serviceUrl The url that the GET request is sent to.
	 * @return Request The Request Object.
	 */
	public final Request doGet(String serviceUrl) { 
		return new Request(getService(serviceUrl), ClientService.METHOD_GET, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP DELETE request using a static instance of the restClient. 
	 * @param serviceUrl  The URL the request is sent to
	 * @return Request The Request Object
	 */
	public static Request delete(String serviceUrl) { 
		RestClient restClient = new RestClient();
		return new Request(restClient.getService(serviceUrl), ClientService.METHOD_DELETE, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP DELETE request.
	 * @param serviceUrl  The URL the request is sent to
	 * @return Request The Request Object
	 */
	public final Request doDelete(String serviceUrl) { 
		return new Request(getService(serviceUrl), ClientService.METHOD_DELETE, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP POST request using a static instance of the restClient 
	 * @param serviceUrl  The URL the request is sent to
	 * @return Request The Request Object
	 */
	public static Request post(String serviceUrl) { 
		RestClient restClient = new RestClient();
		return new Request(restClient.getService(serviceUrl), ClientService.METHOD_POST, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP POST request.
	 * @param serviceUrl  The URL the request is sent to
	 * @return Request The Request Object
	 */
	public final Request doPost(String serviceUrl) { 
		return new Request(getService(serviceUrl), ClientService.METHOD_POST, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP PUT request using a static instance of the restClient 
	 * @param serviceUrl  The URL the request is sent to
	 * @return Request The Request Object
	 */
	public static Request put(String serviceUrl) { 
		RestClient restClient = new RestClient();
		return new Request(restClient.getService(serviceUrl), ClientService.METHOD_PUT, serviceUrl);
	}
	
	/**
	 * Method to send an HTTP PUT request.
	 * @param serviceUrl  The URL the request is sent to
	 * @return Request The Request Object
	 */
	public final Request doPut(String serviceUrl) { 
		return new Request(getService(serviceUrl), ClientService.METHOD_PUT, serviceUrl);
	}
	
	//
	// Internals
	//
	
	private RestService getService(String serviceUrl) {
		if (restService == null) {
			BasicEndpoint endpoint = createBasicEndpoint(serviceUrl, null, null);
			restService = new RestService(endpoint);
		}
		return restService;
	}

	/*
	 * Perform standalone intialization if needed
	 */
	private static void initContext(){
		if (Context.getUnchecked() == null) {
			RuntimeFactoryStandalone runtimeFactory = new RuntimeFactoryStandalone();
			Application application = runtimeFactory.initApplication(null);
			Context.init(application, null, null);
		}
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
	public class RestService extends BaseService {
		
		private static final long serialVersionUID = 1L;

		RestService(Endpoint endpoint) {
			super(endpoint);
		}
		
		RestService(String endpointName) {
			super(endpointName);
		}
		
		public Map<String, String> getParameters(Map<String, String> parameters) {
			if(parameters == null) return new HashMap<String, String>();
			else return parameters;
		}

		// ATOM support
			    
		public AtomEntity getAtomEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
			try {
				return getEntity(requestUrl, parameters, getAtomFeedHandler(false));
			} catch (ClientServicesException e) {
				throw e;
			} catch (Exception e) {
				throw new ClientServicesException(e);
			}
		}
	    
		public EntityList<AtomEntity> getAtomEntityList(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
			return (EntityList<AtomEntity>)getEntities(requestUrl, getParameters(parameters), getAtomFeedHandler(true));
		}
		
		public IFeedHandler<AtomEntity> getAtomFeedHandler(boolean isFeed) {
			return new AtomFeedHandler<AtomEntity>(this, isFeed) {

				@Override
				public AtomEntity entityInstance(BaseService service, Node node, XPathExpression xpath) {
					return new AtomEntity(service, node, nameSpaceCtx, xpath);
				}
			};
		}
		
		// JSON support
		
		public JsonEntity getJsonEntity(String requestUrl, Map<String, String> parameters) throws ClientServicesException {
			try {
				return getEntity(requestUrl, parameters, getJsonFeedHandler("."));
			} catch (ClientServicesException e) {
				throw e;
			} catch (Exception e) {
				throw new ClientServicesException(e);
			}
		}
	    
		public EntityList<JsonEntity> getJsonEntityList(String requestUrl, Map<String, String> parameters, String entitiesPath) throws ClientServicesException {
			return (EntityList<JsonEntity>)getEntities(requestUrl, getParameters(parameters), getJsonFeedHandler(entitiesPath));
		}
		
		public IFeedHandler<JsonEntity> getJsonFeedHandler(String entitiesPath) {
			return new JsonFeedHandler<JsonEntity>(this, entitiesPath) {
				@Override
				public JsonEntity newEntity(BaseService service, JsonJavaObject jsonObject) {
					return new JsonEntity(service, jsonObject);
				}
			};
		}
		
		
	}

}
