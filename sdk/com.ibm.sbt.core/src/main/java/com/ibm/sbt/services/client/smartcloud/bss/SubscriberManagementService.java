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
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use subscription management services to create subscriptions, suspend subscriptions, get subscriptions by ID, and more.
 * 
 * @author mwallace
 */
public class SubscriberManagementService extends BssService {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public SubscriberManagementService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public SubscriberManagementService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public SubscriberManagementService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public SubscriberManagementService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public SubscriberManagementService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }
    
    /**
     * Return subscriber id for the specified JSON object.
     * 
     * @param subscriberObject
     * @return
     */
    public BigInteger getSubscriberId(JsonJavaObject subscriberObject) {
    	return getId(subscriberObject, PROPERTY_SUBSCRIBER);
    }
    
    /**
     * Add a subscriber either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param customerJson
     * @return
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject addSubsciber(SubscriberJsonBuilder subscriber) throws BssException, IOException, JsonException {
    	return addSubsciber(subscriber.toJson());
    }

    /**
     * Add a subscriber either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param customerJson
     * @return
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject addSubsciber(String customerJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, customerJson);
    	return addSubsciber(jsonObject);
    }

    /**
     * Add a subscriber either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param customerJson
     * @return JSON object containing 
     * @throws BssException
     * @throws IOException 
     */
    public JsonJavaObject addSubsciber(JsonJavaObject customerJson) throws BssException {
		try {
			Response serverResponse = createData(API_RESOURCE_SUBSCRIBER, null, JsonHeader, customerJson, ClientService.FORMAT_JSON);
			return (JsonJavaObject)serverResponse.getData();
		} catch (Exception e) {
			throw new BssException(e);
		}
    }
      
    /**
     * Get details about a single subscriber. 
     * 
     * @param subscriberId
     * @return
     * @throws BssException
     */
    public JsonEntity getSubscriberById(BigInteger subscriberId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId;
			return getEntity(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber {0}", subscriberId);
		}
    }
    
    /**
     * Activate a subscriber.
     * 
     * @param subscriberId
     * @throws BssException
     */
    public void activateSubscriber(BigInteger subscriberId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId;
    		Response response = createData(serviceUrl, (Map<String, String>)null, ActivateSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error deleting subscriber {0}", subscriberId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error deleting subscriber {0}", subscriberId);
		}
    }
    
    /**
     * Delete a subscriber from an organization. 
     * This action also frees the assigned seats to the subscribers. 
     * The subscriber must be owned by the organization of the authenticated user.
     * 
     * @param subscriberId
     * @return
     * @throws BssException
     */
    public void deleteSubsciber(BigInteger subscriberId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId;
    		Response response = deleteData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error deleting subscriber {0}", subscriberId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error deleting subscriber {0}", subscriberId);
		}
    }
    
    /**
     * Update the profile of a registered subscriber. Update details such as name, location, and contact information.
     * Important: Mutable attributes that are not in the payload that you submit are deleted from the database. 
     * To avoid inadvertently deleting information, retrieve the most current subscriber profile for the subscriber that you intend to update.
     * 
     * @param subscriberObject
     * @throws BssException
     */
    public void updateSubscribeProfile(JsonJavaObject subscriberObject) throws BssException {
    	try {
    		BigInteger subscriberId = getSubscriberId(subscriberObject);
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId;
    		Response response = updateData(serviceUrl, null, JsonHeader, subscriberObject, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error updating subscriber profile {0}", subscriberObject);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error updating subscriber profile {0}", subscriberObject);
		}
    }
    
    /**
     * Get a list of subscribers who are in the same organization as the API caller. 
     * Subscribers from the caller's immediate organization and subscribers from all child organizations are returned in a single list.
     * 
     * @return
     * @throws BssException
     */
    public EntityList<JsonEntity> getSubscribers() throws BssException {
    	try {
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIBER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list");
		}
    }
 
    /**
     * Get a list of subscribers who are in the same organization as the API caller. 
     * Subscribers from the caller's immediate organization and subscribers from all child organizations are returned in a single list.
     * 
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws BssException
     */
    public EntityList<JsonEntity> getSubscribers(int pageNumber, int pageSize) throws BssException {
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIBER, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list");
		}
    }
 
    /**
     * Get a list of subscribers with a given email address.
     * 
     * @param email
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscribersByEmail(String email) throws BssException {
    	if (StringUtil.isEmpty(email)) {
    		throw new IllegalArgumentException("Invalid email address");
    	}
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_namedQuery", "getSubscriberByEmailAddress");
    		params.put("emailAddress", email);
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIBER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list by email {0}", email);
		}
    }
    
    /**
     * Get a list of all subscribers for a customer with a given ID.
     * 
     * @param customerId
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscribersById(String customerId) throws BssException {
    	if (StringUtil.isEmpty(customerId)) {
    		throw new IllegalArgumentException("Invalid id");
    	}
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_namedQuery", "getSubscriberByCustomer");
    		params.put("customer", customerId);
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIBER, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list by customer id {0}", customerId);
		}
    }
  
    /**
     * Suspend a subscriber. 
     * Suspended subscribers are no longer active and cannot use services to which they are entitled. 
     * 
     * @param subscriberId
     * @param force
     * @return
     * @throws BssException
     */
    public void suspendSubscriber(BigInteger subscriberId, boolean force) throws BssException {
    	try {
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("_force", force ? "true" : "false");
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId;
    		Response response = createData(serviceUrl, params, SuspendSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending subscriber {0}", subscriberId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending subscriber {0}", subscriberId);
		}
    }
    
    /**
     * Unsuspend a subscriber. 
     * This action returns subscribers to active state so they can use the services to which they are entitled.
     * 
     * @param subscriberId
     * @return
     * @throws BssException
     */
    public void unsuspendSubscriber(BigInteger subscriberId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId;
    		Response response = createData(serviceUrl, (Map<String, String>)null, UnsuspendSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending subscriber {0}", subscriberId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending subscriber {0}", subscriberId);
		}
    }
    
    /**
     * Entitle a subscriber to a subscription. 
     * This action assigns a seat for the subscriber in the subscription.
     * 
     * @param subscriberId
     * @param subscriptionId
     * @param acceptTOU
     * @throws BssException
     */
    public JsonEntity entitleSubscriber(BigInteger subscriberId, BigInteger subscriptionId, boolean acceptTOU) throws BssException {
       	try {
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("acceptTOU", acceptTOU ? "true" : "false");
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId + "/subscription/" + subscriptionId;
    		Response response = createData(serviceUrl, params, EntitleSubscriberHeader, (Object)null);
    		//HttpEntity entity = response.getResponse().getEntity();
    		//System.out.println(EntityUtils.toString(entity));
    		return getJsonFeedHandler().createEntity(response);
		} catch (ClientServicesException e) {
			throw new BssException(e, "Error entitling subscriber {0}", subscriberId);
		}  catch (IOException e) {
			throw new BssException(e, "Error entitling subscriber {0}", subscriberId);
		}
    }
    
    /**
     * Revoke a subscriber from a given subscription. 
     * When revoking subscribers, the seats that they currently occupy in the subscription become available. 
     * This action does not delete subscribers from the system, however.
     * 
     * @param subscriberId
     * @param seatId
     * @param force
     * @throws BssException
     */
    public void revokeSubscriber(BigInteger subscriberId, BigInteger seatId, boolean force) throws BssException {
       	try {
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("_force", force ? "true" : "false");
    		String serviceUrl = API_RESOURCE_SUBSCRIBER + "/" + subscriberId + "/seat/" + seatId;
    		Response response = createData(serviceUrl, params, EntitleSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error revoking subscriber {0}", subscriberId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error revoking subscriber {0}", subscriberId);
		}
    }
    
}
