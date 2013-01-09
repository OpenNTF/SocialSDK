package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.BaseUnitTest;

/**
 * Tests for the java connections Communities API a test class provides its own tests extending the test
 * endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class CommunityServiceTest extends BaseUnitTest {
	@Ignore
	@Test
	public final void testGetCommunityById() throws Exception {

		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity(properties.getProperty("communityUuid"));
		assertNotNull(community);
		assertNotNull(community.getData());
		assertEquals("tCom12", community.getTitle());
		assertEquals("test Community Content", community.getContent());
		assertNotNull(community.getCommunityUrl());
		assertNotNull(community.getCommunityUuid());
		assertNotNull(community.getMembersUrl());
		assertEquals(properties.getProperty("communityUuid"), community.getCommunityUuid());
	}

	@Ignore
	@Test
	public final void testGetCommunityByInvalidId() throws Exception {

		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity("2344invalid");
		assertNotNull(community);
		assertNull(community.getData());
	}

	@Ignore
	@Test
	public final void testGetPublicCommunities() throws Exception {
		CommunityService communityService = new CommunityService();

		List<Community> communities;
		try {
			communities = Arrays.asList(communityService.getPublicCommunities());
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());

		}
	}

	@Ignore
	@Test
	public final void testGetPublicCommunitiesByParameters() throws Exception {
		CommunityService communityService = new CommunityService();

		List<Community> communities;
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("tag", "test");
			parameters.put("ps", "5");
			communities = Arrays.asList(communityService.getPublicCommunities(parameters));
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());

		}
	}

	@Ignore
	@Test
	public final void testGetMyCommunities() throws Exception {
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));

		List<Community> communities;
		try {
			communities = Arrays.asList(communityService.getMyCommunities());
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());

		}
	}

	@Ignore
	@Test
	public final void testGetCommunityBookmarks() throws Exception {
		CommunityService communityService = new CommunityService();
		List<Bookmark> bookmarks;
		try {
			Community community = communityService
					.getCommunity(properties.getProperty("communityUuid"), true);
			bookmarks = Arrays.asList(communityService.getBookmarks(community));
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (Bookmark bookmark : bookmarks) {
			assertNotNull(bookmark.getTitle());
			assertNotNull(bookmark.getSummary());
		}
	}

	@Ignore
	@Test
	public final void testGetCommunityForumTopics() throws Exception {
		CommunityService communityService = new CommunityService();
		List<ForumTopic> forumTopics;
		try {
			Community community = communityService
					.getCommunity(properties.getProperty("communityUuid"), true);
			forumTopics = Arrays.asList(communityService.getForumTopics(community));
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (ForumTopic forumTopic : forumTopics) {
			assertNotNull(forumTopic.getTitle());
		}
	}

	@Ignore
	@Test
	public final void testGetMembers() throws Exception {
		CommunityService communityService = new CommunityService();
		List<Member> members;
		try {
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"));
			members = Arrays.asList(communityService.getMembers(community));
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (Member member : members) {
			assertNotNull(member.getId());
			assertNotNull(member.getName());
		}
	}

}
