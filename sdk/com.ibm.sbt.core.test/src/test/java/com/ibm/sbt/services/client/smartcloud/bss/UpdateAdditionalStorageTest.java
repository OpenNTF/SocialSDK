/*
 * � Copyright IBM Corp. 2014
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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author mwallace
 *
 */
public class UpdateAdditionalStorageTest extends BaseBssTest {

    @Test
    public void testUpdateSubscription() {
    	try {
    		// Step 1. Create customer
    		String customerId = registerCustomer();
    		
    		// prevent deletion
    		//setCustomerId(null);

    		// Step 2. Add Subscriber
    		String subscriberId = addAdministrator(customerId);
    		System.out.println(getSubscriberById(subscriberId));

    		// Step 3. Create "IBM SmartCloud Connections" Subscription
    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 5);
    		System.out.println(engageSubscriptionId);
    		System.out.println("D0NWLLL : " + getSubscriptionById(engageSubscriptionId).toJsonString());

    		// Step 4. Create Extra Storage Subscription
    		String storageSubscriptionId = createSubscription(customerId, 3, "D100PLL", 100);
    		System.out.println(storageSubscriptionId);
    		System.out.println("D100PLL : " + getSubscriptionById(storageSubscriptionId).toJsonString());
    		
    		// Step 5. Update the subscription will cause a 400 Bad Request  		
    		JsonEntity jsonEntity = getSubscriptionById(storageSubscriptionId);
    		JsonJavaObject rootObject = jsonEntity.getJsonObject().getAsObject("Subscription");
    		rootObject.putInt("PartQuantity", 150);
    		getSubscriptionManagementService().updateSubscription(rootObject);

    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error adding subscription caused by: "+e.getMessage());    		
    	}
    }
		
}