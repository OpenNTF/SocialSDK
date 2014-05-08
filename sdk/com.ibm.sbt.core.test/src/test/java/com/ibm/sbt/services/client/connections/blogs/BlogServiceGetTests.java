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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;
import com.ibm.sbt.services.client.connections.common.Tag;

/**
 * @author Swati Singh
 *
 */
public class BlogServiceGetTests extends BaseBlogServiceTest {
	
	@Test
	public void getAllBlogs() throws ClientServicesException {
		EntityList<Blog> entries = blogService.getAllBlogs();
		assertNotNull(entries);
		for (BaseBlogEntity blog : entries) {
			assertValid((Blog)blog);
		}
	}

	@Test
	public void getMyBlogs() throws ClientServicesException {
		EntityList<Blog> entries = blogService.getMyBlogs();
		assertNotNull(entries);
		for (BaseBlogEntity blog : entries) {
			assertValid((Blog)blog);
		}
	}

	@Test
	public void getFeaturedBlogs() throws ClientServicesException {
		EntityList<Blog> entries = blogService.getFeaturedBlogs();
		assertNotNull(entries);
		for (BaseBlogEntity blog : entries) {
			assertValid((Blog)blog);
		}
	}

	@Test
	public void getAllComments() throws ClientServicesException {
		EntityList<Comment> entries = blogService.getAllComments();
		assertNotNull(entries);
		for (BaseBlogEntity comment : entries) {
			assertValid((Comment)comment);
		}
	}


	@Test
	public void getAllPosts() throws ClientServicesException {
		EntityList<BlogPost> entries = blogService.getAllPosts();
		assertNotNull(entries);
		for (BaseBlogEntity blogPost : entries) {
			assertValid((BlogPost)blogPost);
		}
	}

	@Test
	public void getAllTags() throws ClientServicesException {
		EntityList<Tag> entries = blogService.getAllTags();
		assertNotNull(entries);
		for (Tag tag : entries) {
			assertValid((Tag)tag);
		}
	}
	
	@Test
	public void getRecommendedPosts() throws ClientServicesException {
		EntityList<BlogPost> entries = blogService.getRecommendedPosts();
		assertNotNull(entries);
		for (BaseBlogEntity blogPost : entries) {
			assertValid((BlogPost)blogPost);
		}
	}
	
	@Test
	public void getFeaturedPosts() throws ClientServicesException {
		EntityList<BlogPost> entries = blogService.getFeaturedPosts();
		assertNotNull(entries);
		for (BaseBlogEntity blogPost : entries) {
			assertValid((BlogPost)blogPost);
		}
	}
	
	@Test
	public void getBlogPosts() throws ClientServicesException {
		Blog blog = createBlog();
		EntityList<BlogPost> entries = blogService.getBlogPosts(blog.getHandle());
		assertNotNull(entries);
		for (BaseBlogEntity blogPost : entries) {
			assertValid((BlogPost)blogPost);
		}
		deleteBlog(blog);
	}
	
	@Test
	public void getBlogComments() throws ClientServicesException {
		Blog blog = createBlog();
		EntityList<Comment> entries = blogService.getBlogComments(blog.getHandle());
		assertNotNull(entries);
		for (BaseBlogEntity commment : entries) {
			assertValid((Comment)commment);
		}
		deleteBlog(blog);
	}
	
	@Test
	public void getBlogTags() throws ClientServicesException {
		Blog blog = createBlog();
		EntityList<Tag> entries = blogService.getBlogTags(blog.getHandle());
		assertNotNull(entries);
		for (Tag tag : entries) {
			assertValid((Tag)tag);
		}
		deleteBlog(blog);
	}
	
	protected void assertValid(Blog blog) {
		assertNotNull("Invalid blog id", blog.getId());
		assertNotNull("Invalid blog title", blog.getTitle());
		assertNotNull("Invalid blog updated date", blog.getUpdated());
	}
	
	protected void assertValid(BlogPost blogPost) {
		assertNotNull("Invalid blog post id", blogPost.getId());
		assertNotNull("Invalid blog post title", blogPost.getTitle());
		assertNotNull("Invalid blog post updated date", blogPost.getUpdated());
	}
	
	protected void assertValid(Comment comment) {
		assertNotNull("Invalid blog comment id", comment.getId());
		assertNotNull("Invalid blog comment title", comment.getTitle());
		assertNotNull("Invalid blog comment updated date", comment.getUpdated());
	}
	
	protected void assertValid(Tag tag) {
		assertNotNull("Invalid Tag", tag.getTerm());
		assertNotNull("Invalid Tag visibility", tag.isVisible());
		assertNotNull("Invalid Tag frequency", tag.getFrequency());
		assertNotNull("Invalid Tag intensity", tag.isVisible());
	}
}