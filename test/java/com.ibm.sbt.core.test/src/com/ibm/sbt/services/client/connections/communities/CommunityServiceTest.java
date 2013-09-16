package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import org.junit.Ignore;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.MemberList;
import com.ibm.sbt.services.client.connections.communities.Member;
import com.ibm.sbt.services.client.connections.communities.CommunityList;
import com.ibm.sbt.services.client.connections.communities.BookmarkList;
import com.ibm.sbt.services.client.connections.communities.Bookmark;
import com.ibm.sbt.services.client.connections.communities.Invite;
import com.ibm.sbt.services.client.connections.communities.InviteList;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.forums.TopicList;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
/**
 * Tests for the java connections Communities API a test class provides its own tests extending the test
 * endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class CommunityServiceTest extends BaseUnitTest {
	
	

	@Test
	public final void testGetCommunityById() throws Exception {

		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
	
		Community community = new Community(communityService, "");
		community.setTitle("testCommunity"+System.currentTimeMillis());
		community.setContent("test Community Content");
		community = community.save();
		community = communityService.getCommunity(community.getCommunityUuid());
		assertEquals(true, community.getTitle().startsWith("testCommunity"));
		assertEquals("test Community Content", community.getContent());
		
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
		CommunityList communities = communityService.getPublicCommunities(parameters);
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
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		CommunityList communities = communityService.getMyCommunities();
		for (Community community : communities) {
			assertNotNull(community.getTitle());
			assertNotNull(community.getCommunityUrl());
			assertNotNull(community.getCommunityUuid());
			assertNotNull(community.getMembersUrl());

		}
	}

	@Test
	public final void testGetCommunityBookmarks() throws Exception {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getPublicCommunities();
		Community community = communities.iterator().next();
		
		BookmarkList bookmarks = communityService.getBookmarks(community.getCommunityUuid());
		
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
		
		TopicList forumTopics = communityService.getForumTopics(community.getCommunityUuid());

		for (BaseForumEntity forumTopic : forumTopics) {
			assertNotNull(forumTopic.getTitle());
		}
	}

	@Test
	public final void testGetMyInvites() throws Exception {
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
	
		InviteList invites = communityService.getMyInvites();
		if(invites.getTotalResults() > 0){
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
		
		MemberList members = communityService.getMembers(community.getCommunityUuid());

		for (Member member : members) {
			assertNotNull(member.getUserid());
			assertNotNull(member.getName());
		}
	}
	
	@Test
	public final void testCreateCommuniy() throws Exception {
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
	
		Community community = new Community(communityService, "");
		community.setTitle("testCommunity"+System.currentTimeMillis());
		community.setContent("test Community Content");
		community.setCommunityType("public");
		community = community.save();
		assertEquals(true, community.getTitle().startsWith("testCommunity"));
		assertEquals("test Community Content", community.getContent());

	}

	@Ignore
	@Test
	public final void testUpdateCommuniy() throws Exception {
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		CommunityList communities = communityService.getMyCommunities();
		Community community = communities.iterator().next();
		community.setTitle("test Title");
		community.setContent("test Content");
		communityService.updateCommunity(community);
		community = community.load();
		assertEquals("test Title", community.getTitle());
		assertEquals("test Content", community.getContent());
	}

	@Test
	public final void testAddRemoveMember() throws Exception {
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
	
		CommunityList communities = communityService.getMyCommunities();
		Community community = communities.iterator().next();
		Member newMember = new Member(communityService, properties.getProperty("email2"));
		communityService.addMember(community.getCommunityUuid(), newMember );
		MemberList members = communityService.getMembers(community.getCommunityUuid());
		for (Member member : members) {
			assertNotNull(member.getUserid());
			assertNotNull(member.getName());
		}
		communityService.removeMember(community.getCommunityUuid(), properties.getProperty("email2"));
	}


	@Test
	public final void testDeleteCommunity() throws Exception {
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"),
				properties.getProperty("passwordUser1"));
		CommunityList communities = communityService.getMyCommunities();
		Community community = communities.iterator().next();
	
		try {
			communityService.deleteCommunity(community.getCommunityUuid());
			community = communityService.getCommunity(properties.getProperty("communityUuid"));
		} catch (CommunityServiceException e) {
			if (e.getCause() instanceof ClientServicesException) {
				assertEquals(404, ((ClientServicesException)e.getCause()).getResponseStatusCode());
				return;
			} else {
				throw e;
			}
		}
		fail("Community found");
	}

}
