/*
 * (C) Copyright IBM Corp. 2014
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
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Link;

/**
 * @author mwallace
 *
 */
public class ActivityFeedEnhTest extends BaseActivityServiceTest {
	
	@Rule public ExpectedException thrown= ExpectedException.none();

	@Test
	public void testMoveField() throws ClientServicesException, XMLException {
		// Create activity nodes
		
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode srcActivityNode = new ActivityNode();
		srcActivityNode.setActivityUuid(activity.getActivityUuid());
		srcActivityNode.setTitle("Source ActivityNode");
		srcActivityNode.setType("ENTRY");

		ActivityNode dstActivityNode = createActivityNode();
		dstActivityNode.setActivityUuid(activity.getActivityUuid());
		dstActivityNode.setTitle("Source ActivityNode");
		dstActivityNode.setType("ENTRY");

		// Create fields
		
		// Field 1
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");
		
		// Field 2 
		Date date = new Date();
		date.setTime(1397650699000L);
		DateField dateField = new DateField();
		dateField.setName("test_date");
		dateField.setPosition(2000);
		dateField.setDate(date);
		
		// Field 3
		TextField hiddenTextField = new TextField();
		hiddenTextField.setName("test_hidden_text");
		hiddenTextField.setPosition(3000);
		hiddenTextField.setSummary("Hidden_Text_Field");
		hiddenTextField.setHidden(true);
		
		// Field 4
		LinkField linkField = new LinkField();
		linkField.setName("test_link");
		linkField.setPosition(4000);
		linkField.setLink(new Link("IBM", "http://www.ibm.com"));
		
		// Populate source activity and update
		srcActivityNode.addField(textField);
		srcActivityNode.addField(hiddenTextField);
		srcActivityNode.addField(dateField);
		srcActivityNode.addField(linkField);

		activityService.createActivityNode(srcActivityNode);
		activityService.createActivityNode(dstActivityNode);
		
		// The original number of fields in the source node
		int orgNumFields = srcActivityNode.getFields().length;
		
		ActivityNode an = null;
		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());
		
		// Move all fields
		for (Field f : srcActivityNode.getFields()) {
			an = activityService.moveFieldToEntry(dstActivityNode.getActivityNodeUuid(), f.getFid());
		}
		
		Assert.assertNotNull(an);
		
		ActivityNode read = activityService.getActivityNode(dstActivityNode.getActivityNodeUuid());
		Field[] fields = read.getFields();
		
		// Check that all fields have been moved
		Assert.assertNotNull(fields);
		Assert.assertEquals(orgNumFields, fields.length);
		
		// Check text field
		Field movedTextField = read.getFieldByName(textField.getName());
		Assert.assertTrue(movedTextField instanceof TextField);
		Assert.assertEquals("test_text", movedTextField.getName());
		Assert.assertEquals(1000, ((TextField)movedTextField).getPosition());
		Assert.assertEquals("Test_Text_Field", ((TextField)movedTextField).getSummary());

		// Check date field
		Field movedDateField = read.getFieldByName(dateField.getName());
		Assert.assertTrue(movedDateField instanceof DateField);
		Assert.assertEquals("test_date", ((DateField)movedDateField).getName());
		Assert.assertEquals(2000, ((DateField)movedDateField).getPosition());
		Assert.assertNotNull(((DateField)movedDateField).getDate());
		
		// Check hidden text field
		Field movedHiddenTextField = read.getFieldByName(hiddenTextField.getName());
		Assert.assertTrue(movedHiddenTextField instanceof TextField);
		Assert.assertTrue(((TextField)movedHiddenTextField).isHidden());
		Assert.assertEquals("test_hidden_text", ((TextField)movedHiddenTextField).getName());
		Assert.assertEquals(3000, ((TextField)movedHiddenTextField).getPosition());
		Assert.assertEquals("Hidden_Text_Field", ((TextField)movedHiddenTextField).getSummary());
		
		// Check link field
		Field movedLinkField = read.getFieldByName(linkField.getName());
		Assert.assertTrue(movedLinkField instanceof LinkField);
		Assert.assertEquals("test_link", ((LinkField)movedLinkField).getName());
		Assert.assertEquals(4000, ((LinkField)movedLinkField).getPosition());
		Assert.assertEquals("IBM", ((LinkField)movedLinkField).getLink().getTitle());
		Assert.assertEquals("http://www.ibm.com", ((LinkField)movedLinkField).getLink().getHref());
		
		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
		activityService.deleteActivityNode(dstActivityNode);
		activityService.deleteActivity(activity);
	}

	@Test
	public void testReduceFieldsToSingleNode() throws ClientServicesException, XMLException {
		// Create activities
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode srcActivityNode1 = new ActivityNode();
		srcActivityNode1.setActivityUuid(activity.getActivityUuid());
		srcActivityNode1.setTitle("Source ActivityNode 1");
		srcActivityNode1.setType("ENTRY");

		ActivityNode srcActivityNode2 = new ActivityNode();
		srcActivityNode2.setActivityUuid(activity.getActivityUuid());
		srcActivityNode2.setTitle("Source ActivityNode 2");
		srcActivityNode2.setType("ENTRY");

		ActivityNode srcActivityNode3 = new ActivityNode();
		srcActivityNode3.setActivityUuid(activity.getActivityUuid());
		srcActivityNode3.setTitle("Source ActivityNode 3");
		srcActivityNode3.setType("ENTRY");

		ActivityNode destActivityNode = new ActivityNode();
		destActivityNode.setActivityUuid(activity.getActivityUuid());
		destActivityNode.setTitle("Destination ActivityNode");
		destActivityNode.setType("ENTRY");
		
		// Field 1
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setSummary("Test_Text_Field");
		textField.setPosition(1000);
		
		// Field 2 
		Date date = new Date();
		date.setTime(1397650699000L);
		DateField dateField = new DateField();
		dateField.setName("test_date");
		dateField.setDate(date);
		dateField.setPosition(2000);
		
		// Field 3
		TextField hiddenTextField = new TextField();
		hiddenTextField.setName("test_hidden_text");
		hiddenTextField.setSummary("Hidden_Text_Field");
		hiddenTextField.setHidden(true);
		hiddenTextField.setPosition(3000);
		
		// Populate source activity and update
		srcActivityNode1.addField(textField);
		srcActivityNode2.addField(hiddenTextField);
		srcActivityNode3.addField(dateField);
		srcActivityNode1 = activityService.createActivityNode(srcActivityNode1);
		srcActivityNode2 = activityService.createActivityNode(srcActivityNode2);
		srcActivityNode3 = activityService.createActivityNode(srcActivityNode3);
		destActivityNode = activityService.createActivityNode(destActivityNode);
		
		// The original number of fields in the source node
		int totalNumFields = srcActivityNode1.getFields().length + srcActivityNode2.getFields().length + srcActivityNode3.getFields().length;
		
		ActivityNode an = null;
		
		srcActivityNode1 = activityService.getActivityNode(srcActivityNode1.getActivityNodeUuid());
		srcActivityNode2 = activityService.getActivityNode(srcActivityNode2.getActivityNodeUuid());
		srcActivityNode3 = activityService.getActivityNode(srcActivityNode3.getActivityNodeUuid());

		// Move all fields from source acitivity 1
		for (Field f : srcActivityNode1.getFields()) {
			an = activityService.moveFieldToEntry(destActivityNode.getActivityNodeUuid(), f.getFid());
		}
		
		// Move all fields from source acitivity 2
		for (Field f : srcActivityNode2.getFields()) {
			an = activityService.moveFieldToEntry(destActivityNode.getActivityNodeUuid(), f.getFid());
		}
				
		// Move all fields from source acitivity 3
		for (Field f : srcActivityNode3.getFields()) {
			an = activityService.moveFieldToEntry(destActivityNode.getActivityNodeUuid(), f.getFid());
		}
		
		Assert.assertNotNull(an);
		
		ActivityNode read = activityService.getActivityNode(destActivityNode.getActivityNodeUuid());
		Field[] fields = read.getFields();
		
		// Check that all fields have been moved
		Assert.assertNotNull(fields);
		Assert.assertEquals(totalNumFields, fields.length);
		
		// Check text field
		Field movedTextField = read.getFieldByName(textField.getName());
		Assert.assertTrue(movedTextField instanceof TextField);
		Assert.assertEquals("test_text", movedTextField.getName());
		Assert.assertEquals(1000, ((TextField)movedTextField).getPosition());
		Assert.assertEquals("Test_Text_Field", ((TextField)movedTextField).getSummary());
		
		// Check hidden text field
		Field movedHiddenTextField = read.getFieldByName(hiddenTextField.getName());
		Assert.assertTrue(movedHiddenTextField instanceof TextField);
		Assert.assertTrue(((TextField)movedHiddenTextField).isHidden());
		Assert.assertEquals("test_hidden_text", ((TextField)movedHiddenTextField).getName());
		Assert.assertEquals(2000, ((TextField)movedHiddenTextField).getPosition());
		Assert.assertEquals("Hidden_Text_Field", ((TextField)movedHiddenTextField).getSummary());
		
		// Check date field
		Field movedDateField = read.getFieldByName(dateField.getName());
		Assert.assertTrue(movedDateField instanceof DateField);
		Assert.assertEquals("test_date", ((DateField)movedDateField).getName());
		Assert.assertEquals(3000, ((DateField)movedDateField).getPosition());
		Assert.assertNotNull(((DateField)movedDateField).getDate());
		
		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode1);
		activityService.deleteActivityNode(srcActivityNode2);
		activityService.deleteActivityNode(srcActivityNode3);
		activityService.deleteActivityNode(destActivityNode);
	}
	
	@Test
	public void testMoveNonexistentField() throws ClientServicesException, XMLException {
		// Create activities
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode srcActivityNode = new ActivityNode();
		srcActivityNode.setActivityUuid(activity.getActivityUuid());
		srcActivityNode.setTitle("Source ActivityNode");
		srcActivityNode.setType("ENTRY");
		
		ActivityNode dstActivityNode = createActivityNode();
		dstActivityNode.setActivityUuid(activity.getActivityUuid());
		dstActivityNode.setTitle("Source ActivityNode");
		dstActivityNode.setType("ENTRY");

		//We crate the activityNode before adding the field and don't update on purpose
		activityService.createActivityNode(srcActivityNode);
		activityService.createActivityNode(dstActivityNode);

		// Create text field
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");
		
		// Populate source activity
		srcActivityNode.addField(textField);
		
		ActivityNode an = null;
		for (Field f : srcActivityNode.getFields()) {
			thrown.expect(ClientServicesException.class);
			an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), f.getFid());
		}

		// Delete the activities again
		activityService.deleteActivityNode(dstActivityNode);
		activityService.deleteActivityNode(srcActivityNode);
	}
	
	@Test
	public void testMoveNullDestUuid() throws ClientServicesException, XMLException {
		// Create activities
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode srcActivityNode = new ActivityNode();
		srcActivityNode.setActivityUuid(activity.getActivityUuid());
		srcActivityNode.setTitle("Source ActivityNode");
		srcActivityNode.setType("ENTRY");
		
		ActivityNode dstActivityNode = new ActivityNode();
		dstActivityNode.setActivityUuid(activity.getActivityUuid());
		dstActivityNode.setTitle("Destination ActivityNode");
		dstActivityNode.setType("ENTRY");
		
		// Create text field
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");

		
		// Populate source activity and update
		srcActivityNode.addField(textField);
		srcActivityNode = activityService.createActivityNode(srcActivityNode);
		dstActivityNode = activityService.createActivityNode(dstActivityNode);
		
		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());
		
		ActivityNode an = null;
		for (Field f : srcActivityNode.getFields()) {
			thrown.expect(ClientServicesException.class);
			an = activityService.moveFieldToEntry(null, f.getFid());
		}

		// Delete the activities again
		activityService.deleteActivityNode(dstActivityNode);
		activityService.deleteActivityNode(srcActivityNode);
	}
	
	@Test
	public void testMoveFieldWithWrongFieldUuid() throws ClientServicesException, XMLException {
		// Create activities
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode srcActivityNode = new ActivityNode();
		srcActivityNode.setActivityUuid(activity.getActivityUuid());
		srcActivityNode.setTitle("Source ActivityNode");
		srcActivityNode.setType("ENTRY");
		
		ActivityNode dstActivityNode = createActivityNode();
		dstActivityNode.setActivityUuid(activity.getActivityUuid());
		dstActivityNode.setTitle("Source ActivityNode");
		dstActivityNode.setType("ENTRY");
		
		// Create text field
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");

		
		// Populate source activity and update
		srcActivityNode.addField(textField);
		activityService.createActivityNode(srcActivityNode);
		activityService.createActivityNode(dstActivityNode);
		
		// Test wrong fieldUuid
		thrown.expect(ClientServicesException.class);
		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), "FooBarNonExistentField");

		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
		activityService.deleteActivityNode(dstActivityNode);
	}
	
	@Test
	public void testMoveFieldWithWrongDestUuid() throws ClientServicesException, XMLException {
		// Create activities
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode srcActivityNode = new ActivityNode();
		srcActivityNode.setActivityUuid(activity.getActivityUuid());
		srcActivityNode.setTitle("Source ActivityNode");
		srcActivityNode.setType("ENTRY");
		
		ActivityNode dstActivityNode = new ActivityNode();
		dstActivityNode.setActivityUuid(activity.getActivityUuid());
		dstActivityNode.setTitle("Destination ActivityNode");
		dstActivityNode.setType("ENTRY");
		
		// Create text field
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");

		
		// Populate source activity and update
		srcActivityNode.addField(textField);
		
		srcActivityNode = activityService.createActivityNode(srcActivityNode);
		dstActivityNode = activityService.createActivityNode(dstActivityNode);
		
		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());

		
		// Test wrong fieldUuid
		thrown.expect(ClientServicesException.class);
		ActivityNode an = activityService.moveFieldToEntry("FooBar", textField.getFid());

		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
		activityService.deleteActivityNode(dstActivityNode);
	}
	
	@Test
	public void testIncludeCompletedActivities() throws ClientServicesException, XMLException {
		Activity openActivity = createActivity();
		
		Activity completedActivity = createActivity();
		
		completedActivity.setCompleted(true);
		activityService.updateActivity(completedActivity);
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("includeCommunityActivities", "no");
		params.put("page", "1");
		params.put("ps", "50");
		
		EntityList<Activity> openActivities = activityService.getMyActivities(params);
		Assert.assertNotNull("Expected non null activities", openActivities);
		params.put("completed", "yes");
		EntityList<Activity> allActivities = activityService.getMyActivities(params);
		
		activityService.deleteActivity(openActivity);

		Assert.assertTrue(containsActivity(openActivities, openActivity));
		Assert.assertTrue(!containsActivity(openActivities, completedActivity));
		
		Assert.assertNotNull("Expected non null activities", allActivities);
		Assert.assertTrue(containsActivity(allActivities, openActivity));
		Assert.assertTrue(containsActivity(allActivities, completedActivity));
	}
	
}
