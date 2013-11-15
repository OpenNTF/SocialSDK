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
package com.ibm.sbt.test.js.connections.communities.api;

import java.util.Iterator;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class UpdateCommunity extends BaseCommunitiesTest {

    static final String SNIPPET_ID = "Social_Communities_API_UpdateCommunity";

    @Test
    public void testUpdateCommunity() {
        String updatedTitle = "Updated Title - " + System.currentTimeMillis();
        String updatedContent = "Updated Content - " + System.currentTimeMillis();
        
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.communityTitle", updatedTitle);
        addSnippetParam("sample.communityContent", updatedContent);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        
        Assert.assertEquals(community.getCommunityUuid(), json.getString("getCommunityUuid"));
        Assert.assertEquals(updatedTitle, json.getString("getTitle"));
        Assert.assertEquals(updatedContent, json.getString("getContent"));
    }
    
    @Test
    public void testUpdateCommunityDuplicate(){
    	if (environment.isSmartCloud()) {
    		return;
    	}
    	
        String duplicateTitle = "Duplicate Title - " + System.currentTimeMillis();
        String updatedContent = "Updated Content - " + System.currentTimeMillis();
        Community community2 = createCommunity(duplicateTitle, "public", "Content for duplicate test", "duplicate");
        
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.communityTitle", duplicateTitle);
        addSnippetParam("sample.communityContent", updatedContent);

        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(409, json.getInt("code"));
        Assert.assertEquals("A community with the requested name already exists, choose a different name and resubmit.", json.getString("message"));
        
		deleteCommunity(community2);
    }
    
    @Test
    public void testUpdateCommunityError() {
        addSnippetParam("CommunityService.communityUuid", "Foo");
        addSnippetParam("sample.communityTitle", "Foo");
        addSnippetParam("sample.communityContent", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertTrue("Invalid return code, expected 404 or 500", json.getInt("code") == 404 || json.getInt("code") == 500); // TODO 500 OK because of issue in SmartCloud
        String errorMessage  =  json.getString("message");
        boolean isMessageValid = errorMessage.contains("The referenced community does not exist.") || errorMessage.contains("Error generating atom document.");
        Assert.assertTrue("expected message different than "+errorMessage, isMessageValid);
    }
    
}
