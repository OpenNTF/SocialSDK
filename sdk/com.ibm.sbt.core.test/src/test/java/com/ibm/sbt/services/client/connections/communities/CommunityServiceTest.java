/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */

package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * Tests for the java connections Communities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class CommunityServiceTest extends BaseCommunityServiceTest {

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
        
        community.setContent("new content");
        communityService.updateCommunity(community);
        community = community.load();
        assertEquals("new content", community.getContent());
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
        Assert.assertTrue(sub.getParentCommunityUrl().contains(unRandomize(community.getCommunityUuid())));
        communityService.deleteCommunity(sub.getCommunityUuid());
 
    }

    @After
    public void deleteTestData() throws Exception {
        TestEnvironment.setRequiresAuthentication(true);

        if (community != null) {
            communityService.deleteCommunity(community.getCommunityUuid());
        }
    }
}