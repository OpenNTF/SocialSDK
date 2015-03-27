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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class DeleteAllActivitiesTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void deleteAllActivities() throws ClientServicesException, XMLException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ps", "100");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		System.out.println("Deleting "+activities.getTotalResults()+" activities.");
		while (activities.getTotalResults() > 0) {
			for (Activity activity : activities) {
				if (!activity.isDeleted()) {
					try {
						activity.delete();
						System.out.println("Deleted: "+activity.getActivityUuid());
					} catch (Exception e) {
					}
				}
			}
			activities = activityService.getMyActivities(params);
		}
	}
	
}
