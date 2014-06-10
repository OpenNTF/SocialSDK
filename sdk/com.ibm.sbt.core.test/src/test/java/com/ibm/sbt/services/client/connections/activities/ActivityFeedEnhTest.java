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

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Link;
import com.ibm.sbt.services.client.connections.common.Person;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.model.FileRequestParams;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class ActivityFeedEnhTest extends BaseActivityServiceTest {

	@Test
	public void testIncludeCompletedActivities() throws ClientServicesException, XMLException {
		// Create activities
		Activity srcActivity = createActivity();
		Activity destActivity = createActivity();
		
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
		
		// Field 5
		String name = TestEnvironment.getSecondaryUserDisplayName();
		String email = TestEnvironment.getSecondaryUserEmail();
		String userid = TestEnvironment.getSecondaryUserUuid();
		
		PersonField personField = new PersonField();
		personField.setName("test_person");
		personField.setPosition(5000);
		personField.setPerson(new Person(name, email, userid));

		
		// Populate source activity and update
		srcActivity.addField(textField);
		srcActivity.addField(hiddenTextField);
		srcActivity.addField(dateField);
		srcActivity.addField(linkField);
		srcActivity.addField(personField);
		srcActivity.update();
		
		// The original number of fields in the source node
		int orgNumFields = srcActivity.getFields().length;
		
		activityService.updateActivity(srcActivity);
		
		ActivityNode an = null;
		// Move all fields
		for (Field f : srcActivity.getFields()) {
			an = activityService.moveFieldToEntry(destActivity.getActivityUuid(), f.getFid(), (int) f.getPosition());
		}
		
		Assert.assertNotNull(an);
		
		Activity read = an.getActivityService().getActivity(destActivity.getActivityUuid());
		Field[] fields = read.getFields();
		
		// Check that all fields have been moved
		Assert.assertNotNull(fields);
		Assert.assertEquals(orgNumFields, fields.length);
		
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
		Assert.assertEquals(1000, ((TextField)movedTextField).getPosition());
		Assert.assertEquals("Test_Text_Field", ((TextField)movedTextField).getSummary());
		
		// Check hidden text field
		Field movedHiddenTextField = read.getFieldByName(hiddenTextField.getName());
		Assert.assertTrue(movedHiddenTextField instanceof TextField);
		Assert.assertTrue(((TextField)movedHiddenTextField).isHidden());
		Assert.assertEquals("test_hidden_text", ((TextField)movedHiddenTextField).getName());
		Assert.assertEquals(1000, ((TextField)movedHiddenTextField).getPosition());
		Assert.assertEquals("Hidden_Text_Field", ((TextField)movedHiddenTextField).getSummary());
		
		// Check link field
		Field movedLinkField = read.getFieldByName(linkField.getName());
		Assert.assertTrue(movedLinkField instanceof LinkField);
		Assert.assertEquals("test_link", ((LinkField)movedLinkField).getName());
		Assert.assertEquals(1000, ((LinkField)movedLinkField).getPosition());
		Assert.assertEquals("IBM", ((LinkField)movedLinkField).getLink().getTitle());
		Assert.assertEquals("http://www.ibm.com", ((LinkField)movedLinkField).getLink().getHref());
		
		// Check person field
		Field movedPersonField = read.getFieldByName(personField.getName());
		Assert.assertTrue(movedPersonField instanceof PersonField);
		Assert.assertEquals("test_person", ((PersonField)movedPersonField).getName());
		Assert.assertEquals(1000, ((PersonField)movedPersonField).getPosition());
		Assert.assertEquals(name, ((PersonField)movedPersonField).getPerson().getName());
		//Assert.assertEquals(email, ((PersonField)fields[0]).getPerson().getEmail());
		Assert.assertEquals(userid, ((PersonField)movedPersonField).getPerson().getUserid());
		Assert.assertNotNull(((PersonField)movedPersonField).getPerson().getUserState());

		// Delete the activities again
		activityService.deleteActivity(srcActivity);
		activityService.deleteActivity(destActivity);
	}
	
	@Test
	public void testMoveField() throws ClientServicesException, XMLException {
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
	
//	File file = uploadFile("testUpdateFileMetadata");
//    EntityList<File> listOfFiles = fileService.getMyFiles();
//    String testFileId = listOfFiles.get(0).getFileId();
//    File fileEntry = fileService.getFile(testFileId, false);
//    Map<String, String> paramsMap = new HashMap<String, String>();
//    Random random = new Random();
//    paramsMap.put(FileRequestParams.TAG.getFileRequestParams(), "Junit_Tag" + random.nextInt());
//    String label = "Junit_Label_New" + random.nextInt();
//    fileEntry.setLabel(label);
//    fileEntry = fileService.updateFileMetadata(fileEntry, paramsMap);
//    assertEquals(unRandomize(fileEntry.getLabel()), unRandomize(label));
//    fileService.deleteFile(file.getFileId());
	
}
