/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.base.CommunityEntryDataHandler;
import com.ibm.sbt.test.js.base.CommunityFeedDataHandler;
import com.ibm.sbt.test.js.base.CommunityInvitesFeedDataHandler;
import com.ibm.sbt.test.js.base.CommunityMembersFeedDataHandler;
import com.ibm.sbt.test.js.connections.communities.GetCommunityEvents;
import com.ibm.sbt.test.js.connections.communities.GetCommunityForumTopics;
import com.ibm.sbt.test.js.connections.communities.api.AddMember;
import com.ibm.sbt.test.js.connections.communities.api.Community;
import com.ibm.sbt.test.js.connections.communities.api.CreateCommunity;
import com.ibm.sbt.test.js.connections.communities.api.CreateCommunityInvalidTitle;
import com.ibm.sbt.test.js.connections.communities.api.CreateCommunityJson;
import com.ibm.sbt.test.js.connections.communities.api.CreateCommunityLoadIt;
import com.ibm.sbt.test.js.connections.communities.api.CreateCommunityNew;
import com.ibm.sbt.test.js.connections.communities.api.CreateForumTopic;
import com.ibm.sbt.test.js.connections.communities.api.CreateInvite;
import com.ibm.sbt.test.js.connections.communities.api.CrudCommunity;
import com.ibm.sbt.test.js.connections.communities.api.DeleteCommunity;
import com.ibm.sbt.test.js.connections.communities.api.GetAllInvites;
import com.ibm.sbt.test.js.connections.communities.api.GetCommunity;
import com.ibm.sbt.test.js.connections.communities.api.GetCommunityEvent;
import com.ibm.sbt.test.js.connections.communities.api.GetForums;
import com.ibm.sbt.test.js.connections.communities.api.GetMembers;
import com.ibm.sbt.test.js.connections.communities.api.GetForumTopics;
import com.ibm.sbt.test.js.connections.communities.api.GetMyCommunities;
import com.ibm.sbt.test.js.connections.communities.api.GetMyInvites;
import com.ibm.sbt.test.js.connections.communities.api.GetPublicCommunities;
import com.ibm.sbt.test.js.connections.communities.api.GetSaveCommunity;
import com.ibm.sbt.test.js.connections.communities.api.GetSubCommunities;
import com.ibm.sbt.test.js.connections.communities.api.NewSaveCommunity;
import com.ibm.sbt.test.js.connections.communities.api.RemoveMember;
import com.ibm.sbt.test.js.connections.communities.api.RemoveInvite;
import com.ibm.sbt.test.js.connections.communities.api.UpdateCommunity;
import com.ibm.sbt.test.js.connections.communities.api.UpdateCommunityJson;
import com.ibm.sbt.test.js.connections.communities.api.UpdateCommunityTags;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
    //ValidateBaseCommunitiesTest.class,

    GetPublicCommunities.class,
    GetMyCommunities.class, 
    GetMyInvites.class, 
    GetAllInvites.class,
    GetMembers.class, 
    GetSubCommunities.class,
    GetCommunityEvents.class,

    GetCommunity.class, 
    Community.class,

    CreateCommunity.class,
    CreateCommunityNew.class,
    CreateCommunityJson.class,
    CreateCommunityLoadIt.class,
    CreateCommunityInvalidTitle.class,
    DeleteCommunity.class,
    UpdateCommunity.class,
    UpdateCommunityJson.class,
    UpdateCommunityTags.class,
    NewSaveCommunity.class,
    GetSaveCommunity.class,
    CrudCommunity.class,
    //com.ibm.sbt.test.js.connections.communities.api.GetCommunityEvents.class,
    //GetCommunityEvent.class,
    
    CreateInvite.class,
    RemoveInvite.class,
    
    GetCommunityForumTopics.class,
    GetForums.class,
    GetForumTopics.class,
    CreateForumTopic.class,
    
    AddMember.class,
    RemoveMember.class
})
public class CommunitiesTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
