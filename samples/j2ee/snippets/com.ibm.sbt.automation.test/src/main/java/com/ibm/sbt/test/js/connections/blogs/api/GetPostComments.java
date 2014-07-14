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
package com.ibm.sbt.test.js.connections.blogs.api;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseBlogsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.blogs.BlogServiceException;

/**
 * @author fmoloney
 * 
 * @date 29 April 2014
 */
public class GetPostComments extends BaseBlogsTest {

	private final String snippetId = "Social_Blogs_API_GetPostComments";
	
	@Before
	public void createComment(){
    	try {
			createBlogComment();
		} catch (BlogServiceException e) {
			fail("Problem initialising blogs test data", e);
		}
    }
	
    @Test
    public void test() {
        JavaScriptPreviewPage previewPage = executeSnippet(snippetId);
        List jsonList = previewPage.getJsonList();
        JsonJavaObject firstComment = (JsonJavaObject) jsonList.get(0);
        try {
			Assert.assertEquals(this.blog.getHandle(), firstComment.getString("getBlogHandle"));
		} catch (BlogServiceException e) {
			fail("Could not get blog uuid.", e);
		}
    }

}
