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
public class AddMember extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_Add"
    		+ "Member";

    @Test
    public void testAddMember() {
    	String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		id2 = getProperty("smartcloud.id2");
    	}
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", id2);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(id2, json.getString("getId"));
        Assert.assertEquals("member", json.getString("getRole"));
        if (!environment.isSmartCloud()) {
        	Assert.assertTrue(getProperty("sample.email2").equalsIgnoreCase(json.getString("getEmail")));
        }
        Assert.assertEquals(id2, json.getString("getUserid"));
    }

    @Test
    public void testAddMemberError1() {
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", "12345");
            
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Unknown user.", json.getString("message"));
    }

    @Test
    public void testAddMemberError2() {
    	if (environment.isSmartCloud()) {
    		// status 403 causes login
    		return;
    	}

    	String id1 = getProperty("sample.id1");
    	if (environment.isSmartCloud()) {
    		id1 = getProperty("smartcloud.id1");
    	}
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", id1);
            
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        int code = json.getInt("code");
        Assert.assertTrue(403 == code || 400 == code);
        Assert.assertEquals("You are not authorized to perform the requested action.", json.getString("message"));
    }

    @Test
    public void testAddMemberErrorInvalid() {
        setAuthType(AuthType.NONE);
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        addSnippetParam("sample.id2", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNotNull("No response from test snippet", json);
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument, member with userid or email must be specified.", json.getString("message"));
    }

}
