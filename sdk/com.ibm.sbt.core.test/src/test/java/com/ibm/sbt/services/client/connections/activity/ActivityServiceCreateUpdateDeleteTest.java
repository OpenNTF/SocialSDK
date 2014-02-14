package com.ibm.sbt.services.client.connections.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.connections.activity.model.ActivityNodeType;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Activities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Vineet Kanwal
 */
public class ActivityServiceCreateUpdateDeleteTest extends BaseActivityServiceTest {

	@Before
	public void initCRUD(){
		createActivity();
	}

	@Test
	public void testCreateDeleteAndRestoreActivity()
			throws ActivityServiceException {
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
	}

	@Test
	public void testUpdateActivity() throws ActivityServiceException {
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
	}

	@Test
	public void testAddMember() throws ActivityServiceException {
		String id = TestEnvironment.getSecondaryUserUuid();
		Member memberToBeAdded = new Member(activityService, id);
		memberToBeAdded = activityService.addMember(activity.getActivityId(),
				memberToBeAdded);
		assertNotNull(memberToBeAdded.getMemberId());
	}

	@Test
	public void testUpdateMember() throws ActivityServiceException {
		Member memberToBeUpdated = addMember(activity.getActivityId(),
				TestEnvironment.getSecondaryUserUuid());

		if (memberToBeUpdated.getRole() == "member") {
			memberToBeUpdated.setRole("author");
		} else {
			memberToBeUpdated.setRole("member");
		}
		activityService.updateMember(activity.getActivityId(),
				memberToBeUpdated);
	}

	@Test
	public void testGetMembers() throws ActivityServiceException {
		addMember(activity.getActivityId(), TestEnvironment.getSecondaryUserUuid());
		List<Member> members = activityService.getMembers(activity.getActivityId());
		assertTrue(members.size() > 0);
	}

	@Test
	@Ignore
	public void testGetMember() throws ActivityServiceException {
		//BUG in connections
		//getMember generates the  url "https://qs.renovations.com:444/activities/service/atom2/acl?activityUuid=af2358ff-5da9-4729-87b8-2557ea31690e&memberid=be9d7d3e-3c7b-42b6-9da1-e928cd0a5d2b"
		//which gives a 500 error, while the url
		//"https://qs.renovations.com:444/activities/service/atom2/acl?activityUuid=af2358ff-5da9-4729-87b8-2557ea31690e&amp;memberid=be9d7d3e-3c7b-42b6-9da1-e928cd0a5d2b"
		//which is wrong, works
		Member member = addMember(activity.getActivityId(), TestEnvironment.getSecondaryUserUuid());
		member = activityService.getMember(activity.getActivityId(), member.getMemberId());
		assertNotNull(member);
	}

	@Test
	@Ignore
	public void testDeleteMember() throws ActivityServiceException {
		//Ignored because getMember is BROKEN in Connections, it needs &amp; instead of & on the URL
		Member member = addMember(activity.getActivityId(), TestEnvironment.getSecondaryUserUuid());
		activityService.deleteMember(activity.getActivityId(), member.getMemberId());
		member = activityService.getMember(activity.getActivityId(), member.getMemberId());
		assertTrue(member == null);
	}

	@Test
	public void testCreateActivityNode() throws ActivityServiceException {
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
	}

	@Test
	public void testGetActivityNode() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertNotNull(activityNode);
	}

	@Test
	public void testUpdateActivityNode() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityNode.setEntryType(ActivityNodeType.ToDo.getActivityNodeType());
		activityService.updateActivityNode(activityNode);
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertEquals(ActivityNodeType.ToDo.getActivityNodeType(), activityNode.getEntryType());
	}

	@Test
	public void testRestoreActivityNode() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());
		activityService.restoreActivityNode(activityNode.getActivityId());
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertNotNull(activityNode);
	}

	@Test
	public void testGetActivityNodeFromTrash() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());
		activityNode = activityService.getActivityNodeFromTrash(activityNode.getActivityId());
		assertNotNull(activityNode);
	}

	@Test
	public void testGetActivityNodesInTrash() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.deleteActivityNode(activityNode.getActivityId());
		List<ActivityNode> activityNodes = activityService.getActivityNodesInTrash(activity.getActivityId());
		assertTrue(activityNodes.size() > 0);
	}

	@Test
	public void testDeleteActivityNode() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		assertEquals(activityNode.getCategoryFlagDelete(), null);
		assertTrue(activityNode.getNodeUrl().lastIndexOf("/service/atom2/activitynode")>0);
		activityService.deleteActivityNode(activityNode.getActivityId());		
		activityNode = activityService.getActivityNode(activityNode.getActivityId());
		assertTrue(activityNode.getNodeUrl().lastIndexOf("/service/atom2/trashednode")>0);
		assertNotNull(activityNode.getCategoryFlagDelete());
	}

	@Test
	public void testGetActivityTags() throws ActivityServiceException {
		List<String> tags = activity.getBaseTags();
		assertTrue(tags.size() > 0);
	}

	@Test
	public void testGetActivityNodeTags() throws ActivityServiceException {
		ActivityNode activityNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		TagList tags = activityService.getActivityNodeTags(activityNode.getActivityId());
		assertTrue(tags.size() > 0);
	}

	@Test
	public void testMoveEntryToSection() throws ActivityServiceException {
		ActivityNode entryNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		ActivityNode sectionNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Section.getActivityNodeType());
		activityService.moveEntryToSection(entryNode.getActivityId(), sectionNode.getActivityId());
		entryNode = activityService.getActivityNode(entryNode.getActivityId());
		assertEquals(sectionNode.getId(), entryNode.getInReplyToId());
	}

	@Test
	public void testChangeEntryType() throws ActivityServiceException {
		ActivityNode entryNode = createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		activityService.changeEntryType(entryNode.getActivityId(), ActivityNodeType.ToDo.getActivityNodeType());
		entryNode = activityService.getActivityNode(entryNode.getActivityId());
		assertEquals(ActivityNodeType.ToDo.getActivityNodeType(), entryNode.getEntryType());
	}
	
	@Test
	public void testGetActivityNodes() throws ActivityServiceException {
		createActivityNode(activity.getActivityId(), ActivityNodeType.Entry.getActivityNodeType());
		List<ActivityNode> activityNodes = activityService.getActivityNodes(activity.getActivityId());
		assertTrue(activityNodes.size() > 0);
	}

	@After
	public void endCRUD(){
		deleteActivity(activity.getActivityId());
	}
}
