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
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class ActivityCrudrTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivity() throws ClientServicesException, XMLException {
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setTags(tags);
		
		Activity created = activityService.createActivity(activity);
		
		//System.out.println("CREATED: " + created.toXmlString());
		
		Assert.assertTrue("Invalid activity instance", activity == created);
		Assert.assertNotNull("Invalid activity id", activity.getId());
		Assert.assertNotNull("Invalid activity edit url", activity.getEditUrl());
	}
	
	@Test
	public void testReadActivity() throws ClientServicesException, XMLException {
		Activity created = createActivity();
		
		Activity read = activityService.getActivity(created.getActivityUuid());
		
		//System.out.println("CREATED: " + created.toXmlString());
		//System.out.println("READ: " + read.toXmlString());
		
		Assert.assertEquals(created.getActivityUuid(), read.getActivityUuid());
		Assert.assertEquals(created.getTitle(), read.getTitle());
	}
	
	@Test
	public void testUpdateActivity() throws ClientServicesException, XMLException {
		Activity created = createActivity();
		
		Activity read = activityService.getActivity(created.getActivityUuid());
		//System.out.println("READ: " + read.toXmlString());
		
		//read.setTitle(createActivityTitle());
		//read.setCompleted(true);
		activityService.updateActivity(read);
		
		Activity updated = activityService.getActivity(created.getActivityUuid());
		
		Assert.assertEquals(read.getTitle(), updated.getTitle());
		//Assert.assertEquals(true, updated.isCompleted());
				
		//System.out.println("UPDATED: " + updated.toXmlString());
		//System.out.println("READ: " + read.toXmlString());
	}
	
	@Test
	public void testDeleteActivity() throws ClientServicesException, XMLException {
		Activity created = createActivity();
		
		String acctivityUuid = activityService.deleteActivity(created);
		
		Assert.assertEquals(created.getActivityUuid(), acctivityUuid);
		
		try {
			Activity read = activityService.getActivity(created.getActivityUuid());
			Assert.assertNull(read);
		} catch (ClientServicesException cse) { 
			// expected
		}
	}
	
	@Test
	public void testRestoreActivity() throws ClientServicesException, XMLException {
		Activity created = createActivity();
		
		String activityUuid = activityService.deleteActivity(created);
		Assert.assertEquals(created.getActivityUuid(), activityUuid);
		
		activityService.restoreActivity(created);
		
		Activity read = activityService.getActivity(created.getActivityUuid());

		Assert.assertEquals(created.getActivityUuid(), read.getActivityUuid());
		Assert.assertEquals(created.getTitle(), read.getTitle());

		//System.out.println("RESTORED: " + restored.toXmlString());
		//System.out.println("READ: " + read.toXmlString());
	}
	

}
