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

import static com.ibm.sbt.services.client.base.ConnectionsConstants.v5_0;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class ForumTopicGetUpdateTest extends BaseForumServiceTest {

	@Before
	public void initForum() throws Exception {
		topic = createForumTopic();
	}

	@Test
	public void testGetForumTopic() throws Exception {
		ForumTopic topicGot = forumService.getForumTopic(topic.getTopicUuid());

		assertEquals(unRandomize(topic.getTitle()), unRandomize(topicGot.getTitle()));

		if (forumService.getApiVersion().isAtLeast(v5_0)) {
			assertEquals(unRandomize(formatContent(topic.getContent())), unRandomize(topicGot.getContent()));
		} else {
			assertEquals(unRandomize(topic.getContent()), unRandomize(topicGot.getContent()));
		}
	}

	@Test
	public void testUpdateTopic() throws Exception {
		topic.setTitle("new Test topic title" + System.currentTimeMillis());
		topic.setContent("new Test topic content" + System.currentTimeMillis());
		forumService.updateForumTopic(topic);
		
		ForumTopic updatedTopic = forumService.getForumTopic(topic.getTopicUuid());
		
		assertEquals(unRandomize(topic.getTitle()), unRandomize(updatedTopic.getTitle()));
		assertEquals(unRandomize(topic.getTopicUuid()), unRandomize(updatedTopic.getTopicUuid()));
		assertEquals(unRandomize(topic.getContent()), unRandomize(updatedTopic.getContent().trim()));
	}

	@After
	public void deleteTopicOnExit() throws Exception {
		deleteForumTopic(topic);
	}
}
