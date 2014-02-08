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
public class CreateSubscriptionTest extends BaseBssTest {

    @Test
    public void testCreateSubscriptionBadRequest() {
    	try {
    		OrderJsonBuilder order = new OrderJsonBuilder();
    		order.setCustomerId(BigInteger.ZERO)
    			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
    		     .setDurationLength(3);
    		System.out.println(order.toJson());
        	
    		getSubscriptionManagementService().createSubscription(order);

    		Assert.fail("Expected bad request response");
    		
    	} catch (BssException be) {
    		// expecting this exception
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.assertNotNull("Expected JSON response", jsonObject);
    		Assert.assertEquals("400", be.getResponseCode());
    		Assert.assertEquals("The [] input value that was provided for the [BillingFrequency] field is invalid.", be.getResponseMessage());
    		Assert.assertEquals("Error", be.getSeverity());
    		Assert.assertEquals("Provide a valid input value for the [BillingFrequency] field.", be.getUserAction());
    		Assert.assertEquals("BZSAP1011E", be.getMessageId());
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error creating subscription caused by: "+e.getMessage());    		
    	}
    }
	
    @Test
    public void testCreateSubscription() {
    	try {
    		BigInteger customerId = registerCustomer();
    		OrderJsonBuilder order = new OrderJsonBuilder();
    		order.setCustomerId(customerId)
    			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
    		     .setDurationLength(3)
    		     .setPartNumber("D0NWLLL")
    		     .setPartQuantity(5)
    		     .setBillingFrequency(BillingFrequency.ARC);
    		System.out.println(order.toJson());
        	
    		EntityList<JsonEntity> subscriptionList = getSubscriptionManagementService().createSubscription(order);
    		for (JsonEntity subscription : subscriptionList) {
    			Assert.assertNotNull(subscription.getAsString("SubscriptionId"));
    			Assert.assertEquals("D0NWLLL", subscription.getAsString("PartNumber"));
				System.out.println(subscription.toJsonString());
			}
    		
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		//be.printStackTrace();
    		Assert.fail("Error creating subscription because: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error creating subscription caused by: "+e.getMessage());    		
    	}
    }
	
	
}
