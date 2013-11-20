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

import java.util.List;

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
public class CrudCommunity extends BaseCommunitiesTest {
    
    static final String CREATE_SNIPPET_ID = "Social_Communities_API_CreateCommunity";
    static final String GET_SNIPPET_ID = "Social_Communities_API_GetCommunity";
    static final String DELETE_SNIPPET_ID = "Social_Communities_API_DeleteCommunity";

    public CrudCommunity() {
        createCommunity = false;
    }

    @Test
    public void testCrudCommunity() {
    	String name = createCommunityName();
    	addSnippetParam("CommunityService.title", name);
    	addSnippetParam("CommunityService.content", name);
    	
        JavaScriptPreviewPage previewPage = executeSnippet(CREATE_SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        String communityUuid = json.getString("getCommunityUuid");
        community = getCommunity(communityUuid);
        assertCommunityValid(json);
        
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        previewPage = executeSnippet(GET_SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        assertCommunityValid((JsonJavaObject)jsonList.get(0));
        Assert.assertEquals(community.getCommunityUuid(), ((JsonJavaObject)jsonList.get(1)).getString("entityId"));
        
        addSnippetParam("CommunityService.communityUuid2", community.getCommunityUuid());
        previewPage = executeSnippet(DELETE_SNIPPET_ID);
        json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(community.getCommunityUuid(), json.getString("communityUuid"));
        
        community = getCommunity(community.getCommunityUuid(), false);
        if (community != null) {
        	String flag = community.getAsString("a:category[@scheme='http://www.ibm.com/xmlns/prod/sn/flags']/@term");
            Assert.assertEquals("Delete community is still available", "deleted", flag);
        }
        
        // community has already been deleted
        community = null;
    }
    
}
