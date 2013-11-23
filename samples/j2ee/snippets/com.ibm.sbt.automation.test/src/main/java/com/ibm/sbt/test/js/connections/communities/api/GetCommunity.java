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
 * @date 25 Mar 2013
 */
public class GetCommunity extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_GetCommunity";

    public GetCommunity() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetCommunity() {
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        assertCommunityValid((JsonJavaObject)jsonList.get(0));
        Assert.assertEquals(community.getCommunityUuid(), ((JsonJavaObject)jsonList.get(1)).getString("entityId"));
    }
    
    @Test
    public void testGetCommunityError() {
        addSnippetParam("CommunityService.communityUuid", "Foo");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("The community which this resource or page is associated with does not exist.", json.getString("message"));
    }
    
    @Test
    public void testGetCommunityInvalidArg() {
        addSnippetParam("CommunityService.communityUuid", "");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument, expected communityUuid.", json.getString("message"));
    }
    
}
