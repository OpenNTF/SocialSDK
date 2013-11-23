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
public class CreateCommunity extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CreateCommunity";

    @Test
    public void testCreateCommunity() {
    	String name = createCommunityName();
    	addSnippetParam("CommunityService.title", name);
    	addSnippetParam("CommunityService.content", name);
    	
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));

        String communityUuid = json.getString("getCommunityUuid");
        community = getCommunity(communityUuid);
        assertCommunityValid(json);
    }

    /*
    @Test
    public void testCreateCommunityInvalidTitle() {
    	String name = community.getTitle();
    	addSnippetParam("CommunityService.title", name);
    	addSnippetParam("CommunityService.content", name);
    	
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        String message = json.getString("message");
        Assert.assertEquals("A community with the requested name already exists, choose a different name and resubmit.", message);
    }
    */
    
}
