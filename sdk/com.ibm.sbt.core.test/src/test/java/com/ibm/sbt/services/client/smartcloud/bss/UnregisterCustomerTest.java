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
public class UnregisterCustomerTest extends BaseBssTest {

    @Test
    public void testUnregisterCustomer() {
    	try {
    		BigInteger customerId = registerCustomer();

    		CustomerManagementService customerManagement = getCustomerManagementService();
    		
    		JsonEntity jsonEntity = customerManagement.getCustomerById(customerId);
			Assert.assertNotNull("Unable to retrieve customer: "+customerId, jsonEntity);
			Assert.assertEquals(customerId, customerManagement.getCustomerId(jsonEntity.getJsonObject()));

			customerManagement.unregisterCustomer(customerId);
			
			try {
				jsonEntity = customerManagement.getCustomerById(customerId);
				Assert.assertNull("Able to retrieve unregistered customer: "+customerId, jsonEntity);				
			} catch (BssException cme) {
				String responseCode = cme.getResponseCode();
				Assert.assertEquals("404", responseCode);
				
				setCustomerId(null);
			}
    	} catch (BssException cme) {
    		JsonJavaObject jsonObject = cme.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error unregistering customer caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error unregistering customer caused by: "+e.getMessage());    		
    	}
    }
		
}
