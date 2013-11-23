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
import com.ibm.sbt.test.java.connections.forums.*;


/**
 * @author mkataria
 * 
 * @date Nov 14 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ CreateCommunityForumReply.class, CreateCommunityForumTopic.class, CreateForum.class, CreateReply.class, CreateTopic.class, ForumById.class,
	ForumTopicsTags.class,ForumsTags.class,GetMyForumEntities.class,GetRecommendations.class,
	MyForums.class,MyTopics.class,PublicForums.class,RemoveForum.class, RemoveReply.class,RemoveTopic.class,
	RepliesForATopic.class,ReplyById.class, TopicById.class,TopicsForAForum.class,UpdateForum.class,UpdateReply.class,
	UpdateTopic.class }) 
public class ForumsTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}
