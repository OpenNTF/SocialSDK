/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.js.connections.forums.api;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseForumsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class UpdateForumTopic extends BaseForumsTest {

    static final String SNIPPET_ID = "Social_Forums_API_UpdateForumTopic";

    @Test
    public void testUpdateForumTopic() {
        String updatedTitle = "Updated Title - " + System.currentTimeMillis();
        String updatedContent = "Updated Content - " + System.currentTimeMillis();
        
    	String title = createForumTopicName();
    	ForumTopic forumTopic = createForumTopic(forum, title, title);
        addSnippetParam("ForumService.topicUuid", forumTopic.getTopicUuid());
        addSnippetParam("ForumService.topicTitle", updatedTitle);
        addSnippetParam("ForumService.topicContent", updatedContent);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        
        forumTopic = getForumTopic(forumTopic.getTopicUuid(), true);
        Assert.assertEquals(forumTopic.getTopicUuid(), json.getString("getTopicUuid"));
        Assert.assertEquals(updatedTitle, forumTopic.getTitle());
        Assert.assertEquals(updatedContent, StringUtil.trim(forumTopic.getContent()));
    }
    
    @Test
    public void testUpdateForumTopicError() {
        addSnippetParam("ForumService.topicUuid", "Foo");
        addSnippetParam("ForumService.topicTitle", "Foo");
        addSnippetParam("ForumService.topicContent", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("No existing forum found. Please contact your system administrator.", json.getString("message"));
    }
    
}
