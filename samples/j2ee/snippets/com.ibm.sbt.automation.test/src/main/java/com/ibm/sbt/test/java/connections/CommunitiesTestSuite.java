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
package com.ibm.sbt.test.java.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.java.connections.communities.AcceptInvite;
import com.ibm.sbt.test.java.connections.communities.AddMember;
import com.ibm.sbt.test.java.connections.communities.RemoveMember;
import com.ibm.sbt.test.java.connections.communities.CreateCommunity;
import com.ibm.sbt.test.java.connections.communities.CreateForumTopic;
import com.ibm.sbt.test.java.connections.communities.CreateInvite;
import com.ibm.sbt.test.java.connections.communities.DeclineInvite;
import com.ibm.sbt.test.java.connections.communities.GetBookmarks;
import com.ibm.sbt.test.java.connections.communities.GetCommunity;
import com.ibm.sbt.test.java.connections.communities.GetCommunityDataByEntity;
import com.ibm.sbt.test.java.connections.communities.GetForums;
import com.ibm.sbt.test.java.connections.communities.GetForumTopics;
import com.ibm.sbt.test.java.connections.communities.GetMembers;
import com.ibm.sbt.test.java.connections.communities.GetMyCommunities;
import com.ibm.sbt.test.java.connections.communities.GetMyInvites;
import com.ibm.sbt.test.java.connections.communities.GetPublicCommunities;
import com.ibm.sbt.test.java.connections.communities.GetSubCommunities;
import com.ibm.sbt.test.java.connections.communities.RemoveCommunity;
import com.ibm.sbt.test.java.connections.communities.UpdateCommunity;
import com.ibm.sbt.test.java.connections.communities.UpdateCommunityLogo;
import com.ibm.sbt.test.java.connections.communities.UpdateMember;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ AcceptInvite.class, AddMember.class, RemoveMember.class, CreateCommunity.class, CreateForumTopic.class, CreateInvite.class, DeclineInvite.class, GetBookmarks.class,
	GetCommunity.class, GetForumTopics.class,  GetCommunityDataByEntity.class, GetForums.class, GetMembers.class, GetMyCommunities.class, GetMyInvites.class, GetPublicCommunities.class, 
	GetSubCommunities.class, RemoveCommunity.class, UpdateCommunity.class , UpdateCommunityLogo.class, UpdateMember.class})
public class CommunitiesTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}
