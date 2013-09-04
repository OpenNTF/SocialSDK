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
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class CommunityInvitesFeedDataHandler extends BaseApiTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CommunityInvitesFeedDataHandler";

    @Test
    public void testCommunityFeedDataHandler() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(3, jsonList.size());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals("urn:lsid:ibm.com:communities:invite-bf9a49e2-e961-45ad-8b8a-edacce62b3c9-21d28e8a-4af8-47c7-913c-d4194e0a7664", json.getString("getEntityId"));
        Assert.assertEquals("Invitation for Frank Adams to Lucilles Restricted Community 3", json.getString("title"));
        Assert.assertEquals("2013-03-31T14:19:13.671Z", json.getString("updated"));
        Assert.assertEquals("662B6E42-3313-8316-4825-7A7000268F34", json.getString("authorUserid"));
        Assert.assertEquals("Lucille Suarez", json.getString("authorName"));
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("contributorUserid"));
        Assert.assertEquals("Frank Adams", json.getString("contributorName"));
    }

}
