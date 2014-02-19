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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class ForumReplyCreateDeleteTest extends BaseForumServiceTest {

	@Test
	public void testCreateForumReply() {
		try {
			topic = createForumTopic();
			ForumReply reply = new ForumReply(forumService);
			reply.setTitle("Test reply title" + System.currentTimeMillis());
			reply.setContent("Test reply content" + System.currentTimeMillis());
			reply.setReplyToPostUuid(topic.getTopicUuid());
			ForumReply replyReturned = forumService.createForumReply(reply);
			assertNotNull(replyReturned.getTitle());
			assertEquals(unRandomize(reply.getTitle()), unRandomize(replyReturned.getTitle().trim()));
			assertEquals(unRandomize(reply.getContent()), unRandomize(replyReturned.getContent().trim()));
			deleteForumReply(replyReturned);
		} catch (Exception e) {
			fail("Error calling forumService.createForumReply() caused by: "+e.getMessage());
		}
	}

	@Test
	public void testDeleteForumReply() throws Exception {
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
}
