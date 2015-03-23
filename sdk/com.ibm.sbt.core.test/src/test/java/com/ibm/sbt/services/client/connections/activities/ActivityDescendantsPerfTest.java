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
public class ActivityDescendantsPerfTest extends BaseActivityServiceTest {
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");	

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendants() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedSince-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		Map<String, String> params = new HashMap<String, String>();
		EntityList<ActivityNode> activityNodes = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		dumpNodes(activityNodes);
		
		Assert.assertEquals(5, activityNodes.size());
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendantsUpdatedSince() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedSince-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode node = nodes.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + node.getUpdated().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes:" + activityNodes.size() + " updated since: " + dateFormat.format(node.getUpdated()));
		dumpNodes(activityNodes);
		
		Assert.assertEquals(3, activityNodes.size());
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendantsUpdatedUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedUntil-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode node = nodes.get(2);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("until", "" + node.getUpdated().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes:" + activityNodes.size() + " updated until: " + dateFormat.format(node.getUpdated()));
		dumpNodes(activityNodes);
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDescendantsUpdatedSinceUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedSinceUntil-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
		
		ActivityNode sinceNode = nodes.get(1);
		ActivityNode untilNode = nodes.get(3);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", "" + sinceNode.getUpdated().getTime());
		params.put("until", "" + untilNode.getUpdated().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes:" + activityNodes.size() + 
				" updated since: " + dateFormat.format(sinceNode.getUpdated()) +
				" until: " + dateFormat.format(untilNode.getUpdated()));
		dumpNodes(activityNodes);
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
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
		System.out.println("since="+sinceNode.getPublished().getTime());
		System.out.println("until="+untilNode.getPublished().getTime());
		EntityList<ActivityNode> activityNodes = activityService.getActivityDescendants(activity.getActivityUuid(), params);
		System.out.println("Nodes:" + activityNodes.size() + 
				" created since: " + dateFormat.format(sinceNode.getPublished()) +
				" until: " + dateFormat.format(untilNode.getPublished()));
		dumpNodes(activityNodes);
		
		activity = null;
		
		Assert.assertEquals(2, activityNodes.size());
	}
	
}
