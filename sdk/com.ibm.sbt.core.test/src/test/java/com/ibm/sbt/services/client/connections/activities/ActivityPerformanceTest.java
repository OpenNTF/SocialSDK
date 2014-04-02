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
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityPerformanceTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivities() throws ClientServicesException {
		EntityList<Activity> activities = activityService.getMyActivities();
		int totalResults = activities.getTotalResults();
		//System.out.println(DOMUtil.getXMLString((Node)activities.getData()));
		System.out.println("Total number of activities: "+totalResults);
		if (totalResults < 100) {
			int create = 100 - totalResults;
			System.out.println("Creating "+create+" activities");
			for (int i=0; i<create; i++) {
				long start = System.currentTimeMillis();
				Activity activity = createActivity();
				long duration = System.currentTimeMillis() - start;
				System.out.println("Creating "+activity.getActivityUuid()+" took "+duration+"(ms)");
			}
			// prevent last activity being auto-deleted
			activity = null;
		}
		
		long start = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", "1");
		params.put("ps", "1");
		params.put("nodetype", "activity");
		activities = activityService.getAllActivities(params);
		long duration = System.currentTimeMillis() - start;
		System.out.println("Reading all activities feed took "+duration+"(ms)");
		
		start = System.currentTimeMillis();
		params.put("tag", "personal");
		activities = activityService.getAllActivities(params);
		duration = System.currentTimeMillis() - start;
		System.out.println("Reading all activities feed (with tag=personal) took "+duration+"(ms)");
		
		start = System.currentTimeMillis();
		activities = activityService.getMyActivities(params);
		duration = System.currentTimeMillis() - start;
		System.out.println("Reading my activities feed (with tag=personal) took "+duration+"(ms)");
	}
	
	@Test
	@Ignore
	public void testGetMyActivities() throws ClientServicesException {
		for (int i=0; i<10; i++) {
			long start = System.currentTimeMillis();
			Map<String, String> params = new HashMap<String, String>();
			params.put("page", "1");
			params.put("ps", "1");
			params.put("tag", "personal");
			params.put("nodetype", "activity");
			EntityList<Activity> activities = activityService.getMyActivities(params);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Reading "+activities.size()+" took "+duration+"(ms)");
		}		
	}
	
	@Test
	@Ignore
	public void testGetAllActivities() throws ClientServicesException {
		for (int i=0; i<10; i++) {
			long start = System.currentTimeMillis();
			Map<String, String> params = new HashMap<String, String>();
			params.put("page", "1");
			params.put("ps", "1");
			params.put("tag", "personal");
			params.put("nodetype", "activity");
			EntityList<Activity> activities = activityService.getAllActivities(params);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Reading "+activities.size()+" took "+duration+"(ms)");
		}		
	}
	
}
