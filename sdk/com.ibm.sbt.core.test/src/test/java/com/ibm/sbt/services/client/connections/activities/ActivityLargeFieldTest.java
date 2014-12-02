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
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.activity.ActivityRequiredException;

import junit.framework.TestFailure;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityLargeFieldTest extends BaseActivityServiceTest {
	
	@Test
	public void testCreateNodesWithLargeFields() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		StringBuilder nodeContent = new StringBuilder();
		nodeContent.append("Swords Rush ~start~county=Waterford;town=Dunmore~end~ ");
		for (int i=0; i<4096; i++) {
			nodeContent.append('0');
		}

		StringBuilder fieldContent = new StringBuilder();
		fieldContent.append("Ballsbridge, Howth ~start~town=Westport;county=Mayo~end~ ");
		for (int i=0; i<4096; i++) {
			fieldContent.append('0');
		}
		
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setContent(nodeContent.toString());
		TextField textField = new TextField();
		textField.setName("large_field");
		textField.setPosition(1000);
		textField.setSummary(fieldContent.toString());
		textField.setHidden(true); // only hidden text fields can have more than 512 bytes
		activity.addField(textField);

		Activity createdActivity = activityService.createActivity(activity);
		
		this.activity = null;
		
		Activity readActivity = activityService.getActivity(createdActivity.getActivityUuid());
		
		System.out.println("CREATED: " + createdActivity.toXmlString());
		System.out.println("READ: " + readActivity.toXmlString());
		
		System.out.println("BYTES: " + ((TextField)readActivity.getFieldByName("large_field")).getSummary().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + ((TextField)readActivity.getFieldByName("large_field")).getSummary().length());
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(createdActivity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		activityNode.setContent(nodeContent.toString());
		activityNode.addField(textField);

		ActivityNode createdNode = activityService.createActivityNode(activityNode);
		
		ActivityNode readNode = activityService.getActivityNode(createdNode.getActivityNodeUuid());
		
		System.out.println("CREATED: " + createdNode.toXmlString());
		System.out.println("READ: " + readNode.toXmlString());
		
		System.out.println("BYTES: " + ((TextField)readNode.getFieldByName("large_field")).getSummary().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + ((TextField)readNode.getFieldByName("large_field")).getSummary().length());
		
	}
	
	@Test(expected=ClientServicesException.class)
	public void testCreateLargeFieldFails() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		StringBuilder content = new StringBuilder();
		for (int i=0; i<4096; i++) {
			content.append('0');
		}
		
		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityNodeTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		TextField textField = new TextField();
		textField.setName("large_field");
		textField.setPosition(1000);
		textField.setSummary(content.toString());
		activityNode.addField(textField);

		activityService.createActivityNode(activityNode);
	}

	@Test
	public void testCreateLargeFields() throws ClientServicesException, XMLException, IOException {
		InputStream stream = readFile("latin.txt");
		String content = IOUtils.toString(stream);
		System.out.println("CONTENT SIZE: "+content.length());
		
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setContent("Activity-"+System.currentTimeMillis());
		TextField textField = new TextField();
		textField.setName("large_field");
		textField.setPosition(1000);
		textField.setSummary(content);
		textField.setHidden(true); // only hidden text fields can have more than 512 bytes
		activity.addField(textField);

		activityService.createActivity(activity);
		System.out.println(activity.toXmlString());
		
		this.activity = null;
		
		ActivityNode activityEntry = new ActivityNode();
		activityEntry.setActivityUuid(activity.getActivityUuid());
		activityEntry.setTitle("Entry");
		activityEntry.setType(ActivityNode.TYPE_ENTRY);
		activityEntry.setContent(content);
		activityEntry.addField(textField);

		activityService.createActivityNode(activityEntry);
		System.out.println(activityEntry.toXmlString());
		
		ActivityNode activityReply = new ActivityNode();
		activityReply.setActivityUuid(activity.getActivityUuid());
		activityReply.setInReplyTo(activityEntry);
		activityReply.setTitle("Reply");
		activityReply.setType(ActivityNode.TYPE_ENTRY);
		activityReply.setContent(content);
		activityReply.addField(textField);

		activityService.createActivityNode(activityReply);
		System.out.println(activityReply.toXmlString());
		
	}
	

	@Test
	public void testLargeFieldMyActivities() throws ClientServicesException, XMLException, IOException {
		InputStream stream = readFile("latin.txt");
		String content = IOUtils.toString(stream);
		System.out.println("Content Size: "+content.length());
		
		Activity activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity.setContent("Activity-"+System.currentTimeMillis());
		TextField textField = new TextField();
		textField.setName("large_field");
		textField.setPosition(1000);
		textField.setSummary(content);
		textField.setHidden(true); // only hidden text fields can have more than 512 bytes
		activity.addField(textField);

		activityService.createActivity(activity);
		System.out.println("Created Activity");
		System.out.println(activity.toXmlString());
		
		Field[] fields = activity.getFields();
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertNotNull(((TextField)fields[0]).getLink());
		
		this.activity = null;

		Map<String, String> params = new HashMap<String, String>();
		params.put("includeEnclosureLink", "true");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		System.out.println("Get My Activities First Activity");
		System.out.println(activities.get(0).toXmlString());
		
		fields = activities.get(0).getFields();
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertNotNull(((TextField)fields[0]).getLink());
		
	}

}
