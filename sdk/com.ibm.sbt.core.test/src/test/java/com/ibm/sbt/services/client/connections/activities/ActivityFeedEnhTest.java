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
public class ActivityFeedEnhTest extends BaseActivityServiceTest {

	@Test
	public void testIncludeCompletedActivities() throws ClientServicesException, XMLException {
		Activity openActivity = createActivity();
		
		Activity completedActivity = createActivity();
		//System.out.println(completedActivity.toXmlString());
		completedActivity.setCompleted(true);
		activityService.updateActivity(completedActivity);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("includeCommunityActivities", "no");
		params.put("page", "1");
		params.put("ps", "50");
		EntityList<Activity> openActivities = activityService.getMyActivities(params);
		Assert.assertNotNull("Expected non null activities", openActivities);
		params.put("completed", "yes");
		EntityList<Activity> allActivities = activityService.getMyActivities(params);
		
		activityService.deleteActivity(openActivity);

		Assert.assertTrue(containsActivity(openActivities, openActivity));
		Assert.assertTrue(!containsActivity(openActivities, completedActivity));
		
		Assert.assertNotNull("Expected non null activities", allActivities);
		Assert.assertTrue(containsActivity(allActivities, openActivity));
		Assert.assertTrue(containsActivity(allActivities, completedActivity));
	}
	
}
