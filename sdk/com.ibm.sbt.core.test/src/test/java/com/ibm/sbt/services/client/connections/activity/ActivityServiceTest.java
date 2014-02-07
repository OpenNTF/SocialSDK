package com.ibm.sbt.services.client.connections.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.activity.model.ActivityNodeType;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Activities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Vineet Kanwal
 */
public class ActivityServiceTest extends BaseUnitTest {

	ActivityService activityService;

	public Activity createActivity() {
		Activity activity = null;
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
		return activity;
	}

	public Member addMember(String activityId, String memberId) {
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

	public ActivityNode createActivityNode(String activityId, String type) {
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

	public void deleteActivity(String activityId) {
		if (activityId == null) {
			return;
		}
		try {

			activityService.deleteActivity(activityId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
		}
	}

	public void deleteActivityNode(String activityNodeId) {
		if (activityNodeId == null) {
			return;
		}
		try {

			activityService.deleteActivityNode(activityNodeId);
		} catch (ActivityServiceException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMyActivities() throws ActivityServiceException {
		activityService = new ActivityService();
		ActivityList activities = activityService.getMyActivities();
		for (Activity activity : activities) {
			assertEquals("Frank Adams", activity.getAuthor().getName());
		}
	}

	@Test
	public void testGetCompletedActivities() throws ActivityServiceException {
		activityService = new ActivityService();
		ActivityList activities = activityService.getCompletedActivities();
		for (Activity activity : activities) {
			assertTrue(activity.getCategoryFlagCompleted()
					.contains("Completed"));
		}
	}

	@Test
	public void testGetAllActivities() throws ActivityServiceException {
		activityService = new ActivityService();
		ActivityList activities = activityService.getAllActivities();
		for (Activity activity : activities) {
			assertNotNull(activity.getTitle());
		}
	}

	@Test
	public void testGetAllTodos() throws ActivityServiceException {
		activityService = new ActivityService();
		ActivityList activities = activityService.getAllTodos();
		for (Activity activity : activities) {
			assertEquals("todo", activity.getEntryType());
		}
	}

	@Test
	public void testGetAllTags() throws ActivityServiceException {
		activityService = new ActivityService();
		TagList tags = activityService.getAllTags();
		for (Tag tag : tags) {
			assertNotNull(tag.getTerm());
		}
	}

	@Test
	public void testGetActivitiesInTrash() throws ActivityServiceException {
		activityService = new ActivityService();
		ActivityList activities = activityService.getActivitiesInTrash();
		for (Activity activity : activities) {
			assertNotNull(activity.getTitle());
		}
	}

	@Test
	public void testCreateDeleteAndRestoreActivity()
			throws ActivityServiceException {
		activityService = new ActivityService();
		activityService = new ActivityService();
		Activity activity = new Activity(activityService);
		activity.setTitle("UntTestActivity" + System.currentTimeMillis());
		activity.setContent("GoalOfActivity - " + System.currentTimeMillis());
		List<String> tagList = new ArrayList<String>();
		tagList.add("tag1");
		tagList.add("tag2");
		activity.setTags(tagList);
		activity.setDueDate(new Date());

		activity = activityService.createActivity(activity);
		assertNotNull(activity.getActivityId());

		activityService.deleteActivity(activity.getActivityId());

		activityService.restoreActivity(activity.getActivityId());

		Activity restoredActivity = activityService.getActivity(activity
				.getActivityId());

		assertNotNull(restoredActivity.getActivityId());

		assertEquals(activity.getActivityId(), restoredActivity.getActivityId());

		activityService.deleteActivity(activity.getActivityId());
	}

	@Test
	public void testUpdateActivity() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();

		activity.setTitle("UpdateByUnitTest" + System.currentTimeMillis());
		activity.setGoal("GoalOfActivity updated - "
				+ System.currentTimeMillis());

		List<String> tagList = new ArrayList<String>();
		tagList.add("tag123");
		tagList.add("tagabc");
		activity.setTags(tagList);
		activity.setDueDate(new Date());
		activityService.updateActivity(activity);
		Activity updatedActivity = activityService.getActivity(activity
				.getActivityId());
		assertTrue(updatedActivity.getTitle().contains("UpdateByUnitTest"));

		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testAddMember() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		String id = TestEnvironment.getSecondaryUserUuid();
		Member memberToBeAdded = new Member(activityService, id);
		memberToBeAdded = activityService.addMember(activity.getActivityId(),
				memberToBeAdded);
		assertNotNull(memberToBeAdded.getMemberId());
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testUpdateMember() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		Member memberToBeUpdated = addMember(activity.getActivityId(),
				TestEnvironment.getSecondaryUserUuid());

		if (memberToBeUpdated.getRole() == "member") {
			memberToBeUpdated.setRole("author");
		} else {
			memberToBeUpdated.setRole("member");
		}
		activityService.updateMember(activity.getActivityId(),
				memberToBeUpdated);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetMembers() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		Member member = addMember(activity.getActivityId(), TestEnvironment.getSecondaryUserUuid());
		List<Member> members = activityService.getMembers(activity.getActivityId());
		assertTrue(members.size() > 0);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetMember() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		Member member = addMember(activity.getActivityId(), TestEnvironment.getSecondaryUserUuid());
		member = activityService.getMember(activity.getActivityId(), member.getMemberId());
		assertNotNull(member);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testDeleteMember() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		Member member = addMember(activity.getActivityId(), TestEnvironment.getSecondaryUserUuid());
		activityService.deleteMember(activity.getActivityId(), member.getMemberId());
		member = activityService.getMember(activity.getActivityId(), member.getMemberId());
		assertTrue(member == null);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testCreateActivityNode() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = new ActivityNode(activityService, activity.getActivityId());
		activityNode.setEntryType(ActivityNodeType.Entry.getActivityNodeType());
		activityNode.setTitle(ActivityNodeType.Entry.getActivityNodeType() + "Node from Java Test "
				+ System.currentTimeMillis());
		activityNode.setContent(ActivityNodeType.Entry.getActivityNodeType() + "Node Content "
				+ System.currentTimeMillis());
		List<String> tagList = new ArrayList<String>();
		tagList.add("tag1");
		tagList.add("tag2");
		activityNode.setTags(tagList);
		activityNode = activityService.createActivityNode(activityNode);
		assertNotNull(activityNode.getActivityId());
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetActivityNode() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertNotNull(activityNode);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testUpdateActivityNode() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityNode.setEntryType(ActivityNodeType.ToDo.getActivityNodeType());
		activityService.updateActivityNode(activityNode);
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertEquals(ActivityNodeType.ToDo.getActivityNodeType(), activityNode.getEntryType());
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testRestoreActivityNode() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());
		activityService.restoreActivityNode(activityNode.getActivityId());
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertNotNull(activityNode);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetActivityNodeFromTrash() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());
		activityNode = activityService.getActivityNodeFromTrash(activityNode.getActivityId());
		assertNotNull(activityNode);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetActivityNodesInTrash() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());
		List<ActivityNode> activityNodes = activityService.getActivityNodesInTrash(activity.getActivityId());
		assertTrue(activityNodes.size() > 0);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testDeleteActivityNode() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());		
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertTrue(activityNode == null);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetActivityTags() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		TagList tags = activityService.getActivityTags(activity.getActivityId());
		assertTrue(tags.size() > 0);
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetActivityNodeTags() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		TagList tags = activityService.getActivityNodeTags(activityNode.getActivityId());
		assertTrue(tags.size() > 0);
		deleteActivity(activity.getActivityId());
		
	}

	@Test
	public void testMoveEntryToSection() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode entryNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		ActivityNode sectionNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Section.getActivityNodeType());
		activityService.moveEntryToSection(entryNode.getActivityId(), sectionNode.getActivityId());
		entryNode = activityService.getActivityNode(entryNode.getActivityId());
		assertEquals(sectionNode.getId(), entryNode.getInReplyToId());
		deleteActivity(activity.getActivityId());
	}

	
	@Test
	public void testChangeEntryType() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode entryNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.changeEntryType(entryNode.getActivityId(), ActivityNodeType.ToDo.getActivityNodeType());
		entryNode = activityService.getActivityNode(entryNode.getActivityId());
		assertEquals(ActivityNodeType.ToDo.getActivityNodeType(), entryNode.getEntryType());
		deleteActivity(activity.getActivityId());
	}

	
	@Test
	public void testGetActivityNodes() throws ActivityServiceException {
		activityService = new ActivityService();
		Activity activity = createActivity();
		ActivityNode entryNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		List<ActivityNode> activityNodes = activityService.getActivityNodes(activity.getActivityId());
		assertTrue(activityNodes.size() > 0);
		deleteActivity(activity.getActivityId());
	}

}
