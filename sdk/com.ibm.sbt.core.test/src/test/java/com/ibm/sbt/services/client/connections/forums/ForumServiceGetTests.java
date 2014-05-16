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

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Tag;

/**
 * @author Swati Singh
 *
 */
public class ForumServiceGetTests extends BaseForumServiceTest {

	@Test
	public void testGetAllForums() throws Exception {
		EntityList<Forum> entries = forumService.getAllForums();
		assertNotNull(entries);
		for (Forum forum : entries) {
			assertValid(forum);
		}
	}

	@Test
	public void testGetMyForums() throws Exception {
		EntityList<Forum> entries = forumService.getMyForums();
		assertNotNull(entries);
		for (Forum forum : entries) {
			assertValid(forum);
		}
	}

	@Test
	public void testGetPublicForums() throws Exception {
		EntityList<Forum> entries = forumService.getPublicForums();
		assertNotNull(entries);
		for (Forum forum : entries) {
			assertValid(forum);
		}
	}

	@Test
	public void testGetPublicForumTopics() throws Exception {
		EntityList<ForumTopic> entries = forumService.getPublicForumTopics();
		assertNotNull(entries);
		for (ForumTopic topic : entries) {
			assertValid(topic);
		}
	}

	@Test
	public void testGetMyForumTopics() throws Exception {
		EntityList<ForumTopic> entries = forumService.getMyForumTopics();
		assertNotNull(entries);
		for (ForumTopic topic : entries) {
			assertValid(topic);
		}
	}


	@Test
	public void testGetForumReplies() throws Exception {
		EntityList<ForumTopic> topics = forumService.getMyForumTopics();
		ForumTopic topic = (ForumTopic) topics.iterator().next();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("topicUuid", topic.getTopicUuid());
		
		EntityList<ForumReply> entries = forumService.getForumReplies(parameters);
		assertNotNull(entries);
		for (ForumReply reply : entries) {
			assertValid(reply);
		}
	}

	@Test
	public void testGetRecommendations() throws Exception {
		EntityList<ForumTopic> topics = forumService.getMyForumTopics();
		ForumTopic topic = (ForumTopic) topics.get(0);
		EntityList<Recommendation> recommendations = forumService.getRecommendations(topic.getTopicUuid());
		assertNotNull(recommendations);
		for (Recommendation recommendation : recommendations) {
			assertValid(recommendation);
		}
	}

	@Test
	public void testGetForumsTags() throws Exception {
		EntityList<Tag> entries = forumService.getForumsTags();
		assertNotNull(entries);
		for (Tag tag : entries) {
			assertValid(tag);
		}
	}

	@Test
	public void testGetForumTopicsTags() throws Exception {
		EntityList<Forum> forums = forumService.getPublicForums();
		Forum forum = (Forum) forums.get(0);
		EntityList<Tag> entries = forumService.getForumTopicsTags(forum.getForumUuid());
		assertNotNull(entries);
		for (Tag tag : entries) {
			assertValid(tag);
		}
	}
}
