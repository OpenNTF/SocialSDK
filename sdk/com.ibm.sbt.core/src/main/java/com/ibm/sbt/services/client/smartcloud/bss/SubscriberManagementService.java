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
import java.util.List;
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
import com.ibm.sbt.services.client.base.NamedUrlPart;
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
    public String getSubscriberId(JsonJavaObject subscriberObject) {
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
    public JsonJavaObject addSubscriber(SubscriberJsonBuilder subscriber) throws BssException, IOException, JsonException {
    	return addSubscriber(subscriber.toJson());
    }

    /**
     * Add a subscriber either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param subscriberJson
     * @return
     * @throws BssException
     * @throws JsonException
     * @throws IOException 
     */
    public JsonJavaObject addSubscriber(String subscriberJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, subscriberJson);
    	return addSubscriber(jsonObject);
    }

    /**
     * Add a subscriber either to the vendor's organization or to the organization of one of the vendor's customers.
     * 
     * @param subscriberJson
     * @return JSON object containing 
     * @throws BssException
     * @throws IOException 
     */
    public JsonJavaObject addSubscriber(JsonJavaObject subscriberJson) throws BssException {
		try {
			String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER.format(this);
			Response serverResponse = createData(serviceUrl, null, JsonHeader, subscriberJson, ClientService.FORMAT_JSON);
			return (serverResponse == null) ? null : (JsonJavaObject)serverResponse.getData();
		} catch (Exception e) {
			throw new BssException(e, "Error adding subscriber {0} caused by {1}", subscriberJson, e.getMessage());
		}
    }
      
    /**
     * Get details about a single subscriber. 
     * 
     * @param subscriberId
     * @return
     * @throws BssException
     */
    public JsonEntity getSubscriberById(String subscriberId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER_SUBSCRIBERID.format(this, BssUrls.subscriberId(subscriberId));
			return getEntity(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber {0} caused by {1}", subscriberId, e.getMessage());
		}
    }
    
    /**
     * Activate a subscriber.
     * 
     * @param subscriberId
     * @throws BssException
     */
    public void activateSubscriber(String subscriberId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER_SUBSCRIBERID.format(this, BssUrls.subscriberId(subscriberId));
    		Response response = createData(serviceUrl, (Map<String, String>)null, ActivateSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error activating subscriber {0} caused by {1}", subscriberId, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error activating subscriber {0} caused by {1}", subscriberId, e.getMessage());
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
    public void deleteSubscriber(String subscriberId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER_SUBSCRIBERID.format(this, BssUrls.subscriberId(subscriberId));
    		Response response = deleteData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error deleting subscriber {0}", subscriberId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error deleting subscriber {0} caused by {1}", subscriberId, e.getMessage());
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
    		String subscriberId = getSubscriberId(subscriberObject);
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER_SUBSCRIBERID.format(this, BssUrls.subscriberId(subscriberId));
    		Response response = updateData(serviceUrl, null, JsonHeader, subscriberObject, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error updating subscriber profile {0} caused by {1}", subscriberObject, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error updating subscriber profile {0} caused by {1]", subscriberObject, e.getMessage());
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
			String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER.format(this);
			return (EntityList<JsonEntity>)getEntities(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list caused by {0}", e.getMessage());
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
			String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER.format(this);
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list page {0},{1} caused by {2}", pageNumber, pageNumber, e.getMessage());
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
    	return getSubscribersByEmail(email, new HashMap<String, String>());
    }
    
    /**
     * Get a list of subscribers with a given email address.
     * 
     * @param email
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscribersByEmail(String email, HashMap<String, String> params) throws BssException {
    	if (StringUtil.isEmpty(email)) {
    		throw new IllegalArgumentException("Invalid email address");
    	}
    	try {
			String serviceUrl = BssUrls.API_RESOURCE_GET_SUBSCRIBERS_BY_EMAIL_ADDRESS.format(this, new NamedUrlPart("emailAddress", email));
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list by email {0} caused by {1}", email, e.getMessage());
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
    	return getSubscribersById(customerId, new HashMap<String, String>());
    }
  
    /**
     * Get a list of all subscribers for a customer with a given ID.
     * 
     * @param customerId
     * @param params
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscribersById(String customerId, Map<String, String> params) throws BssException {
    	if (StringUtil.isEmpty(customerId)) {
    		throw new IllegalArgumentException("Invalid id");
    	}
    	try {
			String serviceUrl = BssUrls.API_RESOURCE_GET_SUBSCRIBER_BY_CUSTOMER.format(this, new NamedUrlPart("customerId", customerId));
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscriber list by customer id {0} caused by {1}", customerId, e.getMessage());
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
    public void suspendSubscriber(String subscriberId, boolean force) throws BssException {
    	try {
    		Map<String, String> params = new HashMap<String, String>();
    		params.put("_force", force ? "true" : "false");
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER_SUBSCRIBERID.format(this, BssUrls.subscriberId(subscriberId));
    		Response response = createData(serviceUrl, params, SuspendSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending subscriber {0} caused {1}", subscriberId, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending subscriber {0} with force {1} caused by {2}", subscriberId, force, e.getMessage());
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
    public void unsuspendSubscriber(String subscriberId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIBER_SUBSCRIBERID.format(this, BssUrls.subscriberId(subscriberId));
    		Response response = createData(serviceUrl, (Map<String, String>)null, UnsuspendSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error unsuspending subscriber {0} caused by {1}", subscriberId, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error unsuspending subscriber {0} caused by {1}", subscriberId, e.getMessage());
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
    public JsonEntity entitleSubscriber(String subscriberId, String subscriptionId, boolean acceptTOU) throws BssException {
       	try {
       		String serviceUrl = BssUrls.API_RESOURCE_ENTITLE_SUBSCRIBER.format(this, 
       				new NamedUrlPart("subscriberId", subscriberId), 
       				new NamedUrlPart("subscriptionId", subscriptionId), 
       				new NamedUrlPart("acceptTOU", acceptTOU ? "true" : "false"));
    		Response response = createData(serviceUrl, null, EntitleSubscriberHeader, (Object)null);
    		return getJsonFeedHandler().createEntity(response);
		} catch (Exception e) {
			throw new BssException(e, "Error entitling subscriber {0} to {1} with {2} caused by {3}", subscriberId, subscriptionId, acceptTOU, e.getMessage());
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
    public void revokeSubscriber(String subscriberId, String seatId, boolean force) throws BssException {
       	try {
       		String serviceUrl = BssUrls.API_RESOURCE_REVOKE_SUBSCRIBER.format(this, 
       				new NamedUrlPart("subscriberId", subscriberId), 
       				new NamedUrlPart("seatId", seatId), 
       				new NamedUrlPart("_force", force ? "true" : "false"));
    		Response response = createData(serviceUrl, null, RevokeSubscriberHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error revoking subscriber {0} to {1} with force {2} caused by {3}", subscriberId, seatId, force, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error revoking subscriber {0} to {1} with force {2} caused by {3}", subscriberId, seatId, force, e.getMessage());
		}
    }
    
    /**
     * Wait for the subscriber to change to the specified state and then call the state change listener.
     * 
     * @param customerId
     * @param state
     * @param maxAttempts
     * @param waitInterval
     * @param listener
     * 
     * @throws BssException 
     */
    public boolean waitSubscriberState(String subscriberId, String state, int maxAttempts, long waitInterval, StateChangeListener listener) throws BssException {
    	for (int i=0; i<maxAttempts; i++) {
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		String currentState = subscriber.getAsString("Subscriber/SubscriberState");
    		if (state.equalsIgnoreCase(currentState)) {
    			try {
    				if (listener != null) {
    					listener.stateChanged(subscriber);
    				}
    				return true;
    			} catch (Exception e) {
    				logger.log(Level.WARNING, "Error invoking subscriber state listener", e);
    			}
    		}
    		
    		// wait the specified interval
			try {
				Thread.sleep(waitInterval);
			} catch (InterruptedException ie) {}
    	}
    	return false;
    }
    
    /**
     * Wait for the subscriber seat to change to the specified state and then call the state change listener.
     * 
     * @param customerId
     * @param state
     * @param maxAttempts
     * @param waitInterval
     * @param listener
     * 
     * @throws BssException 
     */
    public boolean waitSeatState(String subscriberId, String subscriptionId, String state, int maxAttempts, long waitInterval, StateChangeListener listener) throws BssException {
    	for (int i=0; i<maxAttempts; i++) {
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		JsonJavaObject subscriberJson = subscriber.getJsonObject().getAsObject("Subscriber");
    		List<Object> seatSet = subscriberJson.getAsList("SeatSet");
    		JsonJavaObject seatJson = findSeat(seatSet, subscriptionId);
    		if (seatJson != null) {
        		String currentState = seatJson.getAsString("SeatState");
        		if (state.equalsIgnoreCase(currentState)) {
        			try {
        				if (listener != null) {
        					listener.stateChanged(subscriber);
        				}
        				return true;
        			} catch (Exception e) {
        				logger.log(Level.WARNING, "Error invoking subscriber state listener", e);
        			}
        		}
    		}
    		
    		// wait the specified interval
			try {
				Thread.sleep(waitInterval);
			} catch (InterruptedException ie) {}
    	}
    	return false;
    }
    
    private JsonJavaObject findSeat(List<Object> seatSet, String subscriptionId) {
    	for (Object seat : seatSet) {
    		String nextSubscriptionId = "" + (long)((JsonJavaObject)seat).getAsDouble("SubscriptionId");
    		if (subscriptionId.equals(nextSubscriptionId)) {
    			return (JsonJavaObject)seat;
    		}
    	}
    	return null;
    }
    
}
