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

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class GetMyCommunities extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_GetMyCommunities";
    
    public GetMyCommunities() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetMyCommunities() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("Get my communities returned no communities", jsonList.isEmpty());
        for (int i=0; i<jsonList.size(); i++) {
            JsonJavaObject json = (JsonJavaObject)jsonList.get(i);
            Assert.assertNotNull(json.getString("getCommunityUuid"));
            Assert.assertNotNull(json.getString("getTitle"));
            Assert.assertNotNull(json.getString("getCommunityType"));
            Assert.assertNotNull(json.getString("getAuthor"));
            Assert.assertNotNull(json.getString("getContributor"));
            //Assert.assertNotNull(json.getString("getSummary"));
            Assert.assertNotNull(json.getString("getCommunityUrl"));
            Assert.assertNotNull(json.getString("getLogoUrl"));
            Assert.assertNotNull(json.getString("getMemberCount"));
            Assert.assertNotNull(json.getString("getPublished"));
            Assert.assertNotNull(json.getString("getUpdated"));
            Assert.assertNotNull(json.getString("getSubCommunities"));
            Assert.assertNotNull(json.getString("getMembers"));
            Assert.assertNotNull(json.getString("getMember"));
        }
    }

}
