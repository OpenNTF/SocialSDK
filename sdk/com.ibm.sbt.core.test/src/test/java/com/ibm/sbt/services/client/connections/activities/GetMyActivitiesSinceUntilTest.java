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
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class GetMyActivitiesSinceUntilTest extends BaseActivityServiceTest {

	@Test
	public void testGetMyActivitiesSince() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("MyActivities", 5, 1000);
		
		Activity activity = createdActivities.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + activity.getUpdated().getTime());
		EntityList<Activity> activities = activityService.getMyActivities(params);
		System.out.println("Activities " + activities.size() + " update since: " + dateFormat.format(activity.getUpdated()));
		
		deleteActivities(createdActivities);

		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		Assert.assertEquals(3, activities.size());
	}
	
	@Test
	public void testGetMyActivitiesUntil() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("MyActivities", 5, 1000);
		
		Activity activity = createdActivities.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("until", "" + activity.getUpdated().getTime());
		EntityList<Activity> activities = activityService.getMyActivities(params);
		System.out.println("Activities " + activities.size() + " update until: " + dateFormat.format(activity.getUpdated()));
		
		deleteActivities(createdActivities);

		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		Assert.assertTrue(activities.size() > 2);
	}
	
	@Ignore
	@Test
	public void testGetMyActivitiesSinceUntil() throws ClientServicesException, XMLException {
		if (!isV5OrHigher()) return;

		List<Activity> createdActivities = createActivities("MyActivities", 5, 1000);
		
		Activity sinceActivity = createdActivities.get(1);
		Activity untilActivity = createdActivities.get(3);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + sinceActivity.getUpdated().getTime());
		params.put("until", "" + untilActivity.getUpdated().getTime());
		EntityList<Activity> activities = activityService.getMyActivities(params);
		System.out.println("Activities " + activities.size() + 
				" update since: " + dateFormat.format(sinceActivity.getUpdated()) +
				" and until: " + dateFormat.format(untilActivity.getUpdated()));
		
		deleteActivities(createdActivities);

		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		Assert.assertEquals(2, activities.size());
	}
	
}
