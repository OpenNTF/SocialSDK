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
 * 
 * @author mwallace
 */
public class GetCustomerWorkflowsTest extends BaseBssTest {

    @Test
    public void testGetCustomerWorkflows() {
    	try {
			String customerId = System.getProperty("customerId");
			
   			EntityList<JsonEntity> workflowList = getWorkflowService().getWorkflowsByCustomerId(customerId, 1, 100);
			System.out.println("Customer: "+customerId);
			for (JsonEntity workflow : workflowList) {
				System.out.println(workflow.toJsonString());
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving workflow list caused by: "+e.getMessage());    		
    	}
    }
			
}
