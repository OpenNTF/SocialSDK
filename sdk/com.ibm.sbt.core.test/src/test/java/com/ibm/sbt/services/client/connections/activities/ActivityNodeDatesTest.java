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

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class ActivityNodeDatesTest extends BaseActivityServiceTest {

	@Test
	public void testReadActivityDates() throws ClientServicesException, XMLException {
		Date creationTime = new Date();
		
		Activity created = createActivity();
		
		Date updateTime = new Date();
		created.setContent("Some updated content");
		created.update();
		
		Activity read = activityService.getActivity(activity.getActivityUuid());
		Date published = read.getPublished();
		Date updated = read.getUpdated();
		
		System.out.println("Creation Time: " + dateFormat.format(creationTime));
		System.out.println("Update Time: " + dateFormat.format(updateTime));
		System.out.println("Published: " + dateFormat.format(published));
		System.out.println("Updated: " + dateFormat.format(updated));
		
		System.out.println(read.toXmlString());
	}

}
