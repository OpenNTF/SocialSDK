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
import org.junit.Assert;
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
public class ActivityEntryDuplicateTests extends BaseActivityServiceTest {
	
	@Test
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testMoveEntry() throws ClientServicesException, XMLException {
        
    	activity = createActivity(createActivityTitle());
    	
        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Entry Node");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        ActivityNode todoNode = new ActivityNode();
        todoNode.setActivityUuid(activity.getActivityUuid());
        todoNode.setTitle("Todo Node");
        todoNode.setType("TODO");
        activityService.createActivityNode(todoNode);

        ActivityNode sectionNode = new ActivityNode();
        sectionNode.setActivityUuid(activity.getActivityUuid());
        sectionNode.setTitle("Section Node");
        sectionNode.setType("SECTION");
        activityService.createActivityNode(sectionNode);

        //created 3 nodes
        activity = activityService.getActivity(activity.getActivityUuid());
        Assert.assertEquals(activity.getDescendants().size(), 3);
        
        
        //copy entry to todo + 1
        ActivityNode entryNode2 = activityService.copyNode(entryNode.getActivityNodeUuid(), todoNode.getActivityNodeUuid());
        //copy entry to section + 1
        ActivityNode entryNode3 = activityService.copyNode(entryNode2.getActivityNodeUuid(), sectionNode.getActivityNodeUuid());
        //copy todo to section (now has entry) + 2
        ActivityNode todoNode2 = activityService
                .copyNode(todoNode.getActivityNodeUuid(), sectionNode.getActivityNodeUuid());
        
        
        
        activity = activityService.getActivity(activity.getActivityUuid());
        Assert.assertEquals(activity.getDescendants().size(), 7);
        
    }
    
    //@org.junit.Ignore("Issue with Mime Depdency")
//    /@org.junit.Ignore("Issue with Mime Depdency")
	@Test
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testMoveFieldChangeActivity() throws ClientServicesException, XMLException {
        // Create activity nodes

        
       

        Activity activityb = createActivity("DESTINATION "+ createActivityTitle());
        
        activity= createActivity("SOURCE "+ createActivityTitle());
        
        
        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Entry ActivityNode");
        entryNode.setContent("Entry Content");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        ActivityNode todoNode =  new ActivityNode();
        todoNode.setActivityUuid(activity.getActivityUuid());
        todoNode.setTitle("Todo ActivityNode");
        todoNode.setType("TODO");
        activityService.createActivityNode(todoNode);

        ActivityNode sectionNode =  new ActivityNode();
        sectionNode.setActivityUuid(activity.getActivityUuid());
        sectionNode.setTitle("Section ActivityNode");
        sectionNode.setType("SECTION");
        activityService.createActivityNode(sectionNode);

        activity = activityService.getActivity(activity.getActivityUuid());
        Assert.assertEquals(activity.getDescendants().size(), 3);
        
        ActivityNode entryNode2 = activityService.copyNode(entryNode.getActivityNodeUuid(), activityb.getActivityUuid());
        ActivityNode sectionNode2 = activityService.copyNode(sectionNode.getActivityNodeUuid(), activityb.getActivityUuid());
        ActivityNode todoNode2 = activityService.copyNode(todoNode.getActivityNodeUuid(), activityb.getActivityUuid());
        
        
        activity = activityService.getActivity(activity.getActivityUuid());
        Assert.assertEquals(activity.getDescendants().size(), 3);
        activityb = activityService.getActivity(activity.getActivityUuid());
        Assert.assertEquals(activityb.getDescendants().size(), 3);
        
        //refresh entities
        for (ActivityNode n: activity.getDescendants()) {
        	if (n.getId().equals(entryNode.getId())) {
        		entryNode = n;
        	}
        }
        //refresh entities
        for (ActivityNode n: activityb.getDescendants()) {
        	if (n.getId().equals(entryNode2.getId())) {
        		entryNode = n;
        	}
        }
        
        Assert.assertNotEquals(entryNode.getActivityNodeUuid(), entryNode2.getActivityNodeUuid());
        Assert.assertEquals(entryNode.getTitle(), entryNode2.getTitle());
        Assert.assertEquals(entryNode.getContent(), entryNode2.getContent());
        
    }

    @Test
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testcopyFieldToItself() throws ClientServicesException, XMLException {
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
            entryNode = activityService.copyNode(entryNode.getActivityNodeUuid(),
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
            entryNode = activityService.copyNode(sectionNode.getActivityNodeUuid(),
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
            activityService.copyNode("1234", activity.getActivityUuid());
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
            activityService.copyNode(entryNode.getActivityNodeUuid(), activity.getActivityUuid());
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

        try {
            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(), activity.getActivityUuid());

            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(),
                    todoNode.getActivityNodeUuid());
            assertEquals(todoNode.getActivityNodeUuid(),
                    replyNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(),
                    entryNode.getActivityNodeUuid());
            assertEquals(todoNode.getActivityNodeUuid(),
                    replyNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));

        } catch (ClientServicesException ex) {
            assertEquals(400, ex.getResponseStatusCode());
            return;
        }
    }

    //@org.junit.Ignore("Issue with Mime Depdency")@Test
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
        try {
            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(),
                    todoNode.getActivityNodeUuid());

            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(),
                    entryNode.getActivityNodeUuid());
            replyNode2 = activityService.copyNode(replyNode2.getActivityNodeUuid(),
                    entryNode.getActivityNodeUuid());
            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(),
                    replyNode2.getActivityNodeUuid());

            replyNode = activityService.getActivityNode(replyNode.getActivityNodeUuid());
            replyNode2 = activityService.getActivityNode(replyNode2.getActivityNodeUuid());

            replyNode = activityService.copyNode(replyNode.getActivityNodeUuid(), activity.getActivityUuid());
        } catch (ClientServicesException ex) {
            assertEquals(400, ex.getResponseStatusCode());
            return;
        }
    }

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testcopyNodeACL() throws ClientServicesException {
		
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

		ActivityNode newNode = activityService.copyNode(srcActivityNode.getActivityNodeUuid(), activityB.getActivityUuid());

		BasicEndpoint endpoint = (BasicEndpoint)activityService.getEndpoint();
		try {
			endpoint.logout();
			endpoint.login(TestEnvironment.getSecondaryUsername(), TestEnvironment.getSecondaryUserPassword());
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ActivityNode read = activityService.getActivityNode(newNode.getActivityNodeUuid());
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
	
	
    @Test
    @org.junit.Ignore("Issue with Mime Depdency")
    public void testSimpleClone() throws Exception {
        // Create activity nodes

        activity = new Activity();
        activity.setTitle(createActivityTitle());
        activity = activityService.createActivity(activity);

        ActivityNode entryNode = new ActivityNode();
        entryNode.setActivityUuid(activity.getActivityUuid());
        entryNode.setTitle("Source ActivityNode");
        entryNode.setType("ENTRY");
        activityService.createActivityNode(entryNode);

        ActivityNode entryNode2 = activityService.copyNode(entryNode.getActivityNodeUuid(), activity.getActivityUuid());
        
        Assert.assertNotEquals(entryNode.getActivityNodeUuid(), entryNode2.getActivityNodeUuid());
        
        activity = activityService.getActivity(activity.getActivityUuid());
        Assert.assertEquals(activity.getDescendants().size(), 2);

    }
    

}
