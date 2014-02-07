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

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaFactory;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.commons.util.io.json.JsonParser;
import com.ibm.sbt.services.client.ClientService;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.base.IFeedHandler;
import com.ibm.sbt.services.client.base.JsonEntity;
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
     * @param cacheSize
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
     * @return
     */
    public BigInteger getSubscriptionId(JsonJavaObject subscriptionObject) {
    	return getId(subscriptionObject, PROPERTY_SUBSCRIPTION);
    }
    
    /**
     * Create one or more subscriptions for a given customer.
     * 
     * @param order
     * @return
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
     * @return
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
     * @return
     * @throws BssException
     */
    public EntityList<JsonEntity> createSubscription(JsonJavaObject orderObject) throws BssException {
		try {
			Response serverResponse = createData(API_RESOURCE_SUBSCRIPTION, null, JsonHeader, orderObject, ClientService.FORMAT_JSON);
			
			IFeedHandler<JsonEntity> jsonHandler = getJsonFeedHandler();
			return jsonHandler.createEntityList(serverResponse);
		} catch (Exception e) {
			throw new BssException(e);
		}
    }
    
    /**
     * Use the subscription ID to locate a subscription and get details about that subscription. 
     * 
     * @param subscriptionId
     * @return
     * @throws BssException
     */
    public JsonEntity getSubscriptionById(BigInteger subscriptionId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIPTION + "/" + subscriptionId;
			return getEntity(serviceUrl, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription {0}", subscriptionId);
		}
    }
    
    /**
     * Get a list of subscriptions that belong to a particular customer.
     * 
     * @param customerId
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscriptionsById(String customerId) throws BssException {
    	if (StringUtil.isEmpty(customerId)) {
    		throw new IllegalArgumentException("Invalid id");
    	}
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_namedQuery", "getSubscriptionByCustomer");
    		params.put("customerId", customerId);
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIPTION, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription list by customer id {0}", customerId);
		}
    }
  
    /**
     * Get a list of subscriptions for all the vendor's customers.
     * 
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscriptions() throws BssException {
    	try {
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIPTION, null, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription list");
		}
    }
  
    /**
     * Get a list of subscriptions for all the vendor's customers.
     * 
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws BssException
     * @throws {@link IllegalArgumentException}
     */
    public EntityList<JsonEntity> getSubscriptions(int pageNumber, int pageSize) throws BssException {
    	try {
    		HashMap<String, String> params = new HashMap<String, String>();
    		params.put("_pageNumber", String.valueOf(pageNumber));
    		params.put("_pageSize", String.valueOf(pageSize));
			return (EntityList<JsonEntity>)getEntities(API_RESOURCE_SUBSCRIPTION, params, getJsonFeedHandler());
		} catch (Exception e) {
			throw new BssException(e, "Error retrieving subscription list");
		}
    }
  
    /**
     * Suspend a subscription and its child subscriptions for a registered customer. 
     * Any subscriptions who have a seat entitled to the subscription are suspended. 
     * However, any seat that is assigned to subscriptions of the subscription remain in the assigned state.
     * 
     * @param subscriptionId
     * @return
     * @throws BssException
     */
    public void suspendSubscription(BigInteger subscriptionId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIPTION + "/" + subscriptionId;
    		Response response = createData(serviceUrl, null, SuspendSubscriptionHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending subscription {0}", subscriptionId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending subscription {0}", subscriptionId);
		}
    }
    
    /**
     * Change the status of a subscription from suspended to active. Child subscriptions are also moved to active state. 
     * Any subscribers who have a seat entitled to the subscription are moved from suspended to active state. 
     * If the subscription is a pooled child subscription, the parent subscription is also moved to active state.
     * 
     * @param subscriptionId
     * @return
     * @throws BssException
     */
    public void unsuspendSubscription(BigInteger subscriptionId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIPTION + "/" + subscriptionId;
    		Response response = createData(serviceUrl, (Map<String, String>)null, UnsuspendSubscriptionHeader, (Object)null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error suspending subscription {0}", subscriptionId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error suspending subscription {0}", subscriptionId);
		}
    }
	
    /**
     * Cancel a subscription to revoke access for all users who are entitled to the subscription. 
     * After you cancel the subscription, you can mark the subscription as deleted by deleting the customer. 
     * See the Delete customer topic for more details. 
     * 
     * @param subscriptionId
     * @return
     * @throws BssException
     */
    public void cancelSubscription(BigInteger subscriptionId) throws BssException {
    	try {
    		String serviceUrl = API_RESOURCE_SUBSCRIPTION + "/" + subscriptionId;
    		Response response = deleteData(serviceUrl, null, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error cancelling subscription {0}", subscriptionId);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error cancelling subscription {0}", subscriptionId);
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
    		BigInteger subscriptionId = getSubscriptionId(subscriptionObject);
    		String serviceUrl = API_RESOURCE_SUBSCRIPTION + "/" + subscriptionId;
    		Response response = updateData(serviceUrl, null, JsonHeader, subscriptionObject, null);
    		
    		// expect a 204
    		int statusCode = response.getResponse().getStatusLine().getStatusCode();
    		if (statusCode != 204) {
    			throw new BssException(response, "Error updating subscription {0}", subscriptionObject);
    		}
		} catch (Exception e) {
			throw new BssException(e, "Error updating subscription {0}", subscriptionObject);
		}
    }
    
}
