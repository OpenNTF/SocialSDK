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

import org.junit.Assert;
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
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		
		ByteArrayInputStream fileContent = new ByteArrayInputStream("MyFileContent".getBytes());
		
		activity.uploadFile("MyFile", fileContent, "text/plain");
	}
	
	@Test
	public void testCreateActivityNodeFiles() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		
		ByteArrayInputStream fileContent = new ByteArrayInputStream("MyFileContent".getBytes());
		
		activity.uploadFile("MyFile1", fileContent, "text/plain");
		activity.uploadFile("MyFile2", fileContent, "text/plain");
		activity.uploadFile("MyFile3", fileContent, "text/plain");
		activity.uploadFile("MyFile4", fileContent, "text/plain");
		activity.uploadFile("MyFile5", fileContent, "text/plain");
	}
	
}
