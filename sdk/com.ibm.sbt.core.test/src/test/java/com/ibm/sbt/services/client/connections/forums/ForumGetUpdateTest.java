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
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class ForumGetUpdateTest extends BaseForumServiceTest {

	@Before
	public void initForum() throws ForumServiceException {
		forum = createForum();
	}

	@Test
	public void testGetForum() throws ForumServiceException {
		Forum forumGot = forumService.getForum(forum.getForumUuid());

		assertEquals(forum.getTitle(), forumGot.getTitle());
		assertEquals(forum.getContent(), forumGot.getContent());
	}

	@Test
	public void testUpdateForum() throws ForumServiceException {
		forum.setTitle("new Test forum title" + System.currentTimeMillis());
		forum.setContent("new Test forum content");
		forumService.updateForum(forum);
		Forum updatedForum = forumService.getForum(forum.getForumUuid());
		assertEquals(unRandomize(forum.getTitle()), unRandomize(updatedForum.getTitle()));
		assertEquals(forum.getContent(), updatedForum.getContent());
	}

	@After
	public void deleteForumOnExit() throws ForumServiceException {
		deleteForum(forum);
	}
}
