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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author dtobin
 *
 */
public class UpdateSubscriptionTest extends BaseBssTest {

    @Test
    public void testUpdateSubscription() {
    	try {
    		String subscriptionId = createSubscription();
    		
    		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
    		
    		JsonEntity jsonEntity = subscriptionManagement.getSubscriptionById(subscriptionId);
			Assert.assertNotNull("Unable to retrieve subscription: " + subscriptionId, jsonEntity);
			Assert.assertEquals(subscriptionId, subscriptionManagement.getSubscriptionId(jsonEntity.getJsonObject()));

			JsonJavaObject rootObject = jsonEntity.getJsonObject();
			Assert.assertNotNull("Unable to retrieve subscription: "+subscriptionId, rootObject);
			
			System.out.println(rootObject);
			JsonJavaObject subscriptionObject = rootObject.getAsObject("Subscription");
		//	long noOfSeats = subscriptionObject.getAsLong("NumberOfSeats");
			subscriptionObject.putString("ExpirationDate", "04/13/2018 00:00:00");
			
//			if (noOfSeats>0) {
//				noOfSeats--;
//				subscriptionObject.putLong("NumberOfSeats", noOfSeats);
//			}
//			 else {
//				noOfSeats++;
//				subscriptionObject.putLong("NumberOfSeats", noOfSeats);
//			}
			System.out.println(rootObject);
			subscriptionManagement.updateSubscription(rootObject);
			
			jsonEntity = subscriptionManagement.getSubscriptionById(subscriptionId);
			Assert.assertNotNull("Unable to retrieve subscription: "+subscriptionId, jsonEntity);
			Assert.assertEquals(subscriptionId, subscriptionManagement.getSubscriptionId(jsonEntity.getJsonObject()));
			
			rootObject = jsonEntity.getJsonObject();
			Assert.assertNotNull("Unable to retrieve subscription: "+subscriptionId, subscriptionObject);
			
			System.out.println(rootObject);
			subscriptionObject = rootObject.getAsObject("Subscription");
			Assert.assertEquals("04/13/2018 00:00:00", subscriptionObject.getAsString("ExpirationDate"));
//			Assert.assertEquals(noOfSeats, subscriptionObject.getAsLong("NumberOfSeats"));
				
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error updating subscription caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error updating subscription caused by: "+e.getMessage());    		
    	}
    }
		
}