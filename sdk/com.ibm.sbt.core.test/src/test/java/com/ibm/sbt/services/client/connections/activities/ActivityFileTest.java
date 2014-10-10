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

import java.io.ByteArrayInputStream;
import java.util.Arrays;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class ActivityFileTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityNodeFile() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		this.activity = null; // prevent deletion
				
		byte[] bytes = new byte[Integer.valueOf(1024)];
		Arrays.fill(bytes, (byte)0);
		ByteArrayInputStream fileContent = new ByteArrayInputStream(bytes);
		activity.uploadFile("MyFile", fileContent, "text/plain");
		
		activity = activityService.getActivity(activity.getActivityUuid());
		
		System.out.println(activity.toXmlString());
	}
	
}
