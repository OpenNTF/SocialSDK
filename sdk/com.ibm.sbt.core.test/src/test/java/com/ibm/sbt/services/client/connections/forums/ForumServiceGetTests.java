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

package com.ibm.sbt.services.client.connections.forums;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;

/**
 * @author Swati Singh
 *
 */
public class ForumServiceGetTests extends BaseForumServiceTest {


	@Test
	public void getAllForums() {
		try {
			ForumList entries = forumService.getAllForums();
			assertNotNull(entries);
			for (BaseForumEntity forum : entries) {
				assertValid((Forum)forum);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getAllForums() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getMyForums() {
		try {
			ForumList entries = forumService.getMyForums();
			assertNotNull(entries);
			for (BaseForumEntity forum : entries) {
				assertValid((Forum)forum);
			}
		} catch (Exception e) {
			fail("Error calling  forumService.getMyForums() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getPublicForums() {
		try {
			ForumList entries = forumService.getPublicForums();
			assertNotNull(entries);
			for (BaseForumEntity forum : entries) {
				assertValid((Forum)forum);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getPublicForums() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getPublicForumTopics() {
		try {
			TopicList entries = forumService.getPublicForumTopics();
			assertNotNull(entries);
			for (BaseForumEntity topic : entries) {
				assertValid((ForumTopic)topic);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getPublicForumTopics() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getMyForumTopics() {
		try {
			TopicList entries = forumService.getMyForumTopics();
			assertNotNull(entries);
			for (BaseForumEntity topic : entries) {
				assertValid((ForumTopic)topic);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getMyForumTopics() caused by: "+e.getMessage());
		}
	}


	@Test
	public void getForumReplies() {
		try {
			TopicList topics = forumService.getMyForumTopics();
			ForumTopic topic = (ForumTopic) topics.iterator().next();
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("topicUuid", topic.getTopicUuid());
			
			ReplyList entries = forumService.getForumReplies(parameters);
			assertNotNull(entries);
			for (BaseForumEntity reply : entries) {
				assertValid((ForumReply)reply);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getForumReplies() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getRecommendations() {
		try {
			TopicList topics = forumService.getMyForumTopics();
			ForumTopic topic = (ForumTopic) topics.get(0);
			RecommendationList recommendations = forumService.getRecommendations(topic.getTopicUuid());
			assertNotNull(recommendations);
			for (Recommendation recommendation : recommendations) {
				assertValid(recommendation);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getRecommendations() caused by: "+e.getMessage());
		}
	}


	@Test
	public void getForumsTags() {
		try {
			TagList entries = forumService.getForumsTags();
			assertNotNull(entries);
			for (Tag tag : entries) {
				assertValid((Tag)tag);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getForumsTags() caused by: "+e.getMessage());
		}
	}


	@Test
	public void getForumTopicsTags() {
		try {
			ForumList forums = forumService.getPublicForums();
			Forum forum = (Forum) forums.get(0);
			TagList entries = forumService.getForumTopicsTags(forum.getForumUuid());
			assertNotNull(entries);
			for (Tag tag : entries) {
				assertValid((Tag)tag);
			}
		} catch (Exception e) {
			fail("Error calling forumService.getForumTopicsTags() caused by: "+e.getMessage());
		}
	}



}
