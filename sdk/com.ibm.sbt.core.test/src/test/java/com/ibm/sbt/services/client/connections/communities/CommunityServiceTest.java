package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
<<<<<<< HEAD
import static org.junit.Assert.fail;

=======
>>>>>>> 74625fd... added sub community creation support
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
<<<<<<< HEAD

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
=======
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
>>>>>>> 74625fd... added sub community creation support
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Communities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class CommunityServiceTest extends BaseUnitTest {

	private Community community;
	private CommunityService communityService;

	@Before
	public void createTestData() throws Exception {
		if (communityService==null){
			communityService = new CommunityService();
		}
		community = new Community(communityService, "");
		community.setTitle("JavaTestCommunity " + System.currentTimeMillis());
		community.setContent("Java Community Content");
		String type = "public";
		if (TestEnvironment.isSmartCloudEnvironment()) {
			type = "private";
		}
		community.setCommunityType(type);
		community = community.save();
		community = community.load();
	}

    @Test
    public final void testGetCommunityById() throws Exception {
        Community retrieved = communityService.getCommunity(community
                .getCommunityUuid());
        Assert.assertTrue(retrieved.getTitle().startsWith("JavaTestCommunity"));
        assertEquals(community.getContent(), retrieved.getContent());
        assertEquals(community.getTags(), retrieved.getTags());
    }

    @Test
    public final void testUpdateCommunity() throws Exception {
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
        String id = TestEnvironment.getSecondaryUserEmail();
        if (TestEnvironment.isSmartCloudEnvironment())
            id = TestEnvironment.getSecondaryUserUuid();
        Member newMember = new Member(communityService,
                id);
        newMember.setTitle("test Title" + System.currentTimeMillis());
        newMember.setName("test name" + System.currentTimeMillis());
        communityService.addMember(community.getCommunityUuid(), newMember);
        EntityList<Member> members = communityService.getMembers(community
                .getCommunityUuid());
        for (Member member : members) {
            assertNotNull(member.getUserid());
            assertNotNull(member.getName());
        }
        communityService.updateMember(community.getCommunityUuid(), newMember);
        communityService.removeMember(community.getCommunityUuid(),
                id);
    }

    @Test
    public final void testSubs() throws Exception {
        
        Community sub  = new Community(communityService, "");
        sub.setTitle("JavaTestCommunity " + System.currentTimeMillis());
        sub.setContent("Java Community Content");
        String type = "public";
        if (TestEnvironment.isSmartCloudEnvironment()) {
            type = "private";
        }
        sub.setCommunityType(type);
        sub.setAsString(CommunityXPath.parentCommunityUrl, community.getCommunityUrl());
        sub = sub.save();
        sub = sub.load();
        //we set the url to html page but receive a link to atom
        Assert.assertTrue(sub.getParentCommunityUrl().contains(community.getCommunityUuid()));
        communityService.deleteCommunity(sub.getCommunityUuid());
 
    }
    
	@Test
	public final void testAddRemoveMember() throws Exception {
		String id = TestEnvironment.getSecondaryUserEmail();
		if (TestEnvironment.isSmartCloudEnvironment()) id = TestEnvironment.getSecondaryUserUuid();
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

	@After
	public void deleteTestData() throws Exception {
		TestEnvironment.setRequiresAuthentication(true);

		if (community != null) {
			communityService.deleteCommunity(community.getCommunityUuid());
		}
	}
}
