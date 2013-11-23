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

import com.ibm.sbt.test.js.connections.forums.CreateUpdateDeleteReply;
import com.ibm.sbt.test.js.connections.forums.CreateUpdateDeleteTopic;
import com.ibm.sbt.test.js.connections.forums.GetMyAnsweredTopics;
import com.ibm.sbt.test.js.connections.forums.GetMyForumEntries;
import com.ibm.sbt.test.js.connections.forums.GetMyTopicsRecommendations;
import com.ibm.sbt.test.js.connections.forums.StartAForum;
import com.ibm.sbt.test.js.connections.forums.api.CreateForum;
import com.ibm.sbt.test.js.connections.forums.api.CreateForumReply;
import com.ibm.sbt.test.js.connections.forums.api.CreateForumTopic;
import com.ibm.sbt.test.js.connections.forums.api.DeleteForum;
import com.ibm.sbt.test.js.connections.forums.api.DeleteForumReply;
import com.ibm.sbt.test.js.connections.forums.api.DeleteForumTopic;
import com.ibm.sbt.test.js.connections.forums.api.GetAllForums;
import com.ibm.sbt.test.js.connections.forums.api.GetForum;
import com.ibm.sbt.test.js.connections.forums.api.GetForumReply;
import com.ibm.sbt.test.js.connections.forums.api.GetForumTopic;
import com.ibm.sbt.test.js.connections.forums.api.GetMyForums;
import com.ibm.sbt.test.js.connections.forums.api.GetPublicForums;
import com.ibm.sbt.test.js.connections.forums.api.UpdateForum;
import com.ibm.sbt.test.js.connections.forums.api.UpdateForumReply;
import com.ibm.sbt.test.js.connections.forums.api.UpdateForumTopic;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	CreateUpdateDeleteReply.class,
	CreateUpdateDeleteTopic.class,
	GetMyAnsweredTopics.class,
	GetMyForumEntries.class,
	com.ibm.sbt.test.js.connections.forums.GetMyForums.class,
	com.ibm.sbt.test.js.connections.forums.GetMyTopics.class,
	GetMyTopicsRecommendations.class,
	StartAForum.class,
	GetMyForums.class,
	GetPublicForums.class, 
	GetAllForums.class,
	
	CreateForum.class,
	GetForum.class,
	UpdateForum.class,
	DeleteForum.class,
	
	CreateForumTopic.class,
	GetForumTopic.class,
	UpdateForumTopic.class,
	DeleteForumTopic.class,
	
	CreateForumReply.class,
	GetForumReply.class,
	UpdateForumReply.class,
	DeleteForumReply.class
})
public class ForumsTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
