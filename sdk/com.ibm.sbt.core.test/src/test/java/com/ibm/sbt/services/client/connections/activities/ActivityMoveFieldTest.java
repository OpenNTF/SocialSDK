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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Link;
import com.ibm.sbt.services.client.connections.common.Person;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author Carlos Manias
 *
 */
public class ActivityMoveFieldTest extends BaseActivityServiceTest {
	
	@Rule public ExpectedException thrown= ExpectedException.none();

	@Test
	public void testMovePersonField() throws ClientServicesException, XMLException {
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

		// Field 1
		
		String name = TestEnvironment.getSecondaryUserDisplayName();
		String email = TestEnvironment.getSecondaryUserEmail();
		String userid = TestEnvironment.getSecondaryUserUuid();
		
		PersonField personField = new PersonField();
		personField.setName("test_person");
		personField.setPosition(1000);
		personField.setPerson(new Person(name, email, userid));

		srcActivityNode.addField(personField);

		activityService.createActivityNode(srcActivityNode);
		activityService.createActivityNode(dstActivityNode);

		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());
		Field f = srcActivityNode.getFields()[0];
		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityNodeUuid(), f.getFid());

		ActivityNode read = activityService.getActivityNode(dstActivityNode.getActivityNodeUuid());
		PersonField readField = (PersonField)read.getFields()[0];
		Person readPerson = readField.getPerson();
		assertEquals("test_person", readField.getName());
		assertEquals(name, readPerson.getName());
		assertEquals(userid, readPerson.getUserid());
	}
	
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

		ActivityNode dstActivityNode = new ActivityNode();
		dstActivityNode.setActivityUuid(activity.getActivityUuid());
		dstActivityNode.setTitle("Destination ActivityNode");
		dstActivityNode.setType("REPLY");

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
		dstActivityNode.setInReplyTo(srcActivityNode);
		activityService.createActivityNode(dstActivityNode);
		
		// The original number of fields in the source node
		int orgNumFields = srcActivityNode.getFields().length;
		
		ActivityNode an = null;
		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());
		
		// Move all fields
		int numFields = 0;
		for (Field f : srcActivityNode.getFields()) {
			an = activityService.moveFieldToEntry(dstActivityNode.getActivityNodeUuid(), f.getFid());
			assertNotNull(an);
			assertEquals(++numFields, an.getFields().length);
		}
		
		//Check descendants
		EntityList<ActivityNode> nodeList = activityService.getActivityNodeDescendants(activity.getActivityUuid());
		for (ActivityNode node : nodeList){
			assertNotNull(node);
			if (node.getActivityNodeUuid().equals(srcActivityNode.getActivityNodeUuid())){
				assertEquals(0, node.getFields().length);
			} else if (node.getActivityNodeUuid().equals(dstActivityNode.getActivityNodeUuid())){
				assertEquals(orgNumFields, node.getFields().length);
			}
		}
		
		ActivityNode read = activityService.getActivityNode(dstActivityNode.getActivityNodeUuid());
		Field[] fields = read.getFields();
		
		// Check that all fields have been moved
		assertNotNull(fields);
		assertEquals(orgNumFields, fields.length);
		
		// Check text field
		Field movedTextField = read.getFieldByName(textField.getName());
		assertTrue(movedTextField instanceof TextField);
		assertEquals("test_text", movedTextField.getName());
		assertEquals(1000, ((TextField)movedTextField).getPosition());
		assertEquals("Test_Text_Field", ((TextField)movedTextField).getSummary());

		// Check date field
		Field movedDateField = read.getFieldByName(dateField.getName());
		assertTrue(movedDateField instanceof DateField);
		assertEquals("test_date", ((DateField)movedDateField).getName());
		assertEquals(2000, ((DateField)movedDateField).getPosition());
		assertNotNull(((DateField)movedDateField).getDate());
		
		// Check hidden text field
		Field movedHiddenTextField = read.getFieldByName(hiddenTextField.getName());
		assertTrue(movedHiddenTextField instanceof TextField);
		assertTrue(((TextField)movedHiddenTextField).isHidden());
		assertEquals("test_hidden_text", ((TextField)movedHiddenTextField).getName());
		assertEquals(3000, ((TextField)movedHiddenTextField).getPosition());
		assertEquals("Hidden_Text_Field", ((TextField)movedHiddenTextField).getSummary());
		
		// Check link field
		Field movedLinkField = read.getFieldByName(linkField.getName());
		assertTrue(movedLinkField instanceof LinkField);
		assertEquals("test_link", ((LinkField)movedLinkField).getName());
		assertEquals(4000, ((LinkField)movedLinkField).getPosition());
		assertEquals("IBM", ((LinkField)movedLinkField).getLink().getTitle());
		assertEquals("http://www.ibm.com", ((LinkField)movedLinkField).getLink().getHref());
		
		activityService.deleteActivityNode(dstActivityNode);
		activityService.deleteActivityNode(srcActivityNode);
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
		
		assertNotNull(an);
		
		ActivityNode read = activityService.getActivityNode(destActivityNode.getActivityNodeUuid());
		Field[] fields = read.getFields();
		
		// Check that all fields have been moved
		assertNotNull(fields);
		assertEquals(totalNumFields, fields.length);
		
		// Check text field
		Field movedTextField = read.getFieldByName(textField.getName());
		assertTrue(movedTextField instanceof TextField);
		assertEquals("test_text", movedTextField.getName());
		assertEquals(1000, ((TextField)movedTextField).getPosition());
		assertEquals("Test_Text_Field", ((TextField)movedTextField).getSummary());

		// Check date field
		Field movedDateField = read.getFieldByName(dateField.getName());
		assertTrue(movedDateField instanceof DateField);
		assertEquals("test_date", ((DateField)movedDateField).getName());
		assertEquals(3000, ((DateField)movedDateField).getPosition());
		assertNotNull(((DateField)movedDateField).getDate());
		
		// Check hidden text field
		Field movedHiddenTextField = read.getFieldByName(hiddenTextField.getName());
		assertTrue(movedHiddenTextField instanceof TextField);
		assertTrue(((TextField)movedHiddenTextField).isHidden());
		assertEquals("test_hidden_text", ((TextField)movedHiddenTextField).getName());
		assertEquals(2000, ((TextField)movedHiddenTextField).getPosition());
		assertEquals("Hidden_Text_Field", ((TextField)movedHiddenTextField).getSummary());
		
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
			thrown.expectMessage("400:Bad Request");
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
			thrown.expectMessage("400:Bad Request");
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
		thrown.expectMessage("400:Bad Request");
		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), "FooBarNonExistentField");

		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
		activityService.deleteActivityNode(dstActivityNode);
	}

	@Test
	public void testMoveFieldWithUnauthorizedUser() throws ClientServicesException, XMLException {
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

		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());
		Field aTextField = srcActivityNode.getFields()[0];
		
		// Test move field with unauthorized user
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("403:Forbidden");
		BasicEndpoint endpoint = (BasicEndpoint)activityService.getEndpoint();
		try {
			endpoint.logout();
			endpoint.login(TestEnvironment.getSecondaryUserEmail(), TestEnvironment.getSecondaryUserPassword());
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), aTextField.getFid());

		try {
			endpoint.logout();
			endpoint.login(TestEnvironment.getCurrentUserEmail(), TestEnvironment.getCurrentUserPassword());
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
		
		Field aTextField = srcActivityNode.getFields()[0];
		
		// Test wrong fieldUuid
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("404:Not Found");
		ActivityNode an = activityService.moveFieldToEntry("FooBar", aTextField.getFid());

		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
		activityService.deleteActivityNode(dstActivityNode);
	}

	@Test
	public void testMoveFieldToDeletedNode() throws ClientServicesException, XMLException {
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
		activityService.deleteActivityNode(dstActivityNode);
		
		srcActivityNode = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());

		
		// Test wrong fieldUuid
		thrown.expect(ClientServicesException.class);
		thrown.expectMessage("400:Bad Request");
		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), textField.getFid());

		// Delete the activities again
		activityService.deleteActivityNode(srcActivityNode);
	}

	@Test
	public void testMoveFieldUnsupportedNodeType() throws ClientServicesException, XMLException {
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
		dstActivityNode.setType("SECTION");
		
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
		thrown.expectMessage("400:Bad Request");
		ActivityNode an = activityService.moveFieldToEntry(dstActivityNode.getActivityUuid(), textField.getFid());

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
		assertNotNull("Expected non null activities", openActivities);
		params.put("completed", "yes");
		EntityList<Activity> allActivities = activityService.getMyActivities(params);
		
		activityService.deleteActivity(openActivity);

		assertTrue(containsActivity(openActivities, openActivity));
		assertTrue(!containsActivity(openActivities, completedActivity));
		
		assertNotNull("Expected non null activities", allActivities);
		assertTrue(containsActivity(allActivities, openActivity));
		assertTrue(containsActivity(allActivities, completedActivity));
	}
}
