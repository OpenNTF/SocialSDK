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
public class GetSubscribersByIdTest extends BaseBssTest {

	@Test
	public void testGetCustomer() {
		try {
			String customerId = registerCustomer();
			for (int i = 0; i < 5; i++) {
				addSubscriber();
			}

			EntityList<JsonEntity> subscribers = getSubscriberManagementService().getSubscribersById(customerId);
			Assert.assertEquals("Unable to retrieve subscribers", 6, subscribers.size());
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Error retrieving subscribers caused by: " + e.getMessage());
		}
	}

}
