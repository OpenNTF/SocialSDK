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
 * @author mwallace
 *
 */
public class DeleteCustomersTest extends BaseBssTest {

    @Test
    public void testDeleteAbeIndustrial() {
    	try {
			EntityList<JsonEntity> customerList = getCustomerManagementService().getCustomers(1, 100);
			for (JsonEntity customer : customerList) {
				String orgName = customer.getJsonObject().getJsonObject("Organization").getAsString("OrgName");
				if ("Abe Industrial".equals(orgName)) {
					String id = String.valueOf(customer.getJsonObject().getAsLong("Id"));
					String customerState = customer.getJsonObject().getAsString("CustomerState");
					try {
						if ("ACTIVE".equals(customerState)) {
							getCustomerManagementService().unregisterCustomer(id);
							System.out.println("Deleted: "+id);
						}
					} catch (BssException bsse) {
						System.out.println("Delete failed because: "+bsse.getResponseMessage());
					} catch (Exception e) {
						System.out.println("Delete failed because: "+e.getMessage());
					}
				}
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving customer list caused by: "+e.getMessage());    		
    	}
    }
		
}
