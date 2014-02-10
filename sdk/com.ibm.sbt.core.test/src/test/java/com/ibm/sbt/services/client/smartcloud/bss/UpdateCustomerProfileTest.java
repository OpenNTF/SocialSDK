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
public class UpdateCustomerProfileTest extends BaseBssTest {

    @Test
    public void testUpdateCustomerProfile() {
    	try {
    		BigInteger customerId = registerCustomer();

    		CustomerManagementService customerManagement = getCustomerManagementService();
    		
    		JsonEntity jsonEntity = customerManagement.getCustomerById(customerId);
			Assert.assertNotNull("Unable to retrieve customer: "+customerId, jsonEntity);
			Assert.assertEquals(customerId, customerManagement.getCustomerId(jsonEntity.getJsonObject()));

			JsonJavaObject rootObject = jsonEntity.getJsonObject();
			Assert.assertNotNull("Unable to retrieve customer: "+customerId, rootObject);
			
			System.out.println(rootObject);
			JsonJavaObject customerObject = rootObject.getAsObject("Customer");
			JsonJavaObject organizationObject = customerObject.getAsObject("Organization");
			organizationObject.putString("Phone", "888-888-8888");
			JsonJavaObject contactObject = organizationObject.getAsObject("Contact");
			contactObject.putString("WorkPhone", "800-666-1234");
			
			customerManagement.updateCustomerProfile(rootObject);
			
			jsonEntity = customerManagement.getCustomerById(customerId);
			Assert.assertNotNull("Unable to retrieve customer: "+customerId, jsonEntity);
			Assert.assertEquals(customerId, customerManagement.getCustomerId(jsonEntity.getJsonObject()));
			
			rootObject = jsonEntity.getJsonObject();
			Assert.assertNotNull("Unable to retrieve customer: "+customerId, customerObject);
			
			System.out.println(rootObject);
			customerObject = rootObject.getAsObject("Customer");
			organizationObject = customerObject.getAsObject("Organization");
			Assert.assertEquals("888-888-8888", organizationObject.getAsString("Phone"));
			contactObject = organizationObject.getAsObject("Contact");
			Assert.assertEquals("800-666-1234", contactObject.getAsString("WorkPhone"));
				
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error updating customer profile caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error updating customer profile caused by: "+e.getMessage());    		
    	}
    }
		
}
