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

	@Test
	public void testDescendantsSince() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsSince-"+start);
		String activityUuid = activity.getActivityUuid();
		//System.out.println(activity.toXmlString());
		
		List<ActivityNode> createdNodes = createActivityDescendants(activityUuid, 10, 1000);
		
		ActivityNode activityNode = createdNodes.get(5);
		String title = activityNode.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", title.substring(title.indexOf('-')+1));
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activityUuid, params);
		//System.out.println(activityNodes.size());
		
		Assert.assertEquals(5, activityNodes.size());
	}
	
	@Test
	public void testDescendantsUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsSince-"+start);
		String activityUuid = activity.getActivityUuid();
		//System.out.println(activity.toXmlString());
		
		List<ActivityNode> createdNodes = createActivityDescendants(activityUuid, 10, 1000);
		
		ActivityNode activityNode = createdNodes.get(5);
		String title = activityNode.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("until", title.substring(title.indexOf('-')+1));
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activityUuid, params);
		//System.out.println(activityNodes.size());
		
		Assert.assertEquals(5, activityNodes.size());
	}
	
	@Test
	public void testDescendantsSinceUntil() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsSince-"+start);
		String activityUuid = activity.getActivityUuid();
		//System.out.println(activity.toXmlString());
		
		List<ActivityNode> createdNodes = createActivityDescendants(activityUuid, 10, 1000);
		
		ActivityNode sinceNode = createdNodes.get(3);
		String since = sinceNode.getTitle();
		ActivityNode untilNode = createdNodes.get(7);
		String until = untilNode.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("since", since.substring(since.indexOf('-')+1));
		params.put("until", until.substring(until.indexOf('-')+1));
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activityUuid, params);
		//System.out.println(activityNodes.size());
		
		Assert.assertEquals(4, activityNodes.size());
	}
	
}
