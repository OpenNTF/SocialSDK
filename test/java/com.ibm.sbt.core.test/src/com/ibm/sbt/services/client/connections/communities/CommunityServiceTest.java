package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.ProfileService;

/**
 * Tests for the java connections Communities API a test class provides its own tests extending the test
 * endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class CommunityServiceTest extends BaseUnitTest{

	@Test
	public final void testGetCommunityById() throws Exception{

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
	public final void testGetCommunityByInvalidId() throws Exception{

		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity("2344invalid");    
		assertNotNull(community);
		assertNull(community.getData());
	}

	@Test
	public final void testGetPublicCommunities() throws Exception{
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

	@Test
	public final void testGetPublicCommunitiesByParameters() throws Exception{
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

	@Test
	public final void testGetMyCommunities() throws Exception{
		CommunityService communityService = new CommunityService();
		authenticateEndpoint(communityService.getEndpoint(), properties.getProperty("user1"), properties.getProperty("passwordUser1"));

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

	@Test
	public final void testGetCommunityBookmarks() throws Exception{
		CommunityService communityService = new CommunityService();
		List<Bookmark> bookmarks;
		try {
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"), true);
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
	
	@Test
	public final void testGetCommunityForumTopics() throws Exception{
		CommunityService communityService = new CommunityService();
		List<ForumTopic> forumTopics;
		try {
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"), true);
			forumTopics = Arrays.asList(communityService.getForumTopics(community));
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}

		for (ForumTopic forumTopic : forumTopics) {
			assertNotNull(forumTopic.getTitle());		
		}
	}

	@Test
	public final void testGetMembers() throws Exception{
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
	
	@Test
	public final void testCreateCommuniy() throws Exception{
		CommunityService communityService = new CommunityService();
		boolean result;
		try {
			Community community = communityService.getCommunity("", false);
			community.setTitle("testCommunity");
			community.setContent("test Community Content");
			result = communityService.createCommunity(community);
		
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}
		assertEquals(true, result);	
	}
	
	@Test
	public final void testUpdateCommuniy() throws Exception{
		CommunityService communityService = new CommunityService();
		boolean result;
		try {			
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"));
			community.setTitle("test Title");
			community.setContent("test Content");
			result = communityService.updateCommunity(community);
		
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}
		assertEquals(true, result);	
	}

	@Test
	public final void testAddMember() throws Exception{
		CommunityService communityService = new CommunityService();
		boolean result;
		try {			
			Member member = new Member(communityService, properties.getProperty("email1"));
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"), false);
			
			result = communityService.addMember(community,member);
		
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}
		assertEquals(true, result);	
	}

	@Test
	public final void testRemoveMember() throws Exception{
		CommunityService communityService = new CommunityService();
		boolean result;
		try {			
			Member member = new Member(communityService, properties.getProperty("email1"));
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"), false);
			result = communityService.removeMember(community, member);
		
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}
		assertEquals(true, result);	
	}
	
	@Test
	public final void testDeleteCommunity() throws Exception{
		CommunityService communityService = new CommunityService();
		boolean result;
		try {			
			Community community = communityService.getCommunity(properties.getProperty("communityUuid"), false);
			result = communityService.deleteCommunity(community);
		
		} catch (XMLException e) {
			fail(e.getMessage());
			return;
		}
		assertEquals(true, result);	
	}
}
