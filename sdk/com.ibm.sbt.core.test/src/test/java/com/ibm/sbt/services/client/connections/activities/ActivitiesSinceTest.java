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
public class ActivitiesSinceTest extends BaseActivityServiceTest {

	@Test
	public void testActivitiesSince() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("Activities-", 10, 1000);
		
		Activity activity = createdActivities.get(5);
		String title = activity.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("since", title.substring(title.indexOf('-')+1));
		EntityList<Activity> myActivities = activityService.getMyActivities(params);
		System.out.println(myActivities.size());
		
		Assert.assertEquals(5, myActivities.size());
	}
	
	@Test
	public void testActivitiesUntil() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("Activities-", 10, 1000);
		
		Activity activity = createdActivities.get(5);
		String title = activity.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("until", title.substring(title.indexOf('-')+1));
		EntityList<Activity> myActivities = activityService.getMyActivities(params);
		System.out.println(myActivities.size());
		
		Assert.assertEquals(5, myActivities.size());
	}
	
	@Test
	public void testActivitiesSinceUntil() throws ClientServicesException, XMLException {
		List<Activity> createdActivities = createActivities("Activities-", 10, 1000);
		
		Activity sinceActivity = createdActivities.get(3);
		String sinceTitle = sinceActivity.getTitle();
		Activity untilActivity = createdActivities.get(5);
		String untilTitle = untilActivity.getTitle();
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("since", sinceTitle.substring(sinceTitle.indexOf('-')+1));
		params.put("until", untilTitle.substring(untilTitle.indexOf('-')+1));
		EntityList<Activity> myActivities = activityService.getMyActivities(params);
		System.out.println(myActivities.size());
		
		Assert.assertEquals(2, myActivities.size());
	}
	
}
