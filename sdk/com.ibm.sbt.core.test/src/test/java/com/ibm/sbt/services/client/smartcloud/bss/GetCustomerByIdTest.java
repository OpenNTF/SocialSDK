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
public class GetCustomerByIdTest extends BaseBssTest {

    @Test
    public void testGetCustomer() {
    	try {
    		String customerId = registerCustomer();

    		JsonEntity jsonEntity = getCustomerManagementService().getCustomerById(customerId);
			System.out.println(jsonEntity.toJsonString(false));
			Assert.assertNotNull("Unable to retrieve customer: "+customerId, jsonEntity);
				
			Assert.assertEquals(customerId, getCustomerManagementService().getCustomerId(jsonEntity.getJsonObject()));
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving customer list caused by: "+e.getMessage());    		
    	}
    }
		
}
