package com.ibm.sbt.services.client.connections.activity;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;

import com.ibm.sbt.services.BaseUnitTest;

/**
 * Common methods for tests for the java connections Activities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Carlos Manias
 */
public class BaseActivityServiceTest extends BaseUnitTest {

	protected ActivityService activityService;
	protected Activity activity;
	
	@Before
	public void initActivityServiceTest() {
		if (activityService==null) {
			activityService = new ActivityService();
		}
	}

	protected void createActivity() {
		try {

			activity = new Activity(activityService);
			activity.setTitle("Java Test Activity" + System.currentTimeMillis());
			activity.setContent("GoalOfActivity - "
					+ System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag1");
			tagList.add("tag2");
			activity.setTags(tagList);
			activity.setDueDate(new Date());
			activity = activityService.createActivity(activity);

		} catch (ActivityServiceException e) {
			e.printStackTrace();
			fail("Error creating test activity: " + e.getMessage());
		}
	}

	protected Member addMember(String activityId, String memberId) {
		Member member = null;
		try {			
			member = new Member(activityService, memberId);
			member = activityService.addMember(activityId,
					member);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			fail("Error creating test member: " + e.getMessage());
		}
		return member;
	}

	protected ActivityNode createActivityNode(String activityId, String type) {
		ActivityNode activityNode = null;
		try {

			activityNode = new ActivityNode(activityService, activityId);
			activityNode.setEntryType(type);
			activityNode.setTitle(type + "Node from Java Test "
					+ System.currentTimeMillis());
			activityNode.setContent(type + "Node Content "
					+ System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag1");
			tagList.add("tag2");
			activityNode.setTags(tagList);
			activityNode = activityService.createActivityNode(activityNode);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
			fail("Error creating test activity node: " + e.getMessage());
		}
		return activityNode;
	}

	protected void deleteActivity(String activityId) {
		if (activityId == null) {
			return;
		}
		try {

			activityService.deleteActivity(activityId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
		}
	}

	protected void deleteActivityNode(String activityNodeId) {
		if (activityNodeId == null) {
			return;
		}
		try {

			activityService.deleteActivityNode(activityNodeId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
		}
	}
}
