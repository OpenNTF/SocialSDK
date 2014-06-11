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
import com.ibm.sbt.services.client.connections.common.Person;
import com.ibm.sbt.test.lib.TestEnvironment;

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
	
	@Test(expected=ClientServicesException.class)
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
			an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), f.getFid());
		}

		// Delete the activities again
		activityService.deleteActivityNode(dstActivityNode);
		activityService.deleteActivityNode(srcActivityNode);
	}
	
	@Test(expected=ClientServicesException.class)
	public void testMoveNullDestUuid() throws ClientServicesException, XMLException {
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
		
		ActivityNode an = null;
		for (Field f : srcActivityNode.getFields()) {
			an = activityService.moveFieldToEntry(null, f.getFid(), (int) f.getPosition());
		}

		// Delete the activities again
		activityService.deleteActivityNode(dstActivityNode);
		activityService.deleteActivityNode(srcActivityNode);
	}
	
	@Test(expected=ClientServicesException.class)
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
		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), "FooBarNonExistentField");

		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
		activityService.deleteActivityNode(dstActivityNode);
	}
	
	@Test(expected=ClientServicesException.class)
	public void testMoveFieldWithWrongDestUuid() throws ClientServicesException, XMLException {
		// Create activities
		Activity srcActivity = createActivity();
		Activity destActivity = createActivity();
		
		// Create text field
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");

		
		// Populate source activity and update
		srcActivity.addField(textField);
		srcActivity.update();		
		
		activityService.updateActivity(srcActivity);
		
		// Test wrong fieldUuid
		ActivityNode an = activityService.moveFieldToEntry("FooBar", textField.getFid());

		// Delete the activities again
		activityService.deleteActivity(srcActivity);
		activityService.deleteActivity(destActivity);
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
	
		@Test
	public void testReduceFieldsToSingleNode() throws ClientServicesException, XMLException {
		// Create activities
		Activity srcActivity1 = createActivity();
		Activity srcActivity2 = createActivity();
		Activity srcActivity3 = createActivity();
		
		Activity destActivity = createActivity();
		
		ActivityNode node = new ActivityNode();
		
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
		
		// Field 5
		String name = TestEnvironment.getSecondaryUserDisplayName();
		String email = TestEnvironment.getSecondaryUserEmail();
		String userid = TestEnvironment.getSecondaryUserUuid();
		
		PersonField personField = new PersonField();
		personField.setName("test_person");
		personField.setPosition(5000);
		personField.setPerson(new Person(name, email, userid));
		
		// Populate source activity and update
		srcActivity1.addField(textField);
		srcActivity1.addField(hiddenTextField);
		srcActivity2.addField(dateField);
		srcActivity2.addField(linkField);
		srcActivity3.addField(personField);
		
		srcActivity1.update();
		srcActivity2.update();
		srcActivity3.update();
		
		// The original number of fields in the source node
		int totalNumFields = srcActivity1.getFields().length + srcActivity2.getFields().length + srcActivity3.getFields().length;
		
		ActivityNode an = null;
		
		// Move all fields from source acitivity 1
		for (Field f : srcActivity1.getFields()) {
			an = activityService.moveFieldToEntry(destActivity.getActivityUuid(), f.getFid(), (int) f.getPosition());
		}
		
		// Move all fields from source acitivity 2
		for (Field f : srcActivity2.getFields()) {
			an = activityService.moveFieldToEntry(destActivity.getActivityUuid(), f.getFid(), (int) f.getPosition());
		}
				
		// Move all fields from source acitivity 3
		for (Field f : srcActivity3.getFields()) {
			an = activityService.moveFieldToEntry(destActivity.getActivityUuid(), f.getFid(), (int) f.getPosition());
		}
		
		Assert.assertNotNull(an);
		
		Activity read = an.getActivityService().getActivity(destActivity.getActivityUuid());
		Field[] fields = read.getFields();
		
		// Check that all fields have been moved
		Assert.assertNotNull(fields);
		Assert.assertEquals(totalNumFields, fields.length);
		
		// Check date field
		Field movedDateField = read.getFieldByName(dateField.getName());
		Assert.assertTrue(movedDateField instanceof DateField);
		Assert.assertEquals("test_date", ((DateField)movedDateField).getName());
		Assert.assertEquals(1000, ((DateField)movedDateField).getPosition());
		Assert.assertNotNull(((DateField)movedDateField).getDate());
		
		// Check text field
		Field movedTextField = read.getFieldByName(textField.getName());
		Assert.assertTrue(movedTextField instanceof TextField);
		Assert.assertEquals("test_text", movedTextField);
		Assert.assertEquals(2000, ((TextField)movedTextField).getPosition());
		Assert.assertEquals("Test_Text_Field", ((TextField)movedTextField).getSummary());
		
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
		
		// Check person field
		Field movedPersonField = read.getFieldByName(personField.getName());
		Assert.assertTrue(movedPersonField instanceof PersonField);
		Assert.assertEquals("test_person", ((PersonField)movedPersonField).getName());
		Assert.assertEquals(5000, ((PersonField)movedPersonField).getPosition());
		Assert.assertEquals(name, ((PersonField)movedPersonField).getPerson().getName());
		Assert.assertEquals(userid, ((PersonField)movedPersonField).getPerson().getUserid());
		Assert.assertNotNull(((PersonField)movedPersonField).getPerson().getUserState());

		// Delete the activities again
		activityService.deleteActivity(srcActivity1);
		activityService.deleteActivity(srcActivity2);
		activityService.deleteActivity(srcActivity3);
		activityService.deleteActivity(destActivity);
	}
	
}
