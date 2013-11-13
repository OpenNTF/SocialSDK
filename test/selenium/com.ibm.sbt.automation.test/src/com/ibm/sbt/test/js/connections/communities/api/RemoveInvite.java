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
import com.ibm.sbt.services.client.connections.communities.Invite;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class RemoveInvite extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_RemoveInvite";

    @Test
    public void testRemoveInvite() {
    	String displayName2 = getProperty("sample.displayName2");
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		displayName2 = getProperty("smartcloud.displayName2");
    		id2 = getProperty("smartcloud.id2");
    	}
    	Invite invite = createInvite(community, id2); 
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("CommunityService.inviteeUuid", invite.getInviteeUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        String inviteUuid = community.getCommunityUuid() + "-" + invite.getInviteeUuid();
        Assert.assertEquals(inviteUuid, json.getString("inviteUuid"));
    }


    @Test
    public void testRemoveInviteInvalidCommunity() {
    	String displayName2 = getProperty("sample.displayName2");
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		displayName2 = getProperty("smartcloud.displayName2");
    		id2 = getProperty("smartcloud.id2");
    	}
    	Invite invite = createInvite(community, id2); 
    	
        addSnippetParam("CommunityService.communityUuid", "foo");
        addSnippetParam("CommunityService.inviteeUuid", invite.getInviteeUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(500, json.getAsInt("code"));
        Assert.assertEquals("Error generating atom document.", json.getString("message"));
    }

    @Test
    public void testRemoveInviteNoCommunity() {
    	String displayName2 = getProperty("sample.displayName2");
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		displayName2 = getProperty("smartcloud.displayName2");
    		id2 = getProperty("smartcloud.id2");
    	}
    	Invite invite = createInvite(community, id2); 
    	
        addSnippetParam("CommunityService.communityUuid", "");
        addSnippetParam("CommunityService.inviteeUuid", invite.getInviteeUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getAsInt("code"));
        Assert.assertEquals("Invalid argument, invite with community UUID must be specified.", json.getString("message"));
    }

    @Test
    public void testRemoveInviteInvalidInvitee() {
    	String displayName2 = getProperty("sample.displayName2");
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		displayName2 = getProperty("smartcloud.displayName2");
    		id2 = getProperty("smartcloud.id2");
    	}
    	Invite invite = createInvite(community, id2); 
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("CommunityService.inviteeUuid", "foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getAsInt("code"));
        Assert.assertEquals("Unknown user.", json.getString("message"));
    }

    @Test
    public void testRemoveInviteNoInvitee() {
    	String displayName2 = getProperty("sample.displayName2");
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		displayName2 = getProperty("smartcloud.displayName2");
    		id2 = getProperty("smartcloud.id2");
    	}
    	Invite invite = createInvite(community, id2); 
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("CommunityService.inviteeUuid", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getAsInt("code"));
        Assert.assertEquals("Invalid argument, invite with email or userid or invitee must be specified.", json.getString("message"));
    }

}
