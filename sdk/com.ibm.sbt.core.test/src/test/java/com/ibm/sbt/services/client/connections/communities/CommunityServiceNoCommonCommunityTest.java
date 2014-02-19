package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

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
 * @author Carlos Manias
 * @date Jan 13, 2014
 */

public class CommunityServiceNoCommonCommunityTest extends BaseUnitTest {

	protected CommunityService communityService;

	@Before
	public void initCommunityService() throws Exception {
		if (communityService == null) {
			communityService = new CommunityService();
		}
	}

	@Test(expected = CommunityServiceException.class)
	public final void testGetCommunityByInvalidId() throws Exception {
		communityService.getCommunity("2344invalid");
	}

	@Test
	public final void testGetPublicCommunities() throws Exception {
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
		CommunityList communities = communityService.getMyCommunities();
		
		Assert.assertTrue(communities == null);
	}

	@Test
	public final void testGetCommunityBookmarks() throws Exception {
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
		InviteList invites = communityService.getMyInvites();
		if (invites.getTotalResults() > 0) {
			for (Invite invite : invites) {
				assertNotNull(invite.getTitle());
			}
		}
	}

	@Test
	public final void testGetMembers() throws Exception {
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
	public final void testCreateCommunity() throws Exception {
		Community community = new Community(communityService, "");
		community.setTitle("testCommunity " + System.currentTimeMillis());
		community.setContent("Java Community Content");
		String type = "public";
		if (TestEnvironment.isSmartCloudEnvironment()) {
			type = "private";
		}
		community.setCommunityType(type);
		community = community.save();
		assertEquals(true, community.getTitle().startsWith("testCommunity"));
		assertEquals("Java Community Content", community.getContent());

		communityService.deleteCommunity(community.getCommunityUuid());

	}

	@Test
	public void testCreateCommunityTwice() {
		if (TestEnvironment.isSmartCloudEnvironment()) return;
		String uuid1 = null;
		String uuid2 = null;
		
		String TEST_COMMUNITY_DESCRIPTION = "Test Community Description";
		String NEW_COMMUNITY = "New Community "
				+ System.currentTimeMillis();

		try {
			Community c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			uuid1 = communityService.createCommunity(c);
			c = communityService.getCommunity(uuid1);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		try {
			Community c = new Community(null);
			c = new Community(null);
			c.setTitle(NEW_COMMUNITY);
			c.setContent(TEST_COMMUNITY_DESCRIPTION);
			uuid2 = communityService.createCommunity(c);
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
			if (communityService != null) {
				if (uuid1 != null) {
					try {
						communityService.deleteCommunity(uuid1);
					} catch (Exception c) {
					}
				}
				if (uuid2 != null) {
					try {
						communityService.deleteCommunity(uuid2);
					} catch (Exception c) {
					}
				}
			}
		}
		fail("Duplicated creation did not fail");
	}

}
