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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class ForumCreateDeleteTest extends BaseForumServiceTest {

	@Test
	public void testCreateForum() throws ForumServiceException {
		Forum forum = new Forum(forumService);
		forum.setTitle("Test forum title" + System.currentTimeMillis());
		forum.setContent("Test forum content");
		List<String> tags = new ArrayList<String>();
		tags.add("tag1"); 
		tags.add("tag2"); 
		forum.setTags(tags);

		Forum forumReturned = forumService.createForum(forum);
		assertNotNull(forumReturned.getTitle());
		assertEquals(unRandomize(forum.getTitle()), unRandomize(forumReturned.getTitle()));
		assertEquals(forum.getContent(), forumReturned.getContent());
		deleteForum(forumReturned);
	}

	@Test
	public void testDeleteForum() throws Exception {
		Forum createdForum = createForum();
		Forum forumGot = forumService.getForum(createdForum.getForumUuid());
		assertEquals(createdForum.getTitle(), forumGot.getTitle());
		forumService.removeForum(createdForum.getForumUuid());
	}
}
