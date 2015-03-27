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

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityLargeContentTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivity() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		StringBuilder content = new StringBuilder();
		for (int i=0; i<4096; i++) {
			content.append(i).append('-');
		}
		
		System.out.println("BYTES: " + content.toString().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + content.toString().length());

		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setContent(content.toString());

		Activity created = activityService.createActivity(activity);
		
		Activity read = activityService.getActivity(created.getActivityUuid());
		Assert.assertNotNull(read.getEnclosureLink());
		Assert.assertEquals(content.toString().length(), read.getEnclosureLink().getLength());
		Assert.assertEquals(1520, read.getSummary().getBytes("UTF-8").length);
		System.out.println("READ: " + read.toXmlString());
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("includeEnclosureLink", "true");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		Assert.assertNotNull(activities.get(0).getEnclosureLink());
		Assert.assertEquals(content.toString().length(), activities.get(0).getEnclosureLink().getLength());
		Assert.assertEquals(1520, activities.get(0).getSummary().getBytes("UTF-8").length);
		System.out.println("READ: " + activities.get(0).toXmlString());
	}
	
}
