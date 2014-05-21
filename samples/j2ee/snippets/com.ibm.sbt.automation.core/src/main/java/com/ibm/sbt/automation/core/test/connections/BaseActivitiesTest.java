/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.automation.core.test.connections;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;

import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.activities.Activity;
import com.ibm.sbt.services.client.connections.activities.ActivityNode;
import com.ibm.sbt.services.client.connections.activities.ActivityService;
import com.ibm.sbt.services.client.connections.common.Member;

/**
 * @author mwallace
 * 
 */
public class BaseActivitiesTest extends BaseApiTest {
	ActivityService activityService;
	
	@Before
	public void setupService(){
		
		setAuthType(AuthType.AUTO_DETECT);
		activityService = getActivityService();
	}

	protected ActivityService getActivityService() {
		try {
			loginConnections();
		} catch (AuthenticationException e) {
			Assert.fail("Error logging in to Connections " + e.getMessage());
			e.printStackTrace();
			return null;
		}

		if (activityService == null) {
			activityService = new ActivityService(getEndpointName());
		}
		return activityService;
	}

	public Activity createActivity() throws ClientServicesException {
		Activity activity = null;
		activity = new Activity(activityService);
		activity.setTitle("JS Test Activity" + System.currentTimeMillis());
		activity.setContent("GoalOfActivity - " + System.currentTimeMillis());
		List<String> tagList = new ArrayList<String>();
		tagList.add("tag1");
		tagList.add("tag2");
		activity.setTags(tagList);
		activity.setDuedate(new Date());
		activity = activityService.createActivity(activity);
		Trace.log("Created Test Activity " + activity.getActivityUuid());

		return activity;
	}

	public Member addMember(String activityId, String memberId) throws ClientServicesException {
		Member member = null;
		member = new Member();
		member.setService(activityService);
		member.setId(memberId);
		member = activityService.addMember(activityId, member);
		Trace.log("Created Test Member " + member.getId() + " in activity " + activityId);
		return member;
	}

	public ActivityNode createActivityNode(String activityId, String type) throws ClientServicesException {
		ActivityNode activityNode = null;
		activityNode = new ActivityNode(activityService);
		activityNode.setActivityUuid(activityId);
		//activityNode.setEntryType(type);
		activityNode.setTitle(type + "Node from JS Test " + System.currentTimeMillis());
		activityNode.setContent(type + "Node Content " + System.currentTimeMillis());
		List<String> tagList = new ArrayList<String>();
		tagList.add("tag1");
		tagList.add("tag2");
		activityNode.setTags(tagList);
		activityNode = activityService.createActivityNode(activityNode);
		Trace.log("Created Test Activity Node " + activityNode.getActivityUuid());
		return activityNode;
	}

	public void deleteActivity(String activityId) throws ClientServicesException {
		if (activityId == null) {
			Trace.log("No Activity to delete");
			return;
		}
		activityService.deleteActivity(activityId, null);
		Trace.log("Deleted Test Activity " + activityId);
	}

	public void deleteActivityNode(String activityNodeId) throws ClientServicesException {
		if (activityNodeId == null) {
			Trace.log("No Activity Node to delete");
			return;
		}
		activityService.deleteActivityNode(activityNodeId, null);
		Trace.log("Deleted Test Activity Node " + activityNodeId);
	}

	public void updateActivity(Activity activity) throws ClientServicesException {
		activityService.updateActivity(activity);
		Trace.log("Updated Activity "+ activity.getActivityUuid());
	}
}
