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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.wikis.Wiki;
import com.ibm.sbt.services.client.connections.wikis.WikiService;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

/**
 * @author Swati Singh
 *
 */
public class ForumReplyCRUDTest extends BaseForumServiceTest {

	@Before
	public void initForum() {
		reply = createForumReply();
	}

	@Test
	public void CreateForumReplyTest() {
		try {
			ForumReply reply = new ForumReply(forumService);
			reply.setTitle("Test reply title" + System.currentTimeMillis());
			reply.setContent("Test reply content" + System.currentTimeMillis());
			reply.setReplyToPostUuid(topic.getTopicUuid());
			ForumReply replyReturned = forumService.createForumReply(reply);
			assertNotNull(replyReturned.getTitle());
			assertEquals(reply.getTitle(), replyReturned.getTitle().trim());
			assertEquals(reply.getContent(), replyReturned.getContent().trim());
			deleteForumReply(replyReturned);
		} catch (Exception e) {
			fail("Error calling forumService.createForumReply() caused by: "+e.getMessage());
		}

	}

	@Test
	public void GetForumReply() {
		try {
			ForumReply replyGot = forumService.getForumReply(reply.getReplyUuid());
			assertEquals(reply.getTitle(), replyGot.getTitle());
			assertEquals(reply.getContent(), replyGot.getContent());
		} catch (Exception e) {
			fail("Error calling forumService.getForum() caused by: "+e.getMessage());
		}
	}

	@Test
	public void UpdateReply() {
		try {
			reply.setTitle("new Test reply title" + System.currentTimeMillis());
			reply.setContent("new Test reply content" + System.currentTimeMillis());
			forumService.updateForumReply(reply);
			ForumReply updatedReply = forumService.getForumReply(reply.getReplyUuid());
			assertEquals(reply.getTitle(), updatedReply.getTitle());
			assertEquals(reply.getReplyUuid(), updatedReply.getReplyUuid());
			assertEquals(reply.getContent(), updatedReply.getContent().trim());
		} catch (Exception e) {
			fail("Error calling forumService.updateForumReply() caused by: "+e.getMessage());
		}
	}


	@Test
	public void deleteReply() throws Exception {
		try {
			ForumReply createdReply = createForumReply();
			ForumReply replyGot = forumService.getForumReply(createdReply.getReplyUuid());
			assertEquals(createdReply.getTitle(), replyGot.getTitle());
			forumService.removeForumReply(createdReply);
			forumService.getForumReply(createdReply.getReplyUuid());
			fail("Getting a Reply that does no longer exist should throw an exception.");
		}
		catch(Exception e) {
			assertNotNull(e.getMessage());
		}
	}

	@After
	public void deleteRepyOnExit() {
		try {
			forumService.removeForumReply(reply.getReplyUuid());
		} catch (ForumServiceException e) {
			fail("Error calling forumService.removeForum() caused by: "+e.getMessage());
		}

	}


}
