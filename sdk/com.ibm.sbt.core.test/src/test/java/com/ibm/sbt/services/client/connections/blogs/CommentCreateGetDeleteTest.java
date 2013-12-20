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

package com.ibm.sbt.services.client.connections.blogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author Swati Singh
 *
 */
public class CommentCreateGetDeleteTest extends BaseBlogServiceTest {
	@Before
	public void createCommentInit() {
		comment = createBlogComment();
		
	}

	@Test
	public void CreateComment() {
		try {
			Comment commentReturned = blogService.createBlogComment(comment, blog.getHandle(), blogPost.getPostUuid());
			assertNotNull(commentReturned.getTitle());
			assertEquals(comment.getTitle(), commentReturned.getTitle());
			deleteBlogComment(commentReturned);
		} catch (Exception e) {
			fail("Error calling blogService.createBlogComment() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void GetComment() {
		try {
			
			Comment commentGot = blogService.getBlogComment(blog.getHandle(), comment.getCommentUuid());
			
			assertEquals(comment.getTitle(), commentGot.getTitle());
			assertEquals(comment.getContent(), commentGot.getContent());
		} catch (Exception e) {
			fail("Error calling blogService.getBlogComment() caused by: "+e.getMessage());
		}
	}

	@Test
	public void deleteComment() throws Exception {
		try {
			blogService.removeBlogComment(blog.getBlogUuid(), comment.getCommentUuid());
			blogService.getBlogComment(blog.getHandle(), comment.getCommentUuid());
		}
		catch(Exception e) {
			assertNotNull(e.getMessage());
		}
	}
	

}
