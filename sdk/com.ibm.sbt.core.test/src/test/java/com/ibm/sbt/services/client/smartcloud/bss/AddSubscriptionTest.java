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
public class AddSubscriptionTest extends BaseBssTest {

	@Test
    public void testEntitleSubscriber() {
    	try {
    		// Step 1. Create customer
    		String customerId = registerCustomer();
    		
    		// Step 2. Add Subscriber
    		String subscriberId = addSubscriber(customerId);

    		// Step 3. Create "SC Engage" Subscription
    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 5);
    		System.out.println(engageSubscriptionId);

    		// Step 4. Create Extra Storage Subscription
    		String storageSubscriptionId = createSubscription(customerId, 3, "D100PLL", 5);
    		System.out.println(storageSubscriptionId);

    		// Step 5. Activate the subscriber
    		activateSubscriber(subscriberId);
    		
    		// Step 6. Entitle subscriber
    		JsonEntity engageEntitlement = entitleSubscriber(subscriberId, engageSubscriptionId, true);
    		System.out.println(engageEntitlement.toJsonString());
    		JsonEntity storageEntitlement = entitleSubscriber(subscriberId, storageSubscriptionId, true);
    		System.out.println(storageEntitlement.toJsonString());
    		
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
