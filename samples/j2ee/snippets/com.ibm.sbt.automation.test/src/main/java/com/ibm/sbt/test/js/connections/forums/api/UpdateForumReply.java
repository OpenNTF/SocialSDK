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
import com.ibm.sbt.services.client.connections.forums.ForumReply;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class UpdateForumReply extends BaseForumsTest {

    static final String SNIPPET_ID = "Social_Forums_API_UpdateForumReply";

    @Test
    public void testUpdateForumReply() {
        String updatedTitle = "Updated Title - " + System.currentTimeMillis();
        String updatedContent = "Updated Content - " + System.currentTimeMillis();
        
    	String title = createForumTopicName();
    	ForumTopic forumTopic = createForumTopic(forum, title, title);
    	ForumReply forumReply = createForumReply(forumTopic, title, title);
        addSnippetParam("ForumService.replyUuid", forumReply.getReplyUuid());
        addSnippetParam("ForumService.replyTitle", updatedTitle);
        addSnippetParam("ForumService.replyContent", updatedContent);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        
        forumReply = getForumReply(forumReply.getReplyUuid());
        Assert.assertEquals(forumReply.getReplyUuid(), json.getString("getReplyUuid"));
        Assert.assertEquals(updatedTitle, forumReply.getTitle());
        Assert.assertEquals(updatedContent, StringUtil.trim(forumReply.getContent()));
    }
    
    @Test
    public void testUpdateForumReplyError() {
        addSnippetParam("ForumService.replyUuid", "Foo");
        addSnippetParam("ForumService.replyTitle", "Foo");
        addSnippetParam("ForumService.replyContent", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("CLFRV0008E: Error, unable to find object with uuid: forum for post Foo not found", json.getString("message"));
    }
    
}
