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
package com.ibm.sbt.test.js.connections.communities.api;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 19 Mar 2013
 */
public class DeleteCommunity extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_DeleteCommunity";

    @Test
    public void testDeleteCommunity() {
        addSnippetParam("sample.communityId2", community.getCommunityUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(community.getCommunityUuid(), json.getString("communityUuid"));
        
        // community has already been deleted
        community = null;
    }
    
    @Test
    public void testConfirmDeleteCommunity() {
        addSnippetParam("sample.communityId2", community.getCommunityUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(community.getCommunityUuid(), json.getString("communityUuid"));
        
        community = getCommunity(community.getCommunityUuid(), false);
        Assert.assertNull("Delete community is still available", community);
        
        // community has already been deleted
        community = null;
    }
    
    @Test
    public void testDeleteCommunityError() {
        addSnippetParam("sample.communityId2", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("The referenced community does not exist.", json.getString("message"));
    }
    
    @Test
    public void testDeleteCommunityInvalidArg() {
        setAuthType(AuthType.NONE);
        addSnippetParam("sample.communityId2", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument, expected communityUuid.", json.getString("message"));
    }

}
