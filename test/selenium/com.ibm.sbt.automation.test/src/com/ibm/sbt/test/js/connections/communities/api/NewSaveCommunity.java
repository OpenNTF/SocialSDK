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
public class NewSaveCommunity extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CommunityNewSave";

    public NewSaveCommunity() {
        createCommunity = false;
    }

    @Test
    public void testCommunityNewSave() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));

        community = getCommunity(json.getString("getCommunityUuid"));
        
        Assert.assertEquals(community.getCommunityUuid(), json.getString("getCommunityUuid"));
        Assert.assertEquals(community.getTitle(), json.getString("getTitle"));
        Assert.assertEquals(community.getSummary(), json.getString("getSummary"));
        Assert.assertEquals(community.getCommunityUrl(), json.getString("getCommunityUrl"));
        Assert.assertEquals(community.getLogoUrl(), json.getString("getLogoUrl"));
        Assert.assertEquals(community.getMemberCount(), json.getInt("getMemberCount"));
        Assert.assertEquals(community.getCommunityType(), json.getString("getCommunityType"));
        Assert.assertEquals(community.getAuthor().getName(), json.getJsonObject("getAuthor").getString("authorName"));
        Assert.assertEquals(community.getAuthor().getEmail(), json.getJsonObject("getAuthor").getString("authorEmail"));
        Assert.assertEquals(community.getAuthor().getUserid(), json.getJsonObject("getAuthor").getString("authorUserid"));
        Assert.assertEquals(community.getContributor().getName(), json.getJsonObject("getContributor").getString("contributorName"));
        Assert.assertEquals(community.getContributor().getEmail(), json.getJsonObject("getContributor").getString("contributorEmail"));
        Assert.assertEquals(community.getContributor().getUserid(), json.getJsonObject("getContributor").getString("contributorUserid"));
    }
    
}
