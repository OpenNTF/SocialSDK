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

import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author dtobin
 *
 */
public class GetSubscriptionByCustomerIdTest extends BaseBssTest {

	@Test
    public void testGetSubscriptionByCustomerId() {
    	try {
    		CustomerManagementService customerManagement = getCustomerManagementService();
    		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
    		EntityList<JsonEntity> customerList = customerManagement.getCustomers();
    		if (!customerList.isEmpty()) {
    			JsonEntity customer = customerList.get(0);
    			long customerId = customer.getAsLong("Id");
    			
    			EntityList<JsonEntity> subscriptionList = subscriptionManagement.getSubscriptionsById("" + customerId);
    			if(!subscriptionList.isEmpty()){
    			JsonEntity subscription = subscriptionList.get(0);
    			long subscriptionId = subscription.getAsLong("Id");
        		System.out.println(subscription.toJsonString(false));
        		Assert.assertNotNull("Unable to retrieve subscriptions for customer: "+customerId, subscription);
        		Assert.assertEquals(""+subscriptionId, getSubscriptionManagementService().getSubscriptionId(subscription.getJsonObject()));
    			}
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving subscription caused by: "+e.getMessage());    		
    	}
    }
		
}