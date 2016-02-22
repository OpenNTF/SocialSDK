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
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.NamedUrlPart;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * Use subscription management services to create subscriptions, suspend subscriptions, get subscriptions by ID, and more.
 * 
 * @author mwallace
 */
public class SubscriptionManagementService extends BssService {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor
	 */
	public SubscriptionManagementService() {
	}

	/**
	 * Constructor
	 * 
	 * @param endpointName
	 */
	public SubscriptionManagementService(String endpointName) {
		super(endpointName);
	}

	/**
     * Constructor
     * 
     * @param endpointName
     * @param cacheSize
     */
    public SubscriptionManagementService(String endpointName, int cacheSize) {
       super(endpointName, cacheSize);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     */
    public SubscriptionManagementService(Endpoint endpoint) {
        super(endpoint);
    }

	/**
     * Constructor
     * 
     * @param endpoint
     * @param cacheSize
     */
    public SubscriptionManagementService(Endpoint endpoint, int cacheSize) {
    	super(endpoint, cacheSize);
    }

    /**
     * Return subscription id for the specified JSON object.
     * 
     * @param subscriptionObject
     * @return {String}
     */
    public String getSubscriptionId(JsonJavaObject subscriptionObject) {
    	return getId(subscriptionObject, PROPERTY_SUBSCRIPTION);
    }
    
    /**
     * Create one or more subscriptions for a given customer.
     * 
     * @param order
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     * @throws IOException
     * @throws JsonException
     */
    public EntityList<JsonEntity> createSubscription(OrderJsonBuilder order) throws BssException, IOException, JsonException {
    	return createSubscription(order.toJson());
    }

    /**
     * Create one or more subscriptions for a given customer.
     * 
     * @param orderJson
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     * @throws JsonException
     * @throws IOException
     */
    public EntityList<JsonEntity> createSubscription(String orderJson) throws BssException, JsonException, IOException {
    	JsonJavaObject jsonObject = (JsonJavaObject)JsonParser.fromJson(JsonJavaFactory.instanceEx, orderJson);
    	return createSubscription(jsonObject);
    }

    /**
     * Create one or more subscriptions for a given customer.
     * 
     * @param orderObject
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     */
    public EntityList<JsonEntity> createSubscription(JsonJavaObject orderObject) throws BssException {
		try {
			String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION.format(this);
			Response serverResponse = createData(serviceUrl, null, JsonHeader, orderObject, ClientService.FORMAT_JSON);
			
			IFeedHandler<JsonEntity> jsonHandler = getJsonFeedHandler();
			return jsonHandler.createEntityList(serverResponse);
		} catch (Exception e) {
			throw new BssException(e, "Error creating subscription {0} caused by {1}", orderObject, e.getMessage());
		}
    }
    
    /**
     * Use the subscription ID to locate a subscription and get details about that subscription. 
     * 
     * @param subscriptionId
     * @return {JsonEntity}
     * @throws BssException
     */
    public JsonEntity getSubscriptionById(String subscriptionId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SUBSCRIPTIONID.format(this, new NamedUrlPart("subscriptionId", subscriptionId));
			return getEntity(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription {0} caused by {1}", subscriptionId, e.getMessage());
		}
    }
    
    /**
     * Get a list of subscriptions that belong to a particular customer.
     * 
     * @param customerId
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscriptionsById(String customerId) throws BssException {
    	if (StringUtil.isEmpty(customerId)) {
    		throw new IllegalArgumentException("Invalid id");
    	}
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_GET_SUBSCRIPTION_BY_CUSTOMERID.format(this, 
    				new NamedUrlPart("customerId", customerId)); 
			return (EntityList<JsonEntity>)getEntities(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription list by customer id {0} caused by {1}", customerId, e.getMessage());
		}
    }
  
    /**
     * Get a list of subscriptions for all the vendor's customers.
     * 
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscriptions() throws BssException {
    	try {
			String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION.format(this);
			return (EntityList<JsonEntity>)getEntities(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription list caused by {0}", e.getMessage());
		}
    }
  
    /**
     * Get a list of subscriptions for all the vendor's customers.
     * 
     * @param pageNumber
     * @param pageSize
     * @return {EntityList<JsonEntity>}
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscriptions(int pageNumber, int pageSize) throws BssException {
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION.format(this);
			return (EntityList<JsonEntity>)getEntities(serviceUrl, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription list for page {0},{1} caused by {2}", pageNumber, pageSize, e.getMessage());
		}
    }
  
    /**
     * Suspend a subscription and its child subscriptions for a registered customer. 
     * Any subscriptions who have a seat entitled to the subscription are suspended. 
     * However, any seat that is assigned to subscriptions of the subscription remain in the assigned state.
     * 
     * @param subscriptionId
     * @throws BssException
     */
    public void suspendSubscription(String subscriptionId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SUBSCRIPTIONID.format(this, new NamedUrlPart("subscriptionId", subscriptionId));
    		Response response = createData(serviceUrl, null, SuspendSubscriptionHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending subscription {0} caused by {1}", subscriptionId, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending subscription {0} caused by {1}", subscriptionId, e.getMessage());			
		}
    }
    
    /**
     * Change the status of a subscription from suspended to active. Child subscriptions are also moved to active state. 
     * Any subscribers who have a seat entitled to the subscription are moved from suspended to active state. 
     * If the subscription is a pooled child subscription, the parent subscription is also moved to active state.
     * 
     * @param subscriptionId
     * @throws BssException
     */
    public void unsuspendSubscription(String subscriptionId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SUBSCRIPTIONID.format(this, new NamedUrlPart("subscriptionId", subscriptionId));
    		Response response = createData(serviceUrl, (Map<String, String>)null, UnsuspendSubscriptionHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error unsuspending subscription {0} caused by {1}", subscriptionId, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error unsuspending subscription {0} caused by {1}", subscriptionId, e.getMessage());			
		} 
    }
	
    /**
     * Cancel a subscription to revoke access for all users who are entitled to the subscription. 
     * After you cancel the subscription, you can mark the subscription as deleted by deleting the customer. 
     * See the Delete customer topic for more details. 
     * 
     * @param subscriptionId
     * @throws BssException
     */
    public void cancelSubscription(String subscriptionId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SUBSCRIPTIONID.format(this, new NamedUrlPart("subscriptionId", subscriptionId));
    		Response response = deleteData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error cancelling subscription {0} caused by {1}", subscriptionId, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error cancelling subscription {0} caused by {1]", subscriptionId, e.getMessage());			
		}
    }
	
    /**
     * Update a subscription for a registered customer to change the expiration date or the number of available seats. 
     * The subscription must be owned by the organization of the authenticated user.
     * 
     * @param subscriptionObject
     * @throws BssException
     */
    public void updateSubscription(JsonJavaObject subscriptionObject) throws BssException {
    	try {
    		String subscriptionId = getSubscriptionId(subscriptionObject);
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SUBSCRIPTIONID.format(this, new NamedUrlPart("subscriptionId", subscriptionId));
    		Response response = updateData(serviceUrl, null, JsonHeader, subscriptionObject, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error updating subscription {0} causd by {1}", subscriptionObject, statusCode);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error updating subscription {0} caused by {1}", subscriptionObject, e.getMessage());			
		}
    }
    
    /**
     * This service retrieves details of the specified seat object from the BSS database.
     * 
     *  @param subscriptionId The ID of the subscription for which quota is to be changed.
     *  @param seatId The ID of the seat for which quota is to be changed.
     * 
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public JsonEntity getSeat(String subscriptionId, String seatId) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SEAT.format(this, 
    				new NamedUrlPart("subscriptionId", subscriptionId), new NamedUrlPart("seatId", seatId));
    		return getEntity(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving a seat caused by {0}", e.getMessage());
		}
    }
  
    /**
     * This operation is to assign or allocate extra storage for a pooled storage subscription.
     * 
     *  @param subscriptionId The ID of the subscription for which quota is to be changed.
     *  @param seatId The ID of the seat for which quota is to be changed.
     *  @param seatObject The seat in JSON format
     * 
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public void changeQuota(String subscriptionId, String seatId, JsonJavaObject seatObject) throws BssException {
    	try {
    		String serviceUrl = BssUrls.API_RESOURCE_SUBSCRIPTION_SEAT.format(this, 
    				new NamedUrlPart("subscriptionId", subscriptionId), new NamedUrlPart("seatId", seatId));
    		createData(serviceUrl, null, ChangeQuotaHeaders, seatObject, ClientService.FORMAT_JSON);
		} catch (Exception e) {
			throw new BssException(e, "Error changing quota caused by {0}", e.getMessage());
		}
    }
  
    /**
     * Wait for the subscription to change to the specified state and then call the state change listener.
     * 
     * @param subscriptionId
     * @param state
     * @param maxAttempts
     * @param waitInterval
     * @param listener
     * 
     * @throws BssException 
     */
    public boolean waitSubscriptionState(String subscriptionId, String state, int maxAttempts, long waitInterval, StateChangeListener listener) throws BssException {
    	for (int i=0; i<maxAttempts; i++) {
    		JsonEntity subscription = getSubscriptionById(subscriptionId);
    		String currentState = subscription.getAsString("Subscription/SubscriptionState");
    		if (state.equalsIgnoreCase(currentState)) {
    			try {
    				listener.stateChanged(subscription);
    				return true;
    			} catch (Exception e) {
    				logger.log(Level.WARNING, "Error invoking subscription state listener", e);
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
