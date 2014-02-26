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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author mwallace
 *
 */
public class ProvisioningPendingTest extends BaseBssTest {

	@Test
    public void testEntitleSubscriber() {
    	try {
    		// Step 1. Create customer
    		String customerId = registerCustomer();
    		
    		// Step 2. Add Subscriber
    		final String subscriberId = addSubscriber(customerId);

    		// Step 3. Activate the subscriber
    		activateSubscriber(subscriberId);
    		
    		// Step 4. Create "IBM SmartCloud Connections" Subscription
    		final String subscriptionId = createSubscription(customerId, 3, "D0NWLLL", 5);
    		System.out.println(subscriptionId);
    		//System.out.println("D0NWLLL : " + getSubscriptionById(engageSubscriptionId).toJsonString());
    		
    		// Step 5. Entitle subscriber
    		final SubscriberManagementService subscriberManagement = getSubscriberManagementService();
    		StateChangeListener listener = new StateChangeListener() {
				@Override
				public void stateChanged(JsonEntity jsonEntity) {
					try {
						JsonEntity engageEntitlement = subscriberManagement.entitleSubscriber(subscriberId, subscriptionId, true);
						System.out.println(engageEntitlement.toJsonString());
					} catch (BssException be) {
			    		JsonJavaObject jsonObject = be.getResponseJson();
			    		System.out.println(jsonObject);
			    		Assert.fail("Error entitling subscriber caused by: "+jsonObject);
			    	} 
				}
			};
    		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
    		if (!subscriptionManagement.waitSubscriptionState(subscriptionId, "ACTIVE", 5, 1000, listener)) {
    			Assert.fail("Timeout waiting for subscription to activate");
    		}
    		
    		// Optional: Check seats
    		JsonEntity jsonEntity = getSubscriberManagementService().getSubscriberById(subscriberId);
    		JsonJavaObject rootObject = jsonEntity.getJsonObject();
    		JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
			List<Object> seatSet = subscriberObject.getAsList("SeatSet");
    		for (Object seat : seatSet) {
    			System.out.println(seat);
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error adding subscription caused by: "+e.getMessage());    		
    	}
    }
		
}
