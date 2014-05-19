/*
 * � Copyright IBM Corp. 2014
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
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivitiesSinceTest extends BaseActivityServiceTest {

	@Test
	public void testActivitiesSince() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("Activities", 5, 1000);
		
		Activity activity = createdActivities.get(2);
		String title = activity.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("since", "" + activity.getPublished().getTime());
		EntityList<Activity> myActivities = activityService.getMyActivities(params);
		System.out.println(myActivities.size());
		
		Assert.assertEquals(3, myActivities.size());
	}
	
	@Test
	public void testActivitiesUntil() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("Activities", 5, 1000);
		
		Activity activity = createdActivities.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("until", "" + activity.getPublished().getTime());
		EntityList<Activity> myActivities = activityService.getMyActivities(params);
		System.out.println(myActivities.size());
		
		Assert.assertEquals(2, myActivities.size());
	}
	
	@Test
	public void testActivitiesSinceUntil() throws ClientServicesException, XMLException {
		Map<String, String> params = new HashMap<String, String>();
		params.put("ps", "200");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		for (Activity activity : activities) {
			if (!activity.isDeleted()) {
				activity.delete();
			}
		}
		
		List<Activity> createdActivities = createActivities("Activities", 5, 1000);
		
		Activity sinceActivity = createdActivities.get(1);
		Activity untilActivity = createdActivities.get(3);
		
		params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("since", "" + sinceActivity.getPublished().getTime());
		params.put("until", "" + untilActivity.getPublished().getTime());
		EntityList<Activity> myActivities = activityService.getMyActivities(params);
		System.out.println(myActivities.size());
		
		Assert.assertEquals(2, myActivities.size());
	}
	
}
