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

/**
 * @author mwallace
 *
 */
public class ProvisionSubscriberTest extends BaseBssTest {

    @Test
    public void testProvisionSubscriber() {
    	try {
    		// Step 1. Create customer
    		String customerId = registerCustomer();
    		
    		// Step 2. Add Subscriber with dummy email
    		String subscriberId = addSubscriber(customerId);
    		String loginName = getLoginName(subscriberId);
    		
    		// Step 3. Set one time password
    		setOneTimePassword(loginName, "passw1rd");
    		
    		// Step 4. Change password
    		changePassword(loginName, "passw1rd", "passw0rd");
    		
    		// Step 5. Activate subscriber
    		activateSubscriber(subscriberId);
    		
    		// Step 6. Change email
    		updateSubscriberEmail(subscriberId, "john.doe@example.com");
    		
    		// Display provisioned subscriber
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		System.out.println(subscriber.toJsonString());
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error provisioning subscriber caused by: "+e.getMessage());    		
    	}
    }

}
