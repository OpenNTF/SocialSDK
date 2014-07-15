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
public class ActivityFeedMoveTest extends BaseActivityServiceTest {

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
            replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(), activity.getActivityUuid());

            replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                    todoNode.getActivityNodeUuid());
            assertEquals(todoNode.getActivityNodeUuid(),
                    replyNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));
            replyNode = activityService.moveNode(replyNode.getActivityNodeUuid(),
                    entryNode.getActivityNodeUuid());
            assertEquals(todoNode.getActivityNodeUuid(),
                    replyNode.getInReplyTo().getHref().replaceAll(".*Uuid=", ""));

        } catch (ClientServicesException ex) {
            assertEquals(400, ex.getResponseStatusCode());
            return;
        }
    }

    @Test
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
        } catch (ClientServicesException ex) {
            assertEquals(400, ex.getResponseStatusCode());
            return;
        }
    }
}
