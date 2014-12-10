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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityPriorityTest extends BaseActivityServiceTest {

	@Test
	public void testTunedOutActivity() throws ClientServicesException {
		Activity created = createActivity(createTitle(Activity.PRIORITY_MEDIUM), Activity.PRIORITY_MEDIUM);
		
		created.changePriority(Activity.PRIORITY_TUNED_OUT);
		
		Activity read = activityService.getActivity(activity.getActivityUuid());
		Assert.assertEquals(activity.getUpdated().getTime(), read.getUpdated().getTime());
		Assert.assertEquals(Activity.PRIORITY_TUNED_OUT, read.getPriority());
		
		EntityList<Activity> activities = activityService.getTunedOutActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		Assert.assertNotNull(created.getActivityUuid(), activities.get(0).getId());
		
		activityService.deleteActivity(created);
	}
	
	@Test
	public void testNegativePriorityActivity() throws ClientServicesException {
		Activity created = createActivity(createTitle(Activity.PRIORITY_MEDIUM), Activity.PRIORITY_MEDIUM);
		
		created.changePriority(-1);
		
		Activity read = activityService.getActivity(activity.getActivityUuid());
		Assert.assertEquals(activity.getUpdated().getTime(), read.getUpdated().getTime());
		Assert.assertEquals(-1, read.getPriority());
		
		EntityList<Activity> activities = activityService.getTunedOutActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		Assert.assertNotNull(created.getActivityUuid(), activities.get(0).getId());
		
		activityService.deleteActivity(created);
	}
	
    protected String createTitle(int priority) {
    	return "ActivityPriority" + priority + " - " + System.currentTimeMillis();
    }	
}
