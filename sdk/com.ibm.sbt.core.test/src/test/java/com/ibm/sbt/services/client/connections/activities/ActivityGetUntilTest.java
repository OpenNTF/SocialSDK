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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
public class ActivityGetUntilTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testGetAllActivitiesUntil() throws ClientServicesException {
		Date now = new Date();
				
		Activity activity = createActivity();
		activity.setCompleted(true);
		activity.update();
		
		long start = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("until", "" + now.getTime());
		EntityList<Activity> activities = activityService.getAllActivities(params);
		Assert.assertNotNull("Expected non null activities", activities);
		Assert.assertFalse("Expected non empty activities", activities.isEmpty());
		long duration = System.currentTimeMillis()-start;
		System.out.println("Read all activities took: "+duration+"(ms)");
		Assert.assertFalse("Expected get all activities would take less than 15secs", duration > 15000);
		activityService.deleteActivity(activity);
	}

}
