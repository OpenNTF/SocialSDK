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
package com.ibm.sbt.test.js.connections;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.ibm.sbt.test.js.connections.blogs.api.DeleteBlog;
import com.ibm.sbt.test.js.connections.blogs.api.DeleteComment;
import com.ibm.sbt.test.js.connections.blogs.api.DeletePost;
import com.ibm.sbt.test.js.connections.blogs.api.GetBlogsPosts;
import com.ibm.sbt.test.js.connections.blogs.api.CreateBlog;
import com.ibm.sbt.test.js.connections.blogs.api.CreateComment;
import com.ibm.sbt.test.js.connections.blogs.api.CreatePost;
import com.ibm.sbt.test.js.connections.blogs.api.GetBlogComments;
import com.ibm.sbt.test.js.connections.blogs.api.GetBlogPosts;
import com.ibm.sbt.test.js.connections.blogs.api.GetBlogs;
import com.ibm.sbt.test.js.connections.blogs.api.GetBlogsComments;
import com.ibm.sbt.test.js.connections.blogs.api.GetComment;
import com.ibm.sbt.test.js.connections.blogs.api.GetFeaturedBlogs;
import com.ibm.sbt.test.js.connections.blogs.api.GetMyBlogs;
import com.ibm.sbt.test.js.connections.blogs.api.UpdateBlog;
import com.ibm.sbt.test.js.connections.blogs.api.UpdatePost;
import com.ibm.sbt.test.js.connections.wikis.api.GetAllWikis;
import com.ibm.sbt.test.js.connections.wikis.api.GetMyWikis;
import com.ibm.sbt.test.js.connections.wikis.api.GetPublicWikis;


/**
 * @author mark wallace
 * 
 * @date 1 Oct 2013
 */
@RunWith(Suite.class)
@SuiteClasses({ 
	GetMyWikis.class,
	GetPublicWikis.class,
	GetAllWikis.class
})
public class WikisTestSuite {
    @AfterClass
    public static void cleanup() {
    }
}
