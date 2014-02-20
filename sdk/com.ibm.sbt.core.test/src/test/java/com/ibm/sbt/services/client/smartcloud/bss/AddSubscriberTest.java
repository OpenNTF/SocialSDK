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
public class AddSubscriberTest extends BaseBssTest {

    @Test
    public void testAddSubscriberBadRequest() {
    	try {
    		SubscriberJsonBuilder subscriber = new SubscriberJsonBuilder();
    		subscriber.setEmployeeNumber("1234567")
        	          .setFamilyName("Ninty");
        	System.out.println(subscriber.toJson());
    		
        	getSubscriberManagementService().addSubsciber(subscriber);

    		Assert.fail("Expected bad request response");
    		
    	} catch (BssException be) {
    		// expecting this exception
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.assertNotNull("Expected JSON response", jsonObject);
    		Assert.assertEquals("400", be.getResponseCode());
    		Assert.assertNotNull(be.getResponseMessage());
    		Assert.assertEquals("Error", be.getSeverity());
    		Assert.assertNotNull(be.getUserAction());
    		Assert.assertNotNull(be.getMessageId());
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error registering customer caused by: "+e.getMessage());    		
    	}
    }
	
    @Test
    public void testAddSubscriber() {
    	try {
    		String customerId = registerCustomer();
    		
    		SubscriberJsonBuilder subscriber = new SubscriberJsonBuilder();
    		subscriber.setCustomerId(customerId)
    				  .setRole(SubscriberManagementService.Role.User)
    				  .setFamilyName("Doe")
    				  .setGivenName("John")
    				  .setEmailAddress(getUniqueEmail())
    				  .setNamePrefix("Mr")
    				  .setNameSuffix("")
    				  .setEmployeeNumber("6A7777B")
    				  .setLanguagePreference("EN_US")
    				  .setWorkPhone("111-111-1111")
    				  .setMobilePhone("111-111-1112")
    				  .setHomePhone("111-111-1113")
    				  .setFax("111-111-1114")
    				  .setJobTitle("Director")
    				  .setWebSiteAddress("www.example.com")
    				  .setTimeZone("America/Central")
    				  .setPhoto("");
        	System.out.println(subscriber.toJson());
    		
        	JsonJavaObject response = getSubscriberManagementService().addSubsciber(subscriber);
        	long subscriberId = response.getAsLong("Long");
        	Assert.assertNotNull("Invalid subscriber id", subscriberId);
        	System.out.println(subscriberId);
        	
        	JsonEntity subscriberEntity = getSubscriberManagementService().getSubscriberById(String.valueOf(subscriberId));
        	System.out.println(subscriberEntity.toJsonString());
        	
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error registering customer because: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error registering customer caused by: "+e.getMessage());    		
    	}
    }
	
}
