/*
 * © Copyright IBM Corp. 2012
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

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseForumsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class UpdateForum extends BaseForumsTest {

    static final String SNIPPET_ID = "Social_Forums_API_UpdateForum";

    @Test
    public void testUpdateForum() {
        String updatedTitle = "Updated Title - " + System.currentTimeMillis();
        String updatedContent = "Updated Content - " + System.currentTimeMillis();
        
        //addSnippetParam("ForumService.forumUuid", forum.getForumUuid());
        addSnippetParam("ForumService.forumTitle", updatedTitle);
        addSnippetParam("ForumService.forumContent", updatedContent);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        
        //Assert.assertEquals(forum.getForumUuid(), json.getString("getForumUuid"));
        Assert.assertEquals(updatedTitle, json.getString("getTitle"));
        Assert.assertEquals(updatedContent, json.getString("getContent"));
    }
    
    @Test
    public void testUpdateForumDuplicate(){
    	if (environment.isSmartCloud()) {
    		return;
    	}
    	
        String duplicateTitle = "Duplicate Title - " + System.currentTimeMillis();
        String updatedContent = "Updated Content - " + System.currentTimeMillis();
        //Forum forum2 = createForum(duplicateTitle, "public", "Content for duplicate test", "duplicate");
        
        //addSnippetParam("ForumService.forumUuid", forum.getForumUuid());
        addSnippetParam("ForumService.forumTitle", duplicateTitle);
        addSnippetParam("ForumService.forumContent", updatedContent);

        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(409, json.getInt("code"));
        Assert.assertEquals("A forum with the requested name already exists, choose a different name and resubmit.", json.getString("message"));
        
		//deleteForum(forum2);
    }
    
    @Test
    public void testUpdateForumError() {
        addSnippetParam("ForumService.forumUuid", "Foo");
        addSnippetParam("ForumService.forumTitle", "Foo");
        addSnippetParam("ForumService.forumContent", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("The referenced forum does not exist.", json.getString("message"));
    }
    
}
