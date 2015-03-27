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
    @Test
    @org.junit.Ignore("Issue with Mime Depdency")
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
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testMoveFieldChangeActivity() throws ClientServicesException, XMLException {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        Activity activityb;
        activityb = new Activity();
        activityb.setTitle(createActivityTitle());
        activityb = activityService.createActivity(activityb);
        
        
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

        entryNode = activityService.moveNode(entryNode.getActivityNodeUuid(), activityb.getActivityUuid());
        sectionNode = activityService.moveNode(sectionNode.getActivityNodeUuid(), activityb.getActivityUuid());
        todoNode = activityService.moveNode(todoNode.getActivityNodeUuid(), activityb.getActivityUuid());
    }

    @Test
    @org.junit.Ignore("Issue with Mime Depdency")
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
    @org.junit.Ignore("Issue with Mime Depdency")
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
    @org.junit.Ignore("Issue with Mime Depdency")
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
    @org.junit.Ignore("Issue with Mime Depdency")
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
        	ex.printStackTrace();
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
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testMoveReplyThread() throws ClientServicesException{
        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Entry ActivityNode A");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        ActivityNode replyNode = createActivityNode();
        replyNode.setActivityUuid(activity.getActivityUuid());
        replyNode.setTitle("Reply ActivityNode 1A");
        replyNode.setType("REPLY");
        replyNode.setInReplyTo(entryNode);
        activityService.createActivityNode(replyNode);

        ActivityNode replyNode2 = createActivityNode();
        replyNode2.setActivityUuid(activity.getActivityUuid());
        replyNode2.setTitle("Reply ActivityNode 2A");
        replyNode2.setType("REPLY");
        replyNode2.setInReplyTo(replyNode);
        activityService.createActivityNode(replyNode2);

        ActivityNode replyNode3 = createActivityNode();
        replyNode3.setActivityUuid(activity.getActivityUuid());
        replyNode3.setTitle("Reply ActivityNode 3A");
        replyNode3.setType("REPLY");
        replyNode3.setInReplyTo(replyNode2);
        activityService.createActivityNode(replyNode3);

        ActivityNode replyNode4 = createActivityNode();
        replyNode4.setActivityUuid(activity.getActivityUuid());
        replyNode4.setTitle("Reply ActivityNode 4A");
        replyNode4.setType("REPLY");
        replyNode4.setInReplyTo(replyNode3);
        activityService.createActivityNode(replyNode4);

        Activity activityB = new Activity();
        activityB.setTitle(createActivityTitle());
        activityB = activityService.createActivity(activityB);

        ActivityNode entryNodeB = new ActivityNode();
        entryNodeB.setActivityUuid(activityB.getActivityUuid());
        entryNodeB.setTitle("Entry ActivityNode B");
        entryNodeB.setType("ENTRY");
        activityService.createActivityNode(entryNodeB);

        ActivityNode replyNodeB = createActivityNode();
        replyNodeB.setActivityUuid(activityB.getActivityUuid());
        replyNodeB.setTitle("Reply ActivityNode 1B");
        replyNodeB.setType("REPLY");
        replyNodeB.setInReplyTo(entryNodeB);
        activityService.createActivityNode(replyNodeB);

        ActivityNode replyNode2B = createActivityNode();
        replyNode2B.setActivityUuid(activityB.getActivityUuid());
        replyNode2B.setTitle("Reply ActivityNode 2B");
        replyNode2B.setType("REPLY");
        replyNode2B.setInReplyTo(replyNodeB);
        activityService.createActivityNode(replyNode2B);

        ActivityNode replyNode3B = createActivityNode();
        replyNode3B.setActivityUuid(activityB.getActivityUuid());
        replyNode3B.setTitle("Reply ActivityNode 3B");
        replyNode3B.setType("REPLY");
        replyNode3B.setInReplyTo(replyNode2B);
        activityService.createActivityNode(replyNode3B);

        ActivityNode replyNode4B = createActivityNode();
        replyNode4B.setActivityUuid(activityB.getActivityUuid());
        replyNode4B.setTitle("Reply ActivityNode 4B");
        replyNode4B.setType("REPLY");
        replyNode4B.setInReplyTo(replyNode3B);
        activityService.createActivityNode(replyNode4B);

        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(), replyNode4B.getActivityNodeUuid());
        
        ActivityNode threadNode = activityService.getActivityNode(replyNode4.getActivityNodeUuid());
        assertEquals(replyNode4.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 4A", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNode3.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 3A", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNode2.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 2A", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNode.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 1A", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNode4B.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 4B", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNode3B.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 3B", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNode2B.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 2B", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(replyNodeB.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Reply ActivityNode 1B", threadNode.getTitle());

        threadNode = activityService.getActivityNode(threadNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        assertEquals(entryNodeB.getActivityNodeUuid(), threadNode.getActivityNodeUuid());
        assertEquals("Entry ActivityNode B", threadNode.getTitle());
    	
		if (activityB != null) {
			try {
				activityService.deleteActivity(activityB);
			} catch (Exception e) {
			}
		}
    }
    
    @Test
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testMoveReply() throws ClientServicesException, XMLException {

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

        ActivityNode replyNode = createActivityNode();
        replyNode.setActivityUuid(activity.getActivityUuid());
        replyNode.setTitle("Destination ActivityNode");
        replyNode.setType("REPLY");
        replyNode.setInReplyTo(entryNode);
        activityService.createActivityNode(replyNode);

        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(), activity.getActivityUuid());

        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                todoNode.getActivityNodeUuid());
        assertEquals(todoNode.getActivityNodeUuid(),
                replyNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                entryNode.getActivityNodeUuid());
        assertEquals(todoNode.getActivityNodeUuid(),
                replyNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));

    }

    @Test
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testMoveReplyAdvanced() throws ClientServicesException, XMLException {
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

        ActivityNode replyNode = createActivityNode();
        replyNode.setActivityUuid(activity.getActivityUuid());
        replyNode.setTitle("Destination ActivityNode");
        replyNode.setType("REPLY");
        replyNode.setInReplyTo(entryNode);
        activityService.createActivityNode(replyNode);

        ActivityNode replyNode2 = createActivityNode();
        replyNode2.setActivityUuid(activity.getActivityUuid());
        replyNode2.setTitle("Destination ActivityNode");
        replyNode2.setType("REPLY");
        replyNode2.setInReplyTo(replyNode);

        activityService.createActivityNode(replyNode2);
        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                todoNode.getActivityNodeUuid());

        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                entryNode.getActivityNodeUuid());
        replyNode2 = activityService.moveNode(replyNode2.getActivityNodeUuid(),
                entryNode.getActivityNodeUuid());
        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                replyNode2.getActivityNodeUuid());

        replyNode = activityService.getActivityNode(replyNode.getActivityNodeUuid());
        replyNode2 = activityService.getActivityNode(replyNode2.getActivityNodeUuid());

        replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(), activity.getActivityUuid());
    }

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
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
