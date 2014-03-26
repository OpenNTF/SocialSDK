/*
 * ������ Copyright IBM Corp. 2013
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
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class ForumReplyGetUpdateTest extends BaseForumServiceTest {

	@Before
	public void initForum() throws ForumServiceException {
		reply = createForumReply();
	}

	@Test
	public void testGetForumReply() throws ForumServiceException {
		ForumReply replyGot = forumService.getForumReply(reply.getReplyUuid());
		assertEquals(reply.getTitle(), replyGot.getTitle());
		assertEquals(reply.getContent(), replyGot.getContent());
	}

	@Test
	public void testUpdateReply() throws ForumServiceException {
		reply.setTitle("new Test reply title" + System.currentTimeMillis());
		reply.setContent("new Test reply content" + System.currentTimeMillis());
		forumService.updateForumReply(reply);
		ForumReply updatedReply = forumService.getForumReply(reply.getReplyUuid());
		assertEquals(unRandomize(reply.getTitle()), unRandomize(updatedReply.getTitle()));
		assertEquals(reply.getReplyUuid(), updatedReply.getReplyUuid());
		assertEquals(unRandomize(reply.getContent()), unRandomize(updatedReply.getContent().trim()));
	}

	@After
	public void deleteReplyOnExit() throws ForumServiceException {
		deleteForumReply(reply);
	}
}
