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

import java.text.SimpleDateFormat;
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
public class ActivityDescendantsSinceTest extends BaseActivityServiceTest {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	

	@Test
	public void testDescendantsUpdatedSince() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedSince-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode node = nodes.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + node.getUpdated().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes " + activityNodes.size() + " update since: " + dateFormat.format(node.getUpdated()));
		
		Assert.assertEquals(3, activityNodes.size());
	}
	
	@Test
	public void testDescendantsUpdatedUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedUntil-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode node = nodes.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("until", "" + node.getUpdated().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes " + activityNodes.size() + " update until: " + dateFormat.format(node.getUpdated()));
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
	@Test
	public void testDescendantsUpdatedSinceUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedSinceUntil-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode sinceNode = nodes.get(1);
		ActivityNode untilNode = nodes.get(3);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + sinceNode.getUpdated().getTime());
		params.put("until", "" + untilNode.getUpdated().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes " + activityNodes.size() + 
				" update since: " + dateFormat.format(sinceNode.getUpdated()) +
				" until: " + dateFormat.format(untilNode.getUpdated()));
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
	@Test
	public void testDescendantsCreatedSince() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsCreatedSince-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode node = nodes.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + node.getPublished().getTime());
		params.put("rangeId", "created");
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes " + activityNodes.size() + " create since: " + dateFormat.format(node.getPublished()));
		
		Assert.assertEquals(3, activityNodes.size());
	}
	
	@Test
	public void testDescendantsCreatedUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsCreatedUntil-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode node = nodes.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("until", "" + node.getPublished().getTime());
		params.put("rangeId", "created");
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes " + activityNodes.size() + " create until: " + dateFormat.format(node.getPublished()));
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
	@Test
	public void testDescendantsCreatedSinceUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsCreatedSinceUntil-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode sinceNode = nodes.get(3);
		ActivityNode untilNode = nodes.get(1);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + sinceNode.getPublished().getTime());
		params.put("until", "" + untilNode.getPublished().getTime());
		params.put("rangeId", "created");
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes " + activityNodes.size() + 
				" create since: " + dateFormat.format(sinceNode.getPublished()) +
				" until: " + dateFormat.format(untilNode.getPublished()));
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
}
