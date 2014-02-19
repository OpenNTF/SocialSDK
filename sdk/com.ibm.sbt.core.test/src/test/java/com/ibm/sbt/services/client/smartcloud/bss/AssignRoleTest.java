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

/**
 * @author mwallace
 *
 */
public class AssignRoleTest extends BaseBssTest {

    @Test
    public void testAssignRole() {
    	try {
    		String subscriberId = addSubscriber();
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    		System.out.println(loginName);
    		
			AuthorizationService authorizationService = getAuthorizationService();
    		String[] roles = authorizationService.getRoles(loginName);
			for (String role : roles) {
				System.out.println(role);
			}

    		authorizationService.assignRole(loginName, "CustomerAdministrator");
    		authorizationService.assignRole(loginName, "CustomerPurchaser");
    		
    		roles = authorizationService.getRoles(loginName);
			for (String role : roles) {
				System.out.println(role);
			}
			Assert.assertTrue(arrayContains("User", roles));
			Assert.assertTrue(arrayContains("CustomerAdministrator", roles));
			Assert.assertTrue(arrayContains("CustomerPurchaser", roles));

    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error retrieving roles caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error retrieving roles caused by: "+e.getMessage());    		
    	}
    }

}
