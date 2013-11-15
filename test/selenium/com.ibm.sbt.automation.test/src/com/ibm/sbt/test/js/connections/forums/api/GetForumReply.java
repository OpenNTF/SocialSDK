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
package com.ibm.sbt.test.js.connections.forums.api;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseForumsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumReply;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class GetForumReply extends BaseForumsTest {
    
    static final String SNIPPET_ID = "Social_Forums_API_GetForumReply";

    public GetForumReply() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetForumReply() {
    	String title = createForumTopicName();
    	ForumTopic forumTopic = createForumTopic(forum, title, title);
    	ForumReply forumReply = createForumReply(forumTopic, title, title);
        addSnippetParam("ForumService.replyUuid", forumReply.getReplyUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        assertForumReplyValid(forumReply, (JsonJavaObject)json);
    }
    
    @Test
    public void testGetForumReplyError() {
        addSnippetParam("ForumService.replyUuid", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("No existing forum found. Please contact your system administrator.", json.getString("message"));
    }
    
    @Test
    public void testGetForumReplyInvalidArg() {
        addSnippetParam("ForumService.replyUuid", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument, expected replyUuid.", json.getString("message"));
    }
    
}
