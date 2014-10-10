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

/**
 * @author mwallace
 *
 */
public class ActivityCreatePerformanceTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityWithTags() throws ClientServicesException, XMLException {
		EntityList<Activity> activities = activityService.getMyActivities();
		int totalResults = activities.getTotalResults();
		//System.out.println(DOMUtil.getXMLString((Node)activities.getData()));
		System.out.println("Total number of activities: "+totalResults);
		System.out.println("Creating "+5+" activities");
		for (int i=0; i<5; i++) {
			long start = System.currentTimeMillis();
			Activity activity = createActivity();
			long duration = System.currentTimeMillis() - start;
			System.out.println("Creating "+activity.getActivityUuid()+" took "+duration+"(ms)");
		}
		// prevent last activity being auto-deleted
		activity = null;
		
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

}
