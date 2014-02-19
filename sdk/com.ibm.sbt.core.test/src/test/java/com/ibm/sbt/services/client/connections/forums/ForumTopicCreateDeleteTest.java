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
public class ForumTopicCreateDeleteTest extends BaseForumServiceTest {

	@Test
	public void testCreateForumTopic() {
		try {
			forum = createForum();
			ForumTopic topic = new ForumTopic(forumService);
			topic.setTitle("Test topic titile" + System.currentTimeMillis());
			topic.setContent("Test topic content");
			topic.setForumUuid(forum.getForumUuid());
			ForumTopic topicReturned = forumService.createForumTopic(topic);
			assertNotNull(topicReturned.getTitle());
			assertEquals(unRandomize(topic.getTitle()), unRandomize(topicReturned.getTitle()));
			assertEquals(topic.getContent(), topicReturned.getContent().trim());
			deleteForumTopic(topicReturned);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling forumService.createForumTopic() caused by: "+e.getMessage());
		}
	}

	@Test
	public void testDeleteForumTopic() throws Exception {
		try {
			ForumTopic createdTopic = createForumTopic();
			ForumTopic topicGot = forumService.getForumTopic(createdTopic.getTopicUuid());
			assertEquals(createdTopic.getTitle(), topicGot.getTitle());
			forumService.removeForumTopic(createdTopic);
			forumService.getForumTopic(createdTopic.getTopicUuid());
			fail("Getting a Topic that does no longer exist should throw an exception.");
		}
		catch(Exception e) {
			assertNotNull(e.getMessage());
		}
	}
	
}
