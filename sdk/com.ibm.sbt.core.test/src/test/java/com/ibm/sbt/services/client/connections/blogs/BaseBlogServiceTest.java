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

import static org.junit.Assert.fail;

import org.junit.Before;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author Swati Singh
 *
 */
public class BaseBlogServiceTest extends BaseUnitTest {
	protected BlogService blogService;
	protected Blog blog;
	protected BlogPost blogPost;
	protected Comment comment;

	@Before
	public void initBlogServiceTest() {
		if (blogService==null) {
			blogService = new BlogService();
		}
	}

	protected Blog createBlog() throws ClientServicesException {
		Blog blog = new Blog(blogService, "");

		Long random = System.currentTimeMillis();
		blog.setTitle("Test Blog" + random);
		blog.setHandle("TestHandle"+random);
		blog.setTimeZone("Asia/Calcutta");
		blog = blog.save(); 
		return blog;
	}

	protected BlogPost createBlogPost() throws ClientServicesException {
		blog = createBlog();
		BlogPost blogPost = new BlogPost(blogService, "");

		blogPost.setTitle("Test Blog Post" + System.currentTimeMillis());
		blogPost.setContent("Test Blog Post Content"+ System.currentTimeMillis());
		blogPost = blogService.createBlogPost(blogPost, blog.getHandle());
		return blogPost;
	}

	protected Comment createBlogComment() throws ClientServicesException {
		blogPost = createBlogPost();
		Comment comment = new Comment(blogService, "");

		comment.setTitle("Test Blog Comment" + System.currentTimeMillis());
		comment.setContent("Test Blog Post Content12"+System.currentTimeMillis());
		comment = blogService.createBlogComment(comment, blog.getHandle(), blogPost.getPostUuid());

		return comment;
	}
	
	protected void deleteBlog(Blog blog) throws ClientServicesException {
		blogService.removeBlog(blog.getBlogUuid());
	}
	
	protected void deleteBlogPost(BlogPost post) throws ClientServicesException {
		blogService.removeBlogPost(post.getPostUuid(), blog.getHandle());
	}

	protected void deleteBlogComment(Comment comment) throws ClientServicesException {
		blogService.removeBlogComment(blog.getHandle(), comment.getCommentUuid());
	}
}
