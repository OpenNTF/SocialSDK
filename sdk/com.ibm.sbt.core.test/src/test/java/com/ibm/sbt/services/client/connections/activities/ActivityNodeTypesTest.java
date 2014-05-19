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
public class ActivityNodeTypesTest extends BaseActivityServiceTest {

	@Test
	public void testCreateTodoNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_TODO);
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_TODO, created.getType());
	}
		
	@Test
	public void testCreateSectionNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_SECTION);
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_SECTION, created.getType());
	}
		
	@Test
	public void testCreateReplyNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		
		activityService.createActivityNode(activityNode);
		
		
		ActivityNode activityReply = new ActivityNode();
		activityReply.setActivityUuid(activity.getActivityUuid());
		activityReply.setTitle(createActivityNodeTitle());
		activityReply.setType(ActivityNode.TYPE_REPLY);
		activityReply.setInReplyTo(activityNode);
		
		ActivityNode created = activityService.createActivityNode(activityReply);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityReply == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_REPLY, created.getType());
	}
	
	@Test
	public void testCreateEntryTemplateNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY_TEMPLATE);
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_ENTRY_TEMPLATE, created.getType());
	}
	
	@Test
	public void testCreateEntryNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_ENTRY, created.getType());
	}
	
	@Test
	public void testCreateChatNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_CHAT);
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_CHAT, created.getType());
	}
	
	@Test
	public void testCreateEmailNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_EMAIL);
		
		ActivityNode created = activityService.createActivityNode(activityNode);
		
		//System.out.println(created.toXmlString());
		
		Assert.assertTrue("Invalid activity node instance", activityNode == created);
		Assert.assertEquals("Invalid activity node id", activity.getActivityUuid(), created.getActivityUuid());
		Assert.assertEquals("Invalid activity node type", ActivityNode.TYPE_EMAIL, created.getType());
	}
	
	
}
