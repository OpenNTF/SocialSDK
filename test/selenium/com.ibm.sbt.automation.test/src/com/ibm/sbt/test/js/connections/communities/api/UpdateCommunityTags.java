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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class UpdateCommunityTags extends BaseCommunitiesTest {

    static final String SNIPPET_ID = "Social_Communities_API_UpdateCommunityTags";
    
    @Test
    public void testUpdateTags() {
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        List tags = (List)json.get("getTags");
        Assert.assertNotNull("getTags returned nothing", tags);
        Assert.assertEquals(3, tags.size());
        Assert.assertEquals("newtag1", tags.get(0));
        Assert.assertEquals("newtag2", tags.get(1));
        Assert.assertEquals("newtag3", tags.get(2));
    }

}
