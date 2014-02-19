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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Test;
import com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity;

/**
 * @author Swati Singh
 *
 */
public class BlogServiceGetTests extends BaseBlogServiceTest {
	
	@Test
	public void getAllBlogs() {
		try {
			BlogList entries = blogService.getAllBlogs();
			assertNotNull(entries);
			for (BaseBlogEntity blog : entries) {
				assertValid((Blog)blog);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getBlogs() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getMyBlogs() {
		try {
			BlogList entries = blogService.getMyBlogs();
			assertNotNull(entries);
			for (BaseBlogEntity blog : entries) {
				assertValid((Blog)blog);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getMyBlogs() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getFeaturedBlogs() {
		try {
			BlogList entries = blogService.getFeaturedBlogs();
			assertNotNull(entries);
			for (BaseBlogEntity blog : entries) {
				assertValid((Blog)blog);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getFeaturedBlogs() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getAllComments() {
		try {
			CommentList entries = blogService.getAllComments();
			assertNotNull(entries);
			for (BaseBlogEntity comment : entries) {
				assertValid((Comment)comment);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getAllComments() caused by: "+e.getMessage());
		}
	}


	@Test
	public void getAllPosts() {
		try {
			BlogPostList entries = blogService.getAllPosts();
			assertNotNull(entries);
			for (BaseBlogEntity blogPost : entries) {
				assertValid((BlogPost)blogPost);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getAllPosts() caused by: "+e.getMessage());
		}
	}

	@Test
	public void getAllTags() {
		try {
			TagList entries = blogService.getAllTags();
			assertNotNull(entries);
			for (Tag tag : entries) {
				assertValid((Tag)tag);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getAllTags() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getRecommendedPosts() {
		try {
			BlogPostList entries = blogService.getRecommendedPosts();
			assertNotNull(entries);
			for (BaseBlogEntity blogPost : entries) {
				assertValid((BlogPost)blogPost);
			}
		} catch (Exception e) {
			fail("Error calling blogService.getRecommendedPosts() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getFeaturedPosts() {
		try {
			BlogPostList entries = blogService.getFeaturedPosts();
			assertNotNull(entries);
			for (BaseBlogEntity blogPost : entries) {
				assertValid((BlogPost)blogPost);
			}

		} catch (Exception e) {
			fail("Error calling blogService.getFeaturedPosts() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getBlogPosts() {
		try {
			Blog blog = createBlog();
			BlogPostList entries = blogService.getBlogPosts(blog.getHandle());
			assertNotNull(entries);
			for (BaseBlogEntity blogPost : entries) {
				assertValid((BlogPost)blogPost);
			}
			deleteBlog(blog);
		} catch (Exception e) {
			fail("Error calling blogService.getBlogPosts() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getBlogComments() {
		try {
			Blog blog = createBlog();
			CommentList entries = blogService.getBlogComments(blog.getHandle());
			assertNotNull(entries);
			for (BaseBlogEntity commment : entries) {
				assertValid((Comment)commment);
			}
			deleteBlog(blog);
		} catch (Exception e) {
			fail("Error calling blogService.getBlogComments() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getBlogTags() {
		try {
			Blog blog = createBlog();
			TagList entries = blogService.getBlogTags(blog.getHandle());
			assertNotNull(entries);
			for (Tag tag : entries) {
				assertValid((Tag)tag);
			}
			deleteBlog(blog);
		} catch (Exception e) {
			fail("Error calling blogService.getBlogComments() caused by: "+e.getMessage());
		}
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
		assertNotNull("Invalid Tag visibility", tag.getVisibility());
		assertNotNull("Invalid Tag frequency", tag.getFrequency());
		assertNotNull("Invalid Tag intensity", tag.getIntensity());
	}
}