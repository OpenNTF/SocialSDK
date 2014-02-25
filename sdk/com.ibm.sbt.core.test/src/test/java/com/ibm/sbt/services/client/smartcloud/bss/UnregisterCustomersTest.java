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
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class UnregisterCustomersTest extends BaseBssTest {

    @Test
    public void testUnregisterCustomers() {
    	try {
    		CustomerManagementService customerManagement = getCustomerManagementService();
    		
    		EntityList<JsonEntity> customerList = customerManagement.getCustomers();
			for (JsonEntity customer : customerList) {
				String customerId = "" + customer.getAsLong("Id");
				String customerType = customer.getAsString("CustomerType");
				if (!"RESELLER".equals(customerType)) {
					try {
						
						System.out.println("Unregistering: "+customerId);
						customerManagement.unregisterCustomer(customerId);
						
						JsonEntity jsonEntity = customerManagement.getCustomerById(customerId);
						Assert.assertNull("Able to retrieve unregistered customer: "+customerId, jsonEntity);
						System.out.println("Unregistered: "+customerId);
					} catch (BssException be) {
			    		JsonJavaObject jsonObject = be.getResponseJson();
			    		System.out.println(jsonObject);
			    		Assert.fail("Error unregistering customer caused by: "+jsonObject);
			    	} 
				}
			}
			
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error unregistering customers caused by: "+e.getMessage());    		
    	}
    }
		
}
