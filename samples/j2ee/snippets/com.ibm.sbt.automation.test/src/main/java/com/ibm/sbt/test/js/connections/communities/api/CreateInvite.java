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
 * @date 25 Mar 2013
 */
public class CreateInvite extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CreateInvite";

    @Test
    public void testCreateInvite() {
    	String displayName2 = getProperty("sample.displayName2");
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		displayName2 = getProperty("smartcloud.displayName2");
    		id2 = getProperty("smartcloud.id2");
    	}
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", id2);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(community.getCommunityUuid(), json.getString("getCommunityUuid"));
        Assert.assertEquals(id2, json.getString("getUserid"));
        Assert.assertEquals(id2, json.getAsObject("getContributor").getString("userid"));
        Assert.assertEquals(displayName2, json.getAsObject("getContributor").getString("name"));
    }

    @Test
    public void testCreateInviteInvalidCommunity() {
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		id2 = getProperty("smartcloud.id2");
    	}
    	
        addSnippetParam("CommunityService.communityUuid", "foobar");
        addSnippetParam("sample.id2", id2);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(500, json.getAsInt("code"));
        Assert.assertEquals("Error generating atom document.", json.getString("message"));
    }

    @Test
    public void testCreateInviteInvalidUserid() {
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", "foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(500, json.getAsInt("code"));
        Assert.assertEquals("Error generating atom document.", json.getString("message"));
    }

    @Test
    public void testCreateInviteNoCommunity() {
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		id2 = getProperty("smartcloud.id2");
    	}
    	
        addSnippetParam("CommunityService.communityUuid", "");
        addSnippetParam("sample.id2", id2);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getAsInt("code"));
        Assert.assertEquals("Invalid argument, invite with community UUID must be specified.", json.getString("message"));
    }

    @Test
    public void testCreateInviteNoUserid() {
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getAsInt("code"));
        Assert.assertEquals("Invalid argument, invite with email or userid or invitee must be specified.", json.getString("message"));
    }

}
