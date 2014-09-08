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
public class GetWorkflowsTest extends BaseBssTest {

    @Test
    public void testGetCompletedWorkflows() {
    	try {
			EntityList<JsonEntity> workflowList = getWorkflowService().getWorkflowsByStatus(WorkflowService.STATUS_COMPLETED, 1, 50);
			System.out.println("Completed");
			for (JsonEntity workflow : workflowList) {
				System.out.println(workflow.toJsonString());
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving workflow list caused by: "+e.getMessage());    		
    	}
    }
		
    @Test
    public void testGetFailedWorkflows() {
    	try {
			EntityList<JsonEntity> workflowList = getWorkflowService().getWorkflowsByStatus(WorkflowService.STATUS_FAILED, 1, 50);
			System.out.println("Failed");
			for (JsonEntity workflow : workflowList) {
				System.out.println(workflow.toJsonString());
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving workflow list caused by: "+e.getMessage());    		
    	}
    }
		
    @Test
    public void testGetDeferredWorkflows() {
    	try {
			EntityList<JsonEntity> workflowList = getWorkflowService().getWorkflowsByStatus(WorkflowService.STATUS_DEFERRED, 1, 50);
			System.out.println("Deferred");
			for (JsonEntity workflow : workflowList) {
				System.out.println(workflow.toJsonString());
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving workflow list caused by: "+e.getMessage());    		
    	}
    }
		
    @Test
    public void testGetCustomerWorkflows() {
    	try {
    		EntityList<JsonEntity> customerList = getCustomerManagementService().getCustomers();
    		if (!customerList.isEmpty()) {
    			JsonEntity customer = customerList.get(0);
    			long id = customer.getAsLong("Id");
    			
        		
        		String customerId = "" + id;
    			EntityList<JsonEntity> workflowList = getWorkflowService().getWorkflowsByCustomerId(customerId, 1, 50);
    			System.out.println("Customer: "+customerId);
    			for (JsonEntity workflow : workflowList) {
    				System.out.println(workflow.toJsonString());
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving workflow list caused by: "+e.getMessage());    		
    	}
    }
		
    @Test
    public void testGetSubscriberWorkflows() {
    	try {
    		EntityList<JsonEntity> subscriberList = getSubscriberManagementService().getSubscribers();
    		if (!subscriberList.isEmpty()) {
    			JsonEntity subscriber = subscriberList.get(0);
    			long id = subscriber.getAsLong("Id");
    			
        		
        		String subscriberId = "" + id;
    			EntityList<JsonEntity> workflowList = getWorkflowService().getWorkflowsBySubscriberId(subscriberId, 1, 50);
    			System.out.println("Subscriber: "+subscriberId);
    			for (JsonEntity workflow : workflowList) {
    				System.out.println(workflow.toJsonString());
    			}
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving workflow list caused by: "+e.getMessage());    		
    	}
    }
		
	
}
