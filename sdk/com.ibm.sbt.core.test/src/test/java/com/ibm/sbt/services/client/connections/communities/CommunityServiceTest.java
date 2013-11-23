package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.forums.TopicList;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Communities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class CommunityServiceTest extends BaseUnitTest {

	Community community;

	@Before
	public void createTestData() throws Exception {
		CommunityService communityService = new CommunityService();

		Community community = new Community(communityService, "");
		community.setTitle("JavaTestCommunity " + System.currentTimeMillis());
		community.setContent("Java Community Content");
		String type = "public";
		if (TestEnvironment.isSmartCloud()) {
			type = "private";
		}
		community.setCommunityType(type);
		community = community.save();
		community = community.load();
		this.community = community;
	}

	@After
	public void deleteTestData() throws Exception {
		TestEnvironment.setRequiresAuthentication(true);

		if (community != null) {
			CommunityService communityService = new CommunityService();
			communityService.deleteCommunity(community.getCommunityUuid());
		}
	}

	@Test
	public final void testGetCommunityById() throws Exception {

		CommunityService communityService = new CommunityService();

		Community retrieved = communityService.getCommunity(community
				.getCommunityUuid());
		Assert.assertTrue(retrieved.getTitle().startsWith("JavaTestCommunity"));
		assertEquals(community.getContent(), retrieved.getContent());
		assertEquals(community.getTags(), retrieved.getTags());

	}

	@Test(expected = CommunityServiceException.class)
	public final void testGetCommunityByInvalidId() throws Exception {

		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity("2344invalid");
	}

	@Test
	public final void testGetPublicCommunities() throws Exception {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getPublicCommunities();
		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());
		}
	}

	@Test
	public final void testGetPublicCommunitiesByParameters() throws Exception {
		CommunityService communityService = new CommunityService();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("tag", "test");
		parameters.put("ps", "5");
		CommunityList communities = communityService
				.getPublicCommunities(parameters);
		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());

		}
	}

	@Test
	public final void testGetMyCommunities() throws Exception {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getMyCommunities();
		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());

		}
	}

	public final void testGetMyCommunitiesNotAuthenticated() throws Exception {
		TestEnvironment.setRequiresAuthentication(false);
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getMyCommunities();
		
		Assert.assertTrue(communities == null);
	}

	@Test
	public final void testGetCommunityBookmarks() throws Exception {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getPublicCommunities();
		Community community = communities.iterator().next();

		BookmarkList bookmarks = communityService.getBookmarks(community
				.getCommunityUuid());

		for (Bookmark bookmark : bookmarks) {
			assertNotNull(bookmark.getTitle());
			assertNotNull(bookmark.getSummary());
		}
	}

	@Test
	public final void testGetCommunityForumTopics() throws Exception {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getPublicCommunities();
		Community community = communities.iterator().next();

		TopicList forumTopics = communityService.getForumTopics(community
				.getCommunityUuid());

		for (BaseForumEntity forumTopic : forumTopics) {
			assertNotNull(forumTopic.getTitle());
		}
	}

	@Test
	public final void testGetMyInvites() throws Exception {
		CommunityService communityService = new CommunityService();

		InviteList invites = communityService.getMyInvites();
		if (invites.getTotalResults() > 0) {
			for (Invite invite : invites) {
				assertNotNull(invite.getTitle());
			}
		}
	}

	@Test
	public final void testGetMembers() throws Exception {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getPublicCommunities();
		Community community = communities.iterator().next();

		MemberList members = communityService.getMembers(community
				.getCommunityUuid());

		for (Member member : members) {
			assertNotNull(member.getUserid());
			assertNotNull(member.getName());
		}
	}

	@Test
	public final void testCreateCommuniy() throws Exception {
		CommunityService communityService = new CommunityService();

		Community community = new Community(communityService, "");
		community.setTitle("testCommunity " + System.currentTimeMillis());
		community.setContent("Java Community Content");
		String type = "public";
		if (TestEnvironment.isSmartCloud()) {
			type = "private";
		}
		community.setCommunityType(type);
		community = community.save();
		assertEquals(true, community.getTitle().startsWith("testCommunity"));
		assertEquals("Java Community Content", community.getContent());

		communityService.deleteCommunity(community.getCommunityUuid());

	}

	@Test
	public final void testUpdateCommuniy() throws Exception {
		CommunityService communityService = new CommunityService();
		String newTitle = "test Title" + System.currentTimeMillis();
		String oldTitle = community.getTitle();
		community.setTitle(newTitle);
		community.setContent("test Content");
		communityService.updateCommunity(community);
		community = community.load();
		Assert.assertNotSame(oldTitle, community.getTitle());
		assertEquals("test Content", community.getContent());
	}

	@Test
	public final void testAddRemoveMember() throws Exception {
		CommunityService communityService = new CommunityService();

		String id = TestEnvironment.getSecondaryUserEmail();
		if (TestEnvironment.isSmartCloud()) id = TestEnvironment.getSecondaryUserUuid();
		Member newMember = new Member(communityService,
				id);
		communityService.addMember(community.getCommunityUuid(), newMember);
		MemberList members = communityService.getMembers(community
				.getCommunityUuid());
		for (Member member : members) {
			assertNotNull(member.getUserid());
			assertNotNull(member.getName());
		}
		communityService.removeMember(community.getCommunityUuid(),
				id);
	}

	@Test
	public final void testDeleteCommunity() throws Exception {
		CommunityService communityService = new CommunityService();

		try {
			communityService.deleteCommunity(community.getCommunityUuid());

			community = communityService.getCommunity(properties
					.getProperty("communityUuid"));
		} catch (CommunityServiceException e) {
			if (e.getCause() instanceof ClientServicesException) {
				assertEquals(404,
						((ClientServicesException) e.getCause())
								.getResponseStatusCode());
				this.community = null;
				return;
			} else {
				throw e;
			}
		}
		fail("Community found");
	}


	@Test
	public void testCreateCommunityTwice() {
		if (TestEnvironment.isSmartCloud()) return;
		String uuid1 = null;
		String uuid2 = null;
		
		String TEST_COMMUNITY_DESCRIPTION = "Test Community Description";
		String NEW_COMMUNITY = "New Community "
				+ System.currentTimeMillis();
		
		CommunityService svc = null;
		try {
			svc = new CommunityService();
			Community c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			uuid1 = svc.createCommunity(c);
			c = svc.getCommunity(uuid1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			Community c = new Community(null);
			c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			uuid2 = svc.createCommunity(c);
		} catch (CommunityServiceException e) {
			if (e.getCause() instanceof ClientServicesException
					&& (((ClientServicesException) e.getCause())
							.getResponseStatusCode() == 409)) {
				// duplicate entity exception
				return;
			}
			// in all other cases log the exception and fail
			e.printStackTrace();
			fail(e.getMessage());

		} finally {
			if (svc != null) {
				if (uuid1 != null) {
					try {
						svc.deleteCommunity(uuid1);
					} catch (Exception c) {
					}
				}
				if (uuid2 != null) {
					try {
						svc.deleteCommunity(uuid2);
					} catch (Exception c) {
					}
				}
			}

		}
		fail("Duplicated creation did not fail");
	}

}
