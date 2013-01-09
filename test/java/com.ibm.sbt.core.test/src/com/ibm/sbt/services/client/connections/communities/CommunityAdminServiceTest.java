package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.sbt.services.BaseUnitTest;

public class CommunityAdminServiceTest extends BaseUnitTest {

	@Ignore
	@Test
	public final void testCreateCommuniy() throws Exception {
		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity("", false);
		community.setTitle("testCommunity");
		community.setContent("test Community Content");
		community = communityService.createCommunity(community, true);
		assertEquals("testCommunity", community.getTitle());
		assertEquals("test Community Content", community.getContent());

	}

	@Ignore
	@Test
	public final void testUpdateCommuniy() throws Exception {
		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity(properties.getProperty("communityUuid"));
		community.setTitle("test Title");
		community.setContent("test Content");
		assertTrue(communityService.updateCommunity(community));

	}

	@Ignore
	@Test
	public final void testAddMember() throws Exception {
		CommunityService communityService = new CommunityService();
		Member member = new Member(communityService, properties.getProperty("email1"));
		Community community = communityService.getCommunity(properties.getProperty("communityUuid"), false);
		assertTrue(communityService.addMember(community, member));
	}

	@Ignore
	@Test
	public final void testRemoveMember() throws Exception {
		CommunityService communityService = new CommunityService();
		Member member = new Member(communityService, properties.getProperty("email1"));
		Community community = communityService.getCommunity(properties.getProperty("communityUuid"), false);
		assertTrue(communityService.removeMember(community, member));
	}

	@Ignore
	@Test
	public final void testDeleteCommunity() throws Exception {
		CommunityService communityService = new CommunityService();
		Community community = communityService.getCommunity(properties.getProperty("communityUuid"), false);
		assertTrue(communityService.deleteCommunity(community));
	}

}
