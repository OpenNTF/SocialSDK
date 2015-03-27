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

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Tag;

/**
 * @author mwallace
 *
 */
public class ActivityFeedTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testGetMyActivities() throws ClientServicesException {
		Activity created = createActivity();
		EntityList<Activity> activities = activityService.getMyActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		for (Activity activity : activities) {
			Assert.assertNotNull("Invalid activity id", activity.getId());
		}
		activityService.deleteActivity(created);
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testGetCompletedActivities() throws ClientServicesException {
		Activity activity = createActivity();
		activity.setCompleted(true);
		activityService.updateActivity(activity);
		
		EntityList<Activity> activities = activityService.getCompletedActivities();
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		for (Activity nextActivity : activities) {
			Assert.assertNotNull("Invalid activity id", nextActivity.getId());
		}
		activityService.deleteActivity(activity);
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testGetToDoActivities() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		createActivityNode(activity.getActivityUuid(), createActivityNodeTitle(), ActivityNode.TYPE_TODO);
		//System.out.println(todoNode.toXmlString());
		EntityList<ActivityNode> activityNodes = activityService.getToDos();
		Assert.assertNotNull("Expected non null activity nodes", activityNodes);
		Assert.assertFalse("Expected non empty activity nodes", activityNodes.isEmpty());
		for (ActivityNode activityNode : activityNodes) {
			Assert.assertNotNull("Invalid activity id", activityNode.getId());
		}
		activityService.deleteActivity(activity);
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testGetActivityTags() throws ClientServicesException {
		Activity activity = createActivity();
		EntityList<Tag> tags = activityService.getActivityTags();
		Assert.assertNotNull("Expected non null tags", tags);
		Assert.assertFalse("Expected non empty tags", tags.isEmpty());
		for (Tag tag : tags) {
			Assert.assertNotNull("Invalid tag term", tag.getTerm());
		}
		activityService.deleteActivity(activity);
	}
	
}
