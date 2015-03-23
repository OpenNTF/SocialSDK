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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class ActivityNodeCrudrTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivityNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//String createdXml = created.toXmlString();
		//System.out.println("CREATED: " + createdXml);
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), activityNode.getActivityUuid());
		Assert.assertNotNull("Invalid activity node id", activityNode.getId());
		Assert.assertNotNull("Invalid activity node id", activityNode.getActivityNodeUuid());
		Assert.assertNotNull("Invalid activity node edit url", activityNode.getEditUrl());		
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testReadActivityNode() throws ClientServicesException, XMLException {
		ActivityNode created = createActivityNode();
		
		ActivityNode read = activityService.getActivityNode(created.getActivityNodeUuid());
		
		String createdXml = created.toXmlString();
		String readXml = read.toXmlString();
		
		System.out.println("CREATED: " + createdXml);
		System.out.println("READ: " + readXml);
		
		Assert.assertEquals("Invalid activity node", createdXml, readXml);
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testUpdateActivityNode() throws ClientServicesException, XMLException {
		ActivityNode created = createActivityNode();
		
		ActivityNode read = activityService.getActivityNode(created.getActivityNodeUuid());
		
		read.setTitle(createActivityNodeTitle());
		activityService.updateActivityNode(read);
		
		ActivityNode updated = activityService.getActivityNode(created.getActivityNodeUuid());
		
		Assert.assertEquals(unRandomize(read.getTitle().replace("#-", "")), unRandomize(updated.getTitle().replace("#-", "")));
		
		read = activityService.getActivityNode(created.getActivityNodeUuid());
		
		String updatedXml = updated.toXmlString();
		String readXml = read.toXmlString();
		
		System.out.println("UPDATED: " + updatedXml);
		System.out.println("READ: " + readXml);
		
		Assert.assertEquals("Invalid activity node", updatedXml, readXml);
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testDeleteActivityNode() throws ClientServicesException, XMLException {
		ActivityNode created = createActivityNode();
		String createdNodeUuid = created.getActivityNodeUuid();
		
		String activityNodeUuid = activityService.deleteActivityNode(created);
		
		Assert.assertEquals(createdNodeUuid, activityNodeUuid);
		
		ActivityNode read = activityService.getActivityNode(activityNodeUuid);
		Assert.assertTrue(read.isDeleted());
	}
	
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testRestoreActivityNode() throws ClientServicesException, XMLException {
		ActivityNode created = createActivityNode();
		
		String activityNodeUuid = activityService.deleteActivityNode(created);
		Assert.assertEquals(created.getActivityNodeUuid(), activityNodeUuid);
		
		activityService.restoreActivityNode(created);
		
		ActivityNode read = activityService.getActivityNode(created.getActivityNodeUuid());

		Assert.assertEquals(created.getActivityNodeUuid(), read.getActivityNodeUuid());
		Assert.assertEquals(unRandomize(created.getTitle()), unRandomize(read.getTitle()));

		//System.out.println("RESTORED: " + restored.toXmlString());
		//System.out.println("READ: " + read.toXmlString());
	}
	
}
