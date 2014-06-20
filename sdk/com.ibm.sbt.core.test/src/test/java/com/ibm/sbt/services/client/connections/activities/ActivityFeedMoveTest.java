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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
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
        entryNode = activityService.moveNode(entryNode.getActivityNodeUuid(), sectionNode.getActivityNodeUuid());
        todoNode = activityService.moveNode(todoNode.getActivityNodeUuid(), sectionNode.getActivityNodeUuid());

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
        entryNode = activityService.moveNode(entryNode.getActivityNodeUuid(), entryNode.getActivityNodeUuid());
        } catch(ClientServicesException ex) {
            assertEquals(400,ex.getResponseStatusCode());
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
        entryNode = activityService.moveNode(sectionNode.getActivityNodeUuid(), entryNode.getActivityNodeUuid());
        } catch(ClientServicesException ex) {
            assertEquals(400,ex.getResponseStatusCode());
            return;
        }
        fail();
    }
}
