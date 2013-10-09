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
package com.ibm.sbt.test.js.connections.forums.api;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseForumsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.forums.Forum;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class GetForum extends BaseForumsTest {
    
    static final String SNIPPET_ID = "Social_Forums_API_GetForum";

    public GetForum() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetForum() {
        addSnippetParam("ForumService.forumUuid", forum.getForumUuid());
        
        Forum aforum = getForum(forum.getForumUuid());
        Assert.assertNotNull(aforum);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        assertForumValid((JsonJavaObject)json);
    }
    
    @Test
    public void testGetForumError() {
        addSnippetParam("ForumService.forumUuid", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("CLFRV0008E: Error, unable to find object with uuid: Foo", json.getString("message"));
    }
    
    @Test
    public void testGetForumInvalidArg() {
        addSnippetParam("ForumService.forumUuid", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument, expected forumUuid.", json.getString("message"));
    }
    
}
