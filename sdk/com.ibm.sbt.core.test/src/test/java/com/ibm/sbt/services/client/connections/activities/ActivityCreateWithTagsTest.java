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
public class ActivityCreateWithTagsTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityWithTags() throws ClientServicesException, XMLException {
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setTags(tags);
		
		Activity created = activityService.createActivity(activity);
		
		System.out.println("CREATED: " + created.toXmlString());
		
		Assert.assertNotNull("Invalid activity id", created.getId());
		Assert.assertNotNull("Invalid activity edit url", created.getEditUrl());
		Assert.assertEquals("Invalid activity tags", 3, created.getTags().size());
	}

}
