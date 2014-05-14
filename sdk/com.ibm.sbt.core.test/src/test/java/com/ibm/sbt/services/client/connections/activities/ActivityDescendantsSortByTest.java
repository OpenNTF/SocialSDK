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

import java.text.SimpleDateFormat;
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
	
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");	

	@Test
	public void testDescendantsUpdatedSince() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		activity = createActivity("ActivityDescendantsUpdatedSince-"+start);
		
		List<ActivityNode> nodes = createActivityDescendants(activity);
				
		Map<String, String> params = new HashMap<String, String>();
		params.put("sortBy", "modified");
		params.put("sortOrder", "asc");
		EntityList<ActivityNode> activityDescendants = activityService.getActivityNodeDescendants(activity.getActivityUuid(), params);
		System.out.println("\nActivity descendants sorted ascending by modification date");
		for (ActivityNode node : activityDescendants) {
			System.out.println(node.getTitle() + "  modified: " + dateFormat.format(node.getUpdated()));
		}
		
		params.put("sortOrder", "desc");
		EntityList<ActivityNode> activityChildren = activityService.getActivityNodeChildren(activity.getActivityUuid(), params);
		System.out.println("\nActivity shildren sorted descending by modification date");
		for (ActivityNode node : activityChildren) {
			System.out.println(node.getTitle() + " modified: " + dateFormat.format(node.getUpdated()));
		}
	}
		
}
