/**
 * 
 */
package com.ibm.sbt.automation.core.test.connections;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.ActivityNode;
import com.ibm.sbt.services.client.connections.activity.ActivityService;
import com.ibm.sbt.services.client.connections.activity.ActivityServiceException;
import com.ibm.sbt.services.client.connections.activity.Member;
import com.ibm.sbt.services.client.connections.activity.MemberList;

/**
 * @author mwallace
 * 
 */
public class BaseActivitiesTest extends BaseApiTest {
	ActivityService activityService;

	public BaseActivitiesTest() {
		super();
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

	public Activity createActivity() {
		Activity activity = null;
		try {

			activity = new Activity(activityService, "");
			activity.setTitle("JS Test Activity" + System.currentTimeMillis());
			activity.setContent("GoalOfActivity - " + System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag1");
			tagList.add("tag2");
			activity.setTags(tagList);
			activity.setDueDate(new Date());
			activity = activityService.createActivity(activity);
			Trace.log("Created Test Activity " + activity.getActivityId());

		} catch (ActivityServiceException e) {
			e.printStackTrace();
			Assert.fail("Error creating test activity: " + e.getMessage());
		}
		return activity;
	}

	public Member addMember(String activityId, String memberId) {
		Member member = null;
		try {
			member = new Member(activityService, memberId);
			activityService.addMember(activityId, member);
			MemberList memberList = activityService.getMembers(activityId);
			member = memberList.get(0);
			Trace.log("Created Test Member " + member.getMemberId() + " in activity " + activityId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			Assert.fail("Error creating test member: " + e.getMessage());
		}
		return member;
	}

	public ActivityNode createActivityNode(String activityId, String type) {
		ActivityNode activityNode = null;
		try {

			activityNode = new ActivityNode(activityService, activityId);
			activityNode.setEntryType(type);
			activityNode.setTitle(type + "Node from JS Test " + System.currentTimeMillis());
			activityNode.setContent(type + "Node Content " + System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag1");
			tagList.add("tag2");
			activityNode.setTags(tagList);
			activityNode = activityService.createActivityNode(activityNode);
			Trace.log("Created Test Activity Node " + activityNode.getActivityId());
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			Assert.fail("Error creating test activity node: " + e.getMessage());
		}
		return activityNode;
	}

	public void deleteActivity(String activityId) {
		if (activityId == null) {
			Trace.log("No Activity to delete");
			return;
		}
		try {

			activityService.deleteActivity(activityId);
			Trace.log("Deleted Test Activity " + activityId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			Assert.fail("Error deleting test activity: " + e.getMessage());
		}
	}

	public void deleteActivityNode(String activityNodeId) {
		if (activityNodeId == null) {
			Trace.log("No Activity Node to delete");
			return;
		}
		try {

			activityService.deleteActivityNode(activityNodeId);
			Trace.log("Deleted Test Activity Node " + activityNodeId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			Assert.fail("Error deleting test activity node: " + e.getMessage());
		}
	}

	public void updateActivity(Activity activity) {
		try {
			activityService.updateActivity(activity);
			Trace.log("Updated Activity "+ activity.getActivityId());
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			Assert.fail("Errorupdating activity: " + e.getMessage());
		}
	}
}
