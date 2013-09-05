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
package com.ibm.sbt.test.java.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.java.connections.communities.AddMember;
import com.ibm.sbt.test.java.connections.communities.RemoveMember;
import com.ibm.sbt.test.java.connections.communities.CreateCommunity;
import com.ibm.sbt.test.java.connections.communities.GetCommunityBookmarks;
import com.ibm.sbt.test.java.connections.communities.GetForumTopics;
import com.ibm.sbt.test.java.connections.communities.GetCommunityMembers;
import com.ibm.sbt.test.java.connections.communities.GetMyCommunities;
import com.ibm.sbt.test.java.connections.communities.GetPublicCommunities;
import com.ibm.sbt.test.java.connections.communities.UpdateCommunityTags;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ AddMember.class, RemoveMember.class, CreateCommunity.class, GetCommunityBookmarks.class, GetForumTopics.class,
        GetCommunityMembers.class, GetMyCommunities.class, GetPublicCommunities.class, UpdateCommunityTags.class })
public class CommunitiesTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}
