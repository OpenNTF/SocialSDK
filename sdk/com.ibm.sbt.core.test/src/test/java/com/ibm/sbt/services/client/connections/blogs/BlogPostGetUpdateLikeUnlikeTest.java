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
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Swati Singh
 *
 */
public class BlogPostGetUpdateLikeUnlikeTest extends BaseBlogServiceTest {

	@Before
	public void initBlogPostTestData() throws BlogServiceException {
		blogPost = createBlogPost();
	}

	@Test
	public void recommendBlogPostTest() throws BlogServiceException {
		blogService.recommendPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
		BlogPost recommendedPost = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
		assertEquals(recommendedPost.getRecommendationsCount(), "1");
		blogService.unrecommendPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
		BlogPost unrecommendedPost = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
		assertEquals(unrecommendedPost.getRecommendationsCount(), "0");

	}

	@Test
	public void getBlogPost() throws BlogServiceException {
		BlogPost blogPostGot = blogService.getBlogPost(blog.getHandle(), blogPost.getPostUuid());
		assertEquals(unRandomize(blogPost.getTitle()), unRandomize(blogPostGot.getTitle()));
		assertEquals(unRandomize(blogPost.getContent()), unRandomize(blogPostGot.getContent()));
	}

	@Test
	public void updateBlogPost() throws BlogServiceException {
		blogPost.setTitle("New title " + System.currentTimeMillis());
		blogPost.setContent("New content " + System.currentTimeMillis());
		blogService.updateBlogPost(blogPost, blogPost.getBlogHandle());
		BlogPost postUpdated = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
		assertEquals(unRandomize(blogPost.getTitle()), unRandomize(postUpdated.getTitle()));
		assertEquals(unRandomize(blogPost.getContent()), unRandomize(postUpdated.getContent()));
	}

	@After
	public void deleteBlogPostTestDataOnExit() throws BlogServiceException {
		deleteBlogPost(blogPost);
		deleteBlog(blog);
	}
}
