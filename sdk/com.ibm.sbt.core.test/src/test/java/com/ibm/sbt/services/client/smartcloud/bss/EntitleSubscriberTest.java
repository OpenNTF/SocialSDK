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

import java.math.BigInteger;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.BssService.BillingFrequency;

/**
 * @author mwallace
 *
 */
public class EntitleSubscriberTest extends BaseBssTest {

	@Test
    public void testEntitleSubscriber() {
    	try {
    		BigInteger customerId = registerCustomer();
    		BigInteger subscriberId = addSubscriber();

    		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
    		OrderJsonBuilder order = new OrderJsonBuilder();
    		order.setCustomerId(customerId)
    			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
    		     .setDurationLength(3)
    		     .setPartNumber("D0NWLLL")
    		     .setPartQuantity(5)
    		     .setBillingFrequency(BillingFrequency.ARC);
    		EntityList<JsonEntity> subscriptionList = subscriptionManagement.createSubscription(order);
    		for (JsonEntity subscription : subscriptionList) {
    			System.out.println(subscription.toJsonString());
    		}
    		BigInteger subscriptionId = BigInteger.valueOf(subscriptionList.get(0).getAsLong("SubscriptionId"));
    		System.out.println(subscriptionId);
    		
    		JsonEntity subscription = subscriptionManagement.getSubscriptionById(subscriptionId);
    		System.out.println(subscription.toJsonString());
			
    		SubscriberManagementService subscriberManagement = getSubscriberManagementService();
    		
    		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
			Assert.assertNotNull("Unable to retrieve subscriber: "+subscriberId, jsonEntity);
			Assert.assertEquals(subscriberId, subscriberManagement.getSubscriberId(jsonEntity.getJsonObject()));

			subscriberManagement.activateSubscriber(subscriberId);
			
			jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
			Assert.assertNotNull("Unable to retrieve activated subscriber: "+subscriberId, jsonEntity);
			Assert.assertEquals(subscriberId, subscriberManagement.getSubscriberId(jsonEntity.getJsonObject()));
				
			JsonJavaObject rootObject = jsonEntity.getJsonObject();
			Assert.assertNotNull("Unable to retrieve subscriber: "+subscriberId, rootObject);
			
			System.out.println(rootObject);
			JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
			System.out.println(subscriberObject);
			Assert.assertEquals("ACTIVE", subscriberObject.get("SubscriberState"));
						
			JsonEntity entitlement = subscriberManagement.entitleSubscriber(subscriberId, subscriptionId, true);
			Assert.assertNotNull("Unable to entitle subscriber: "+subscriberId, entitlement);
			System.out.println(entitlement.toJsonString());
			
			jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
			Assert.assertNotNull("Unable to retrieve activated subscriber: "+subscriberId, jsonEntity);
			Assert.assertEquals(subscriberId, subscriberManagement.getSubscriberId(jsonEntity.getJsonObject()));
				
			rootObject = jsonEntity.getJsonObject();
			Assert.assertNotNull("Unable to retrieve subscriber: "+subscriberId, rootObject);
			
			subscriberObject = rootObject.getAsObject("Subscriber");
			List<Object> seatSet = subscriberObject.getAsList("SeatSet");
    		for (Object seat : seatSet) {
    			System.out.println(seat);
    		}
			JsonJavaObject seat = (JsonJavaObject)seatSet.get(0);
			System.out.println(seat.getAsLong("SubscriptionId"));
			Assert.assertEquals(subscriptionId, BigInteger.valueOf(seat.getAsLong("SubscriptionId")));
			
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error deleting subscriber caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error deleting subscriber caused by: "+e.getMessage());    		
    	}
    }
	
}
