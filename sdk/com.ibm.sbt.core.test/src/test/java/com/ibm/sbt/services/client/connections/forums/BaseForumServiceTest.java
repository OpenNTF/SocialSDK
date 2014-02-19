/*
 * �� Copyright IBM Corp. 2013
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

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

import com.ibm.sbt.services.BaseUnitTest;

/**
 * @author Swati Singh
 *
 */
public class BaseForumServiceTest extends BaseUnitTest {
	protected ForumService forumService;
	protected Forum forum;
	protected ForumTopic topic;
	protected ForumReply reply;

	@Before
	public void initForumServiceTest() {
		if (forumService==null){
			forumService = new ForumService();
		}
	}

	protected Forum newForum() {
		Forum forum = new Forum(forumService);

		try{
			forum.setTitle("Test forum 1ab" + System.currentTimeMillis());
			forum.setContent("Test forum created by Create Forum Java snippet");
			List<String> tags = new ArrayList<String>();
			tags.add("tag1"); 
			tags.add("tag2"); 
			forum.setTags(tags);
			forum = forum.save(); 
		} catch (Exception e) {
			fail("Error calling forum.save() caused by: "+e.getMessage());
		}

		return forum;
	}

	protected Forum createForum() {
		return createForum(newForum());
	}

	protected Forum createForum(Forum forum) {
		try {
			forum.setTitle("Test forum 1ab" + System.currentTimeMillis());
			forum.setContent("Test forum created by Create Forum Java snippet");
			List<String> tags = new ArrayList<String>();
			tags.add("tag1"); 
			tags.add("tag2"); 
			forum.setTags(tags);
			forum = forumService.createForum(forum);
			return forum;
		} catch (ForumServiceException e) {
			throw new RuntimeException("Failed to create a forum.", e);
		}
	}

	protected ForumTopic createForumTopic() {
		forum = createForum();
		ForumTopic topic = new ForumTopic(forumService);

		try{
			topic.setTitle("Test topic title" + System.currentTimeMillis());
			topic.setContent("Test topic Content"+ System.currentTimeMillis());
			topic.setForumUuid(forum.getForumUuid());
			topic = forumService.createForumTopic(topic);
			
		} catch (Exception e) {
			fail("Error calling forumService.createForumTopic() caused by: "+e.getMessage());
		}
		return topic;
	}
	
	protected ForumReply createForumReply() {
		topic = createForumTopic();
		ForumReply reply = new ForumReply(forumService);

		try{
			reply.setTitle("Test topic title" + System.currentTimeMillis());
			reply.setContent("Test topic Content"+ System.currentTimeMillis());
			reply.setTopicUuid(topic.getTopicUuid());
			reply = forumService.createForumReply(reply);
		} catch (Exception e) {
			fail("Error calling forumService.createForumReply() caused by: "+e.getMessage());
		}
		return reply;

	}
	
	protected void deleteForum(Forum forum) {
		try {
			forumService.removeForum(forum.getForumUuid());
		} catch (ForumServiceException e) {
			fail("Error calling forumService.removeForum() caused by: "+e.getMessage());
		}
	}
	
	protected void deleteForumTopic(ForumTopic topic) {
		try {
			forumService.removeForumTopic(topic.getTopicUuid());
		} catch (ForumServiceException e) {
			fail("Error calling forumService.removeForumTopic() caused by: "+e.getMessage());
		}
	}
	
	protected void deleteForumReply(ForumReply reply) {
		try {
			forumService.removeForumReply(reply.getReplyUuid());
		} catch (ForumServiceException e) {
			fail("Error calling forumService.removeForum() caused by: "+e.getMessage());
		}
	}
	
	protected void assertValid(Forum forum) {
		assertNotNull("Invalid forum id", forum.getId());
		assertNotNull("Invalid forum title", forum.getTitle());
		assertNotNull("Invalid forum updated date", forum.getUpdated());
	}
	
	protected void assertValid(ForumTopic topic) {
		assertNotNull("Invalid topic id", topic.getId());
		assertNotNull("Invalid topic title", topic.getTitle());
		assertNotNull("Invalid topic updated date", topic.getUpdated());
	}
	
	protected void assertValid(ForumReply reply) {
		assertNotNull("Invalid reply id", reply.getId());
		assertNotNull("Invalid reply title", reply.getTitle());
		assertNotNull("Invalid reply updated date", reply.getUpdated());
	}
	
	protected void assertValid(Tag tag) {
		assertNotNull("Invalid Tag", tag.getTerm());
		assertNotNull("Invalid Tag visibility", tag.getVisibility());
		assertNotNull("Invalid Tag frequency", tag.getFrequency());
		assertNotNull("Invalid Tag intensity", tag.getIntensity());
	}
	
	protected void assertValid(Recommendation recommendation) {
		assertNotNull("Invalid Name", recommendation.getName());
		assertNotNull("Invalid Id", recommendation.getId());
		assertNotNull("Invalid Email", recommendation.getEmail());
	}
}
