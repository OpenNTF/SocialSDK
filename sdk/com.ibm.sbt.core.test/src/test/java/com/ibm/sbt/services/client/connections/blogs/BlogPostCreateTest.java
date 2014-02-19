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
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class BlogPostCreateTest extends BaseBlogServiceTest {

	@Test
	public void createBlogTest() {
		try {
			Blog blog = new Blog(blogService, "");
			blog.setTitle("Test Blog" +  System.currentTimeMillis());
			blog.setHandle("TestHandle"+ System.currentTimeMillis());
			blog.setTimeZone("Asia/Calcutta");

			Blog blogReturned = blogService.createBlog(blog);
			assertNotNull(blogReturned.getTitle());
			assertEquals(unRandomize(blog.getTitle()), unRandomize(blogReturned.getTitle()));
			assertEquals(unRandomize(blog.getHandle()), unRandomize(blogReturned.getHandle()));
			deleteBlog(blogReturned);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling blogService.createBlog() caused by: "+e.getMessage());
		}
	}

	@Test
	public void createBlogPostTest() {
		try {
			blog = createBlog();
			BlogPost blogPost = new BlogPost(blogService, "");
			blogPost.setTitle("Test Blog Post" + System.currentTimeMillis());
			blogPost.setContent("Test Blog Post Content"+ System.currentTimeMillis());
			BlogPost blogPostReturned = blogService.createBlogPost(blogPost, blog.getHandle());
			assertNotNull(blogPostReturned.getTitle());
			assertEquals(unRandomize(blogPost.getTitle()), unRandomize(blogPostReturned.getTitle()));
			assertEquals(unRandomize(blogPost.getContent()), unRandomize(blogPostReturned.getContent()));
			deleteBlogPost(blogPostReturned);
			deleteBlog(blog);
		} catch (Exception e) {
			fail("Error calling blogService.createBlogPost() caused by: "+e.getMessage());
		}
	}
}
