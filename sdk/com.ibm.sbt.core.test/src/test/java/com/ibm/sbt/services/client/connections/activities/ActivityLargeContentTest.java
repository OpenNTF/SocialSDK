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

import java.io.UnsupportedEncodingException;
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
public class ActivityLargeContentTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivity() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		StringBuilder content = new StringBuilder();
		for (int i=0; i<4096; i++) {
			content.append(i).append('-');
		}
		
		System.out.println("BYTES: " + content.toString().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + content.toString().length());

		/*
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setContent(content.toString());

		Activity created = activityService.createActivity(activity);
		
		Activity read = activityService.getActivity(created.getActivityUuid());
		
		System.out.println("CREATED: " + created.toXmlString());
		System.out.println("READ: " + read.toXmlString());
		
		System.out.println("BYTES: " + read.getSummary().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + read.getSummary().length());
		*/
	}
	

}
