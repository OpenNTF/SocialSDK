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
public class ActivityNodeFileTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityNodeFile() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		this.activity = null;
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		
		ByteArrayInputStream fileContent = new ByteArrayInputStream("MyFileContent".getBytes());
		
		//ActivityNode created = activityService.createActivityNode(activityNode, "myFile", fileContent, "text/plain");
		
		//System.out.println(created.toXmlString());
		
		//Assert.assertTrue("Invalid activity node instance", activityNode == created);
		//Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), activityNode.getActivityUuid());
		//Assert.assertNotNull("Invalid activity node id", activityNode.getId());
		//Assert.assertNotNull("Invalid activity node id", activityNode.getActivityNodeUuid());
		//Assert.assertNotNull("Invalid activity node edit url", activityNode.getEditUrl());		
	}
	
}
