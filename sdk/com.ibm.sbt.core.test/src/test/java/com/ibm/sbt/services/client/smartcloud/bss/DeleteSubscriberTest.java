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

/**
 * @author mwallace
 *
 */
public class DeleteSubscriberTest extends BaseBssTest {

	@Test
    public void testDeleteSubscriber() {
    	try {
    		registerCustomer();
    		BigInteger subscriberId = addSubscriber();

    		SubscriberManagementService subscriberManagement = getSubscriberManagementService();
    		
    		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
			Assert.assertNotNull("Unable to retrieve subscriber: "+subscriberId, jsonEntity);
			Assert.assertEquals(subscriberId, subscriberManagement.getSubscriberId(jsonEntity.getJsonObject()));

			subscriberManagement.deleteSubsciber(subscriberId);
			
			try {
				jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
				Assert.assertNull("Able to retrieve deleted subscriber: "+subscriberId, jsonEntity);				
			} catch (BssException cme) {
				String responseCode = cme.getResponseCode();
				Assert.assertEquals("404", responseCode);
			}
				
    	} catch (BssException cme) {
    		JsonJavaObject jsonObject = cme.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error deleting subscriber caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error deleting subscriber caused by: "+e.getMessage());    		
    	}
    }
	
}
