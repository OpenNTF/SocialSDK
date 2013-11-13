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
public class CommunityMembersFeedDataHandler extends BaseApiTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CommunityMembersFeedDataHandler";

    @Test
    public void testCommunityFeedDataHandler() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(5, jsonList.size());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("getEntityId"));
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("userid")); 
        Assert.assertEquals("frankadams@renovations.com", json.getString("email"));
        Assert.assertEquals("Frank Adams", json.getString("name"));
        Assert.assertEquals("owner", json.getString("role"));
    }

}
