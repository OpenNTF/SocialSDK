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
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityDescendantsSortByTest extends BaseActivityServiceTest {
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendantsModifiedSort() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsModifiedSort-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		org.junit.Assert.assertNotNull(nodes);
				
		Map<String, String> params = new HashMap<String, String>();
		params.put("sortBy", "modified");
		params.put("sortOrder", "asc");
		EntityList<ActivityNode> activityDescendants = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("\nActivity descendants sorted ascending by modification date");
		for (ActivityNode node : activityDescendants) {
			System.out.println(node.getTitle() + "  modified: " + dateFormat.format(node.getUpdated()));
		}
		
		params.put("sortOrder", "desc");
		EntityList<ActivityNode> activityChildren = activityService.getActivityNodeChildren(activity.getActivityUuid(), params);
		System.out.println("\nActivity children sorted descending by modification date");
		for (ActivityNode node : activityChildren) {
			System.out.println(node.getTitle() + " modified: " + dateFormat.format(node.getUpdated()));
		}
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendantsTitleModifiedSort() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsModifiedSort-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		org.junit.Assert.assertNotNull(nodes);
				
		Map<String, String> params = new HashMap<String, String>();
		params.put("sortfields", "title,modified");
		params.put("sortorder", "desc,asc");
		EntityList<ActivityNode> activityDescendants = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("\nActivity descendants sorted descending by title and ascending by modification date");
		for (ActivityNode node : activityDescendants) {
			System.out.println(node.getTitle() + "  modified: " + dateFormat.format(node.getUpdated()));
		}
		
		params.put("sortorder", "asc,desc");
		EntityList<ActivityNode> activityChildren = activityService.getActivityNodeChildren(activity.getActivityUuid(), params);
		System.out.println("\nActivity children sorted ascending by title and descending by modification date");
		for (ActivityNode node : activityChildren) {
			System.out.println(node.getTitle() + " modified: " + dateFormat.format(node.getUpdated()));
		}
	}

	/*
	@Test
	public void testDescendantsLastModSort() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsLastModSort-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		org.junit.Assert.assertNotNull(nodes);
				
		Map<String, String> params = new HashMap<String, String>();
		params.put("sortBy", "LASTMOD");
		params.put("sortOrder", "asc");
		EntityList<ActivityNode> activityDescendants = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("\nActivity descendants sorted ascending by LASTMOD date");
		for (ActivityNode node : activityDescendants) {
			System.out.println(node.getTitle() + "  modified: " + dateFormat.format(node.getUpdated()));
		}
		
		params.put("sortOrder", "desc");
		EntityList<ActivityNode> activityChildren = activityService.getActivityNodeChildren(activity.getActivityUuid(), params);
		System.out.println("\nActivity children sorted descending by LASTMOD date");
		for (ActivityNode node : activityChildren) {
			System.out.println(node.getTitle() + " modified: " + dateFormat.format(node.getUpdated()));
		}
	}
	*/
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendantsCreatedSort() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsCreatedSort-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		org.junit.Assert.assertNotNull(nodes);
				
		Map<String, String> params = new HashMap<String, String>();
		params.put("sortBy", "created");
		params.put("sortOrder", "asc");
		EntityList<ActivityNode> activityDescendants = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("\nActivity descendants sorted ascending by created date");
		for (ActivityNode node : activityDescendants) {
			System.out.println(node.getTitle() + " created: " + dateFormat.format(node.getPublished()));
		}
		
		params.put("sortOrder", "desc");
		EntityList<ActivityNode> activityChildren = activityService.getActivityNodeChildren(activity.getActivityUuid(), params);
		System.out.println("\nActivity shildren sorted descending by created date");
		for (ActivityNode node : activityChildren) {
			System.out.println(node.getTitle() + " created: " + dateFormat.format(node.getPublished()));
		}
	}
		
}
