/*
 *  Copyright IBM Corp. 2013
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
public class BlogPostLikeUnlikeTest extends BaseBlogServiceTest {


	@Before
	public void initBlogPostTestData() {
		blogPost = createBlogPost();
	}

	@Test
	public void recommendBlogPostTest() {
		try {
			blogService.recommendPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
			BlogPost recommendedPost = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
			assertEquals(recommendedPost.getRecommendationsCount(), "1");
			blogService.unrecommendPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
			BlogPost unrecommendedPost = blogService.getBlogPost(blogPost.getBlogHandle(), blogPost.getPostUuid());
			
			assertEquals(unrecommendedPost.getRecommendationsCount(), "0");
			
		} catch (Exception e) {
			fail("Error calling blogService.recommendPost() caused by: "+e.getMessage());
		}

	}

	@After
	public void deleteBlogPostTestDataOnExit() {
		try {

			blogService.removeBlogPost(blogPost.getPostUuid(), blogPost.getBlogHandle());
			blogService.removeBlog(blog.getBlogUuid());
		} catch (BlogServiceException e) {
			fail("Error calling blogService.removeBlog() caused by: "+e.getMessage());
		}

	}


}
