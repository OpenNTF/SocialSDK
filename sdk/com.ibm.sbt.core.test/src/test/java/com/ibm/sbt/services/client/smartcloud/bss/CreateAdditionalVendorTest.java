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
 * @author mwallace
 *
 */
public class CreateAdditionalVendorTest extends BaseBssTest {

    @Test
    public void testCreateAdditionalVendor() {
    	try {
    		String customerId = System.getProperty("CustomerId");
			String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 50);
			System.out.println("SubscriptionId:"+engageSubscriptionId);
			
			final SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
			StateChangeListener stateChangeListener = new StateChangeListener(){
				@Override
				public void stateChanged(JsonEntity jsonEntity) {
					System.out.println(jsonEntity.toJsonString());
				}
			};
			subscriptionManagement.waitSubscriptionState(engageSubscriptionId, "ACTIVE" , 5, 1000, stateChangeListener);

	    	String subscriberId = addSubscriber(customerId);
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    		System.out.println(loginName);
    		
			AuthorizationService authorizationService = getAuthorizationService();
    		authorizationService.assignRole(loginName, "CustomerAdministrator");
    		authorizationService.assignRole(loginName, "VSR");
    		String[] roles = authorizationService.getRoles(loginName);
			Assert.assertTrue(arrayContains("User", roles));
			Assert.assertTrue(arrayContains("CustomerAdministrator", roles));
			Assert.assertTrue(arrayContains("VSR", roles));
			
			activateSubscriber(subscriberId);
			setPassword(loginName, "onet1me!", "passw0rd");
			
			entitleSubscriber(subscriberId, engageSubscriptionId, true, false);

			//getSubscriberManagementService().waitSeatState(subscriberId, engageSubscriptionId, "ASSIGNED", 10, 2000, null);
			long start = System.currentTimeMillis();
	    	for (int i=0; i<100; i++) {
	    		JsonJavaObject seatJson = findSeat(subscriberId, engageSubscriptionId);
	    		if (seatJson != null) {
	    			System.out.println(seatJson.toString());
	        		String currentState = seatJson.getAsObject("Seat").getAsString("SeatState");
	        		System.out.println("SeatState:"+currentState);
	        		if ("ASSIGNED".equalsIgnoreCase(currentState)) {
	        			break;
	        		}
	    		}
	    		
	    		// wait the specified interval
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {}
	    	}
	    	long duration = System.currentTimeMillis() - start;
	    	System.out.println("Wait for seat took: " + duration);

			subscriber = getSubscriberManagementService().getSubscriberById(subscriberId);
			System.out.println(subscriber.toJsonString());
			
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error retrieving roles caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving roles caused by: "+e.getMessage());    		
    	}
    }

}
