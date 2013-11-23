/*
 * � Copyright IBM Corp. 2013
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

package com.ibm.sbt.test.java.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.test.java.connections.blogs.AllBlogs;
import com.ibm.sbt.test.java.connections.blogs.AllComments;
import com.ibm.sbt.test.java.connections.blogs.AllPosts;
import com.ibm.sbt.test.java.connections.blogs.AllTags;
import com.ibm.sbt.test.java.connections.blogs.BlogComments;
import com.ibm.sbt.test.java.connections.blogs.BlogTags;
import com.ibm.sbt.test.java.connections.blogs.CreateBlog;
import com.ibm.sbt.test.java.connections.blogs.CreateBlogComment;
import com.ibm.sbt.test.java.connections.blogs.CreateBlogPost;
import com.ibm.sbt.test.java.connections.blogs.FeaturedBlogs;
import com.ibm.sbt.test.java.connections.blogs.FeaturedPosts;
import com.ibm.sbt.test.java.connections.blogs.GetBlog;
import com.ibm.sbt.test.java.connections.blogs.GetBlogComment;
import com.ibm.sbt.test.java.connections.blogs.GetBlogPost;
import com.ibm.sbt.test.java.connections.blogs.MyBlogs;
import com.ibm.sbt.test.java.connections.blogs.RecommendedPosts;
import com.ibm.sbt.test.java.connections.blogs.RecommendPost;
import com.ibm.sbt.test.java.connections.blogs.UnrecommendPost;
import com.ibm.sbt.test.java.connections.blogs.UpdateBlog;
import com.ibm.sbt.test.java.connections.blogs.UpdateBlogPost;

/**
* @author ssingh
*
* @date 19 Nov 2013
*/
@RunWith(Suite.class)
@SuiteClasses({ AllBlogs.class, AllComments.class, AllPosts.class, AllTags.class, BlogComments.class, BlogTags.class, CreateBlog.class, CreateBlogComment.class,
        CreateBlogPost.class, CreateBlogComment.class, CreateBlog.class, FeaturedBlogs.class, FeaturedPosts.class, GetBlog.class, GetBlogComment.class, GetBlogPost.class,
        MyBlogs.class, RecommendedPosts.class, RecommendPost.class , UnrecommendPost.class, UpdateBlog.class, UpdateBlogPost.class})
public class BlogsTestSuite {
    @AfterClass
    public static void cleanup() {
        TestEnvironment.cleanup();
    }
}