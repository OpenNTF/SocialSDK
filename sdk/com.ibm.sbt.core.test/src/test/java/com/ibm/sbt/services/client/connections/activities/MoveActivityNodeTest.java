/*
 * � Copyright IBM Corp. 2014
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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class MoveActivityNodeTest extends BaseActivityServiceTest {

	@Test
	public void testMoveActivityNode() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		ActivityNode activityNode1 = new ActivityNode();
		activityNode1.setActivityUuid(activity.getActivityUuid());
		activityNode1.setTitle("Node 1");
		activityNode1.setType(ActivityNode.TYPE_ENTRY);
		activityService.createActivityNode(activityNode1);
		
		ActivityNode activityNode2 = new ActivityNode();
		activityNode2.setActivityUuid(activity.getActivityUuid());
		activityNode2.setTitle("Node 2");
		activityNode2.setType(ActivityNode.TYPE_ENTRY);
		activityService.createActivityNode(activityNode2);
		
		ActivityNode sectionNode = new ActivityNode();
		sectionNode.setActivityUuid(activity.getActivityUuid());
		sectionNode.setTitle("Section");
		sectionNode.setType(ActivityNode.TYPE_SECTION);
		activityService.createActivityNode(sectionNode);
		//System.out.println("Section: "+sectionNode.getActivityNodeUuid());
		
		activityNode2.setInReplyTo(sectionNode);
		activityService.updateActivityNode(activityNode2);
		
		ActivityNode updated = activityService.getActivityNode(activityNode2.getActivityNodeUuid());
		
		//System.out.println(updated.toXmlString());
		
		Assert.assertEquals(sectionNode.getId(), updated.getInReplyTo().getRef());
		Assert.assertEquals(sectionNode.getSelfUrl(), updated.getInReplyTo().getHref());
	}
	
}
