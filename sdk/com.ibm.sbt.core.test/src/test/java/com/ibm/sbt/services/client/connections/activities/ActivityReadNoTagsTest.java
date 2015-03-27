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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.base.datahandlers.XmlDataHandler;

/**
 * @author mwallace
 *
 */
public class ActivityReadNoTagsTest extends BaseActivityServiceTest {
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivity() throws ClientServicesException, XMLException {
		
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activityService.createActivity(activity);
		
		System.out.println(activity.getTags().size());
		//System.out.println(activity.toXmlString());

		List<String> no_tags = new ArrayList<String>();

		List<String> tags = new ArrayList<String>();
    	tags.add("user=1234567890");		

		activity.setTags(tags);
		activityService.updateActivity(activity);

		System.out.println(activity.getTags().size());
		//System.out.println(activity.toXmlString());

		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("page", "1");
		params.put("priority", "all");
		params.put("tag", "user=1234567890");
		params.put("ps", "1");
		params.put("completed", "yes");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		System.out.println(activities.get(0).getTags().size());
		
		createActivityNode(activity.getActivityUuid(), "^^^^^^");
		
		activities = activityService.getMyActivities(params);
		System.out.println(activities.get(0).getTags().size());
		Assert.assertEquals("Invalid activity count", 1, activities.size());

		createActivityNode(activity.getActivityUuid(), "^^^^^^");
		
		activities = activityService.getMyActivities(params);
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		System.out.println(activities.get(0).getTags().size());		

		tags.add("user=1234567890");
		activity.setTags(tags);
		activityService.updateActivity(activity);
		
		activities = activityService.getMyActivities(params);
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		System.out.println(activities.get(0).getTags().size());		
		
		createActivityNode(activity.getActivityUuid(), "^^^^^^");
		
		activities = activityService.getMyActivities(params);
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		System.out.println(activities.get(0).getTags().size());		

		EntityList<ActivityUpdate> updates = activityService.getHistory(activity.getActivityUuid());
		for (ActivityUpdate update : updates) {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(update.getType());
			System.out.println(update.getTitle());
			System.out.println(update.getAuthor().getName());
			System.out.println(XmlDataHandler.dateFormat.format(update.getUpdated()));
		}

		/*
    	testTags(activity, tags);
    	removeTags(activity);
    	testTags(activity, tags);
    	removeTags(activity);
    	testTags(activity, tags);
    	removeTags(activity);
    	testTags(activity, tags);
    	*/
	}
	
	protected void testTags(Activity activity, List<String> tags) throws ClientServicesException {
		activity.setTags(tags);
		activityService.updateActivity(activity);

		System.out.println(activity.getTags().size());
		//System.out.println(activity.toXmlString());

		//https://apps.na.collabservtest.lotus.com/activities/service/atom2/activities?nodetype=activity&page=1&priority=all&tag=user%3D20133257&ps=1&completed=yes
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("page", "1");
		params.put("priority", "all");
		params.put("tag", "user=1234567890");
		params.put("ps", "1");
		params.put("completed", "yes");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		
		String activityUuid = activities.get(0).getActivityUuid();
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		//System.out.println(activityUuid);
		System.out.println(activities.get(0).getTags().size());
		//System.out.println(activities.get(0).toXmlString());
		
		//Assert.assertTrue("Invalid activity tags", activities.get(0).getTags().size() > 0);
		
		Activity read = activityService.getActivity(activityUuid);
		System.out.println(read.getTags().size());
	}
	
	protected void removeTags(Activity activity) throws ClientServicesException {
		List<String> tags = new ArrayList<String>();
		activity.setTags(tags);
		activityService.updateActivity(activity);
		//System.out.println(activity.getTags().size());
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testReadActivity() throws ClientServicesException, XMLException {
		//https://apps.na.collabservtest.lotus.com/activities/service/atom2/activities?nodetype=activity&page=1&priority=all&tag=user%3D20133257&ps=1&completed=yes
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("page", "1");
		params.put("priority", "all");
		params.put("tag", "user=20133257");
		params.put("ps", "1");
		params.put("completed", "yes");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		
		String activityUuid = activities.get(0).getActivityUuid();
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		System.out.println(activityUuid);
		System.out.println(activities.get(0).getTags().size());
		System.out.println(activities.get(0).toXmlString());
		
		Assert.assertTrue("Invalid activity tags", activities.get(0).getTags().size() > 0);
		
		Activity read = activityService.getActivity(activityUuid);
		System.out.println(read.getTags().size());
		System.out.println(read.toXmlString());
		
		EntityList<ActivityUpdate> updates = activityService.getHistory(activityUuid);
		for (ActivityUpdate update : updates) {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(update.getType());
			System.out.println(update.getTitle());
			System.out.println(update.getAuthor().getName());
			System.out.println(XmlDataHandler.dateFormat.format(update.getUpdated()));
		}
		
		System.out.println("\n\n");
		
		EntityList<ActivityNode> descendants = activityService.getActivityDescendants(activityUuid);
		for (ActivityNode descendant : descendants) {
			System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------");
			System.out.println(descendant.getType());
			System.out.println(descendant.getTitle());
		}

		/*
		int page = 1;
		params = new HashMap<String, String>();
		params.put("page", "" +  page);
		params.put("ps", "100");
		activities = activityService.getMyActivities(params);
		int totalResults = activities.getTotalResults();
		int readCount = activities.size();
		while (readCount < totalResults) {
			for (Activity activity : activities) {
				if (activity.getActivityUuid().equals(activityUuid)) {
					System.out.println(activity.getTags().size());
					System.out.println(activity.toXmlString());
				}
			}
			params.put("page", "" +  ++page);
			activities = activityService.getMyActivities(params);
			totalResults = activities.getTotalResults();
			readCount += activities.size();
		}
		for (Activity activity : activities) {
			if (activity.getActivityUuid().equals(activityUuid)) {
				System.out.println(activity.getTags().size());
				System.out.println(activity.toXmlString());
			}
		}
		
		page = 1;
		params = new HashMap<String, String>();
		params.put("page", "" +  page);
		params.put("ps", "100");
		activities = activityService.getAllActivities(params);
		totalResults = activities.getTotalResults();
		readCount = activities.size();
		while (readCount < totalResults) {
			for (Activity activity : activities) {
				if (activity.getActivityUuid().equals(activityUuid) && 
					"activity".equals(activity.getType())) {
					System.out.println(activity.getTags().size());
					System.out.println(activity.toXmlString());
				}
			}
			params.put("page", "" +  ++page);
			activities = activityService.getAllActivities(params);
			totalResults = activities.getTotalResults();
			readCount += activities.size();
		}
		for (Activity activity : activities) {
			if (activity.getActivityUuid().equals(activityUuid) && 
				"activity".equals(activity.getType())) {
				System.out.println(activity.getTags().size());
				System.out.println(activity.toXmlString());
			}
		}
		*/
	}

}
