/*
 * © Copyright IBM Corp. 2013
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at,
 * 
 * http,//www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or 
 * implied. See the License for the specific language governing 
 * permissions and limitations under the License.
 */
package com.ibm.sbt.test.js.sbt;

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
public class TransportGet extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Toolkit_TransportGet";
    
    public TransportGet() {
        setAuthType(AuthType.NONE);
    }
    
    @Test
    public void testTransportGet() {
        addSnippetParam("sample.communityId", community.getCommunityUuid());
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(2, jsonList.size());
        Assert.assertNotNull(((JsonJavaObject)jsonList.get(0)).getString("data"));
        Assert.assertNotNull(((JsonJavaObject)jsonList.get(1)).getString("url"));
        Assert.assertNotNull(((JsonJavaObject)jsonList.get(1)).getString("options"));
        Assert.assertNotNull(((JsonJavaObject)jsonList.get(1)).getString("data"));
        Assert.assertNotNull(((JsonJavaObject)jsonList.get(1)).getString("text"));
        Assert.assertNotNull(((JsonJavaObject)jsonList.get(1)).getString("xhr"));
        Assert.assertEquals(200, ((JsonJavaObject)jsonList.get(1)).getInt("status"));
    }

}
