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

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class GetActivityReplyNodeTagsTest extends BaseActivityServiceTest {

	@org.junit.Ignore("Issue with Mime Depdency")
	@Test
	public void testGetActivityNodeDescendantsTags() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		// uncomment to stop the activity being deleted
		//this.activity = null;
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	activityNode.setTags(tags);    	
		activityNode = activityService.createActivityNode(activityNode);

		ActivityNode activityReply = new ActivityNode();
		activityReply.setActivityUuid(activity.getActivityUuid());
		activityReply.setTitle(createActivityNodeTitle());
		activityReply.setTags(tags);    	
		activityReply.setType(ActivityNode.TYPE_REPLY);
		activityReply.setInReplyTo(activityNode);
		activityReply = activityService.createActivityNode(activityReply);
		
		EntityList<ActivityNode> activityNodes = activityService.getActivityDescendants(activity.getActivityUuid());
		Assert.assertEquals(2, activityNodes.size());
		for (ActivityNode nextNode : activityNodes) {
			System.out.println(nextNode.toXmlString());
			tags = nextNode.getTags();
			Assert.assertEquals(3, tags.size());
		}
	}
		
	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testGetActivityNodeChildrenTags() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		// uncomment to stop the activity being deleted
		//this.activity = null;
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	activityNode.setTags(tags);    	
		activityNode = activityService.createActivityNode(activityNode);

		ActivityNode activityReply = new ActivityNode();
		activityReply.setActivityUuid(activity.getActivityUuid());
		activityReply.setTitle(createActivityNodeTitle());
		activityReply.setTags(tags);    	
		activityReply.setType(ActivityNode.TYPE_REPLY);
		activityReply.setInReplyTo(activityNode);
		activityReply = activityService.createActivityNode(activityReply);
		
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeChildren(activityNode.getActivityNodeUuid());
		Assert.assertEquals(1, activityNodes.size());
		for (ActivityNode nextNode : activityNodes) {
			System.out.println(nextNode.toXmlString());
			tags = nextNode.getTags();
			Assert.assertEquals(3, tags.size());
		}
	}
		
}
