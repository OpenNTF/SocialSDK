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

package com.ibm.sbt.services.client.connections.blogs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class BlogPostDeleteTest extends BaseBlogServiceTest {

	@Test
	public void deleteBlog() throws Exception {
		try {
			blog = createBlog();
			Blog blogGot = blogService.getBlog(blog.getBlogUuid());
			assertEquals(blog.getTitle(), blogGot.getTitle());
			blogService.removeBlog(blog.getBlogUuid());
			blogService.getBlog(blog.getBlogUuid());
		}
		catch(Exception e) {
			assertNotNull(e.getMessage());
		}
	}

	@Test
	public void deleteBlogPost() throws Exception {
		try {
			blogPost = createBlogPost();
			BlogPost blogPostGot = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
			assertEquals(blogPost.getTitle(), blogPostGot.getTitle());
			blogService.removeBlogPost(blogPost.getPostUuid(), blogPost.getBlogHandle());
			blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
			deleteBlog(blog);
		}catch(Exception e) {
			assertNotNull(e.getMessage());
		}
	}

}
