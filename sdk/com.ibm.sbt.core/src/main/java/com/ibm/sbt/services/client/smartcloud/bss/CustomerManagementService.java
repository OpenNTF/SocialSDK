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
package com.ibm.sbt.services.client.smartcloud.bss;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use customer management services to register a customer, get a customer ID, update a customer profile, remove a customer, suspend a customer, and more.
 * 
 * @author mwallace
 */
public class CustomerManagementService extends BssService {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public CustomerManagementService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public CustomerManagementService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public CustomerManagementService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public CustomerManagementService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public CustomerManagementService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
    /**
     * Return customer id for the specified JSON object.
     * 
     * @param customerObject
     * @return
     */
    public String getCustomerId(JsonJavaObject customerObject) {
    	return getId(customerObject, PROPERTY_CUSTOMER);
    }
    
    /**
     * Get details about a single customer, such as current status and contact information. 
     * 
     * @param customerId
     * @return
     * @throws BssException
     */
    public JsonEntity getCustomerById(String customerId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_CUSTOMER + "/" + customerId;
			return getEntity(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving customer {0} caused by {1}", customerId, e.getMessage());
		}
    }
    
    /**
     * Update the profile of a single customer who is part of the organization of the authenticated user. 
     * Editable attributes include organization name, address, location, contact information, job title and more.
     * 
     * To create the payload, you must first retrieve the most current customer profile for the customer that you intend to update. 
     * To retrieve the most current customer profile, use the GET method Get customer by ID, which returns a JSON payload. 
     * Update the payload that you retrieve and use it as the new payload. You can update one or more of the mutable attributes that are described in the table Mutable attributes.
     * 
     * Note: Attributes that do not have values might not be in the payload that you retrieve. 
     * If the attribute that you want to modify is not in the payload that you retrieve, add the attribute and its value to the payload that you submit. 
     * Mutable attributes that are not in the payload that you submit are deleted from the database.
     * 
     * @param customerObject
     * @throws BssException
     */
    public void updateCustomerProfile(JsonJavaObject customerObject) throws BssException {
    	try {
    		String customerId = getCustomerId(customerObject);
    		String serviceUrl = API_RESOURCE_CUSTOMER + "/" + customerId;
    		Response response = updateData(serviceUrl, null, JsonHeader, customerObject, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error updating customer profile {0}", customerObject);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error updating customer profile {0} caused by {1}", customerObject, e.getMessage());			
		} 
    }
    
    /**
     * Unregister a customer organization using the internal ID of the customer. 
     * When you unregister an organization, all subscribers of that customer are deleted, and all subscriptions are cancelled. 
     * The customer must be owned by the organization of the authenticated user.
     * 
     * @param customerId
     * @return
     * @throws BssException
     */
    public void unregisterCustomer(String customerId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_CUSTOMER + "/" + customerId;
    		Response response = deleteData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error unregistering customer {0}", customerId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error unregistering customer {0} caused by {1}", customerId, e.getMessage());			
		}
    }
    
    /**
     * Change the status of a single customer from active to suspended. 
     * A customer who is suspended cannot use any services. 
     * 
     * @param customerId
     * @return
     * @throws BssException
     */
    public void suspendCustomer(String customerId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_CUSTOMER + "/" + customerId;
    		Response response = createData(serviceUrl, (Map<String, String>)null, SuspendCustomerHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending customer {0}", customerId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending customer {0} caused by {1}", customerId, e.getMessage());			
		}
    }
    
    /**
     * Change the status of a single customer from suspended to active. 
     * Only active customers can use services.
     * 
     * @param customerId
     * @return
     * @throws BssException
     */
    public void unsuspendCustomer(String customerId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_CUSTOMER + "/" + customerId;
    		Response response = createData(serviceUrl, (Map<String, String>)null, UnsuspendCustomerHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending customer {0}", customerId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error unsuspending customer {0} caused by {1}", customerId, e.getMessage());			
		}
    }
    
    /**
     * Get a list of all customers who are part of the vendor's organization.
     * 
     * @return
     * @throws BssException
     */
    public EntityList<JsonEntity> getCustomers() throws BssException {
    	try {
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_CUSTOMER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving customer list caused by {0}", e.getMessage());
		}
    }
    
    /**
     * Get a list of all customers who are part of the vendor's organization.
     * 
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws BssException
     */
    public EntityList<JsonEntity> getCustomers(int pageNumber, int pageSize) throws BssException {
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_CUSTOMER, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving customer list caused by {0}", e.getMessage());
		}
    }
    
    /**
     * Get a customer list by the email address of the contact person.
     * 
     * @param contactEmail
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getCustomersByContactEmail(String contactEmail) throws BssException {
    	if (StringUtil.isEmpty(contactEmail)) {
    		throw new IllegalArgumentException("Invalid contact email address");
    	}
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_namedQuery", "getCustomersByContactEmail");
    		params.put("emailAddress", contactEmail);
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_CUSTOMER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving customer list by contact email {0} caused by {1}", contactEmail, e.getMessage());
		}
    }
    
    /**
     * Get a customer list by the email address of the subscriber.
     * 
     * @param subscriberEmail
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getCustomersBySubscriberEmail(String subscriberEmail) throws BssException {
    	if (StringUtil.isEmpty(subscriberEmail)) {
    		throw new IllegalArgumentException("Invalid subscriber email address");
    	}
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_namedQuery", "getCustomersByContactEmail");
    		params.put("emailAddress", subscriberEmail);
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_CUSTOMER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving customer list by subscriber email {0} caused by {1}", subscriberEmail, e.getMessage());
		}
    }
    
    /**
     * Get a customer list by the organization name.
     * 
     * @param orgName
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getCustomerByOrgName(String orgName) throws BssException {
    	if (StringUtil.isEmpty(orgName)) {
    		throw new IllegalArgumentException("Invalid organization name");
    	}
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_namedQuery", "getCustomersByContactEmail");
    		params.put("orgName", orgName);
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_CUSTOMER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving customer list by organization name {0} caused by {1]", orgName, e.getMessage());
		}
    }
    
    /**
     * Create a customer organization with a contact person.
     * 
     * @param customer
     * @return
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject registerCustomer(CustomerJsonBuilder customer) throws BssException, IOException, JsonException {
    	return registerCustomer(customer.toJson());
    }

    /**
     * Create a customer organization with a contact person.
     * 
     * @param customerJson
     * @return
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject registerCustomer(String customerJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, customerJson);
    	return registerCustomer(jsonObject);
    }

    /**
     * Create a customer organization with a contact person.
     * 
     * @param customerObject
     * @return JSON object containing 
     * @throws BssException
     * @throws IOException 
     */
    public JsonJavaObject registerCustomer(JsonJavaObject customerObject) throws BssException {
		try {
			Response serverResponse = createData(API_RESOURCE_CUSTOMER, null, JsonHeader, customerObject, ClientService.FORMAT_JSON);
			return (JsonJavaObject)serverResponse.getData();
		} catch (Exception e) {
			throw new BssException(e, "Error registering customer {0} caused by {1}", customerObject, e.getMessage());
		}
    }
        
    
    /**
     * Wait for the customer to change to the specified state and then call the state change listener.
     * 
     * @param customerId
     * @param state
     * @param maxAttempts
     * @param waitInterval
     * @param listener
     * 
     * @throws BssException 
     */
    public boolean waitCustomerState(String customerId, String state, int maxAttempts, long waitInterval, StateChangeListener listener) throws BssException {
    	for (int i=0; i<maxAttempts; i++) {
    		JsonEntity subscription = getCustomerById(customerId);
    		String currentState = subscription.getAsString("Customer/CustomerState");
    		if (state.equalsIgnoreCase(currentState)) {
    			try {
    				listener.stateChanged(subscription);
    				return true;
    			} catch (Exception e) {
    				logger.log(Level.WARNING, "Error invoking customer state listener", e);
    			}
    		}
    		
    		// wait the specified interval
			try {
				Thread.sleep(waitInterval);
			} catch (InterruptedException ie) {}
    	}
    	return false;
    }
    
}
