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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 * 
 */
public class ActivityFeedMoveTest extends BaseActivityServiceTest {
    @BeforeClass
    public static void before() {
        TestEnvironment.enableSmartCloud(true);
    }

    @AfterClass
    public static void after() {
        TestEnvironment.enableSmartCloud(false);
    }

    @Test
    public void testMoveField() throws ClientServicesException, XMLException {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Source ActivityNode");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        ActivityNode todoNode = createActivityNode();
        todoNode.setActivityUuid(activity.getActivityUuid());
        todoNode.setTitle("Destination ActivityNode");
        todoNode.setType("TODO");
        activityService.createActivityNode(todoNode);

        ActivityNode sectionNode = createActivityNode();
        sectionNode.setActivityUuid(activity.getActivityUuid());
        sectionNode.setTitle("Destination ActivityNode");
        sectionNode.setType("SECTION");
        activityService.createActivityNode(sectionNode);

        entryNode = activityService.moveNode(entryNode.getActivityNodeUuid(), todoNode.getActivityNodeUuid());
        entryNode = activityService.moveNode(entryNode.getActivityNodeUuid(),
                sectionNode.getActivityNodeUuid());
        todoNode = activityService
                .moveNode(todoNode.getActivityNodeUuid(), sectionNode.getActivityNodeUuid());

    }

    @Test
    public void testMoveFieldToItself() throws ClientServicesException, XMLException {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Source ActivityNode");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        try {
            entryNode = activityService.moveNode(entryNode.getActivityNodeUuid(),
                    entryNode.getActivityNodeUuid());
        } catch (ClientServicesException ex) {
            assertEquals(400, ex.getResponseStatusCode());
            return;
        }
        fail();
    }

    @Test
    public void testMoveSection() throws ClientServicesException, XMLException {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Source ActivityNode");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        ActivityNode sectionNode = createActivityNode();
        sectionNode.setActivityUuid(activity.getActivityUuid());
        sectionNode.setTitle("Destination ActivityNode");
        sectionNode.setType("SECTION");
        activityService.createActivityNode(sectionNode);

        try {
            entryNode = activityService.moveNode(sectionNode.getActivityNodeUuid(),
                    entryNode.getActivityNodeUuid());
        } catch (ClientServicesException ex) {
            assertEquals(400, ex.getResponseStatusCode());
            return;
        }
        fail();
    }

    @Test
    public void testNotFound() throws ClientServicesException, XMLException {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        try {
            activityService.moveNode("1234", activity.getActivityUuid());
        } catch (ClientServicesException ex) {
            assertEquals(404, ex.getResponseStatusCode());
            return;
        }
        fail();
    }

    @Test
    public void testNotAuthorized() throws ClientServicesException, XMLException, AuthenticationException {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Source ActivityNode");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        activityService.getEndpoint().logout();
        ((BasicEndpoint) activityService.getEndpoint()).login(TestEnvironment.getSecondaryUsername(),
                TestEnvironment.getSecondaryUserPassword());

        try {
            activityService.moveNode(entryNode.getActivityNodeUuid(), activity.getActivityUuid());
        } catch (ClientServicesException ex) {
            assertEquals(403, ex.getResponseStatusCode());
            return;
        } finally {
            activityService.getEndpoint().logout();
            ((BasicEndpoint) activityService.getEndpoint()).login(TestEnvironment.getCurrentUsername(),
                    TestEnvironment.getCurrentUserPassword());
        }
        fail();
    }

	@Test
	public void testMoveNodeACL() throws ClientServicesException {
		Activity activityA = new Activity();
		activityA.setTitle(createActivityTitle());
		activityA = activityService.createActivity(activityA);

		Activity activityB = new Activity();
		activityB.setTitle(createActivityTitle());
		activityB = activityService.createActivity(activityB);

		String memberId = getMemberId();
		Member member = activityB.addMember("person", memberId, "owner");

		ActivityNode srcActivityNode = new ActivityNode();
		srcActivityNode.setActivityUuid(activityA.getActivityUuid());
		srcActivityNode.setTitle("Source ActivityNode");
		srcActivityNode.setType("ENTRY");

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

		srcActivityNode.addField(textField);
		srcActivityNode.addField(hiddenTextField);
		srcActivityNode.addField(dateField);

		srcActivityNode = activityService.createActivityNode(srcActivityNode);
		
		int numFields = srcActivityNode.getFields().length;

		activityService.moveNode(srcActivityNode.getActivityNodeUuid(), activityB.getActivityUuid());

		BasicEndpoint endpoint = (BasicEndpoint)activityService.getEndpoint();
		try {
			endpoint.logout();
			endpoint.login(TestEnvironment.getSecondaryUserEmail(), TestEnvironment.getSecondaryUserPassword());
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ActivityNode read = activityService.getActivityNode(srcActivityNode.getActivityNodeUuid());
		assertNotNull(read);

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
	}

}
