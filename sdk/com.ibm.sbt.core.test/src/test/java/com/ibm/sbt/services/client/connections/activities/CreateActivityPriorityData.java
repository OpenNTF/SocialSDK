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
package com.ibm.sbt.services.client.connections.activities;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class CreateActivityPriorityData extends BaseActivityServiceTest {

	@Test
	public void createActivityPriorityData() throws ClientServicesException, XMLException {
		int[] priorities = new int[] { -1, 1, 500, 999, 1000, 1001, 1500, 1999, 2000, 2001, 2500, 2999, 3000 };
		for (int i=0; i<priorities.length; i++) {
			Activity created = createActivity(createTitle(priorities[i]));
			
			created.changePriority(priorities[i]);
			
			Activity read = activityService.getActivity(created.getActivityUuid());
			System.out.println(priorities[i]+"="+read.getPriority());
		}
		activity = null;
	}
	
    protected String createTitle(int priority) {
    	return "ActivityPriority" + priority + " - " + System.currentTimeMillis();
    }	
}
