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
package com.ibm.sbt.test.js.base;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class BaseServiceGetEntity extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Toolkit_Base_BaseServiceGetEntity";
    
    public BaseServiceGetEntity() {
        setAuthType(AuthType.NONE);
        createCommunity = false;
    }
    
    @Test
    public void testGetEntityInvalid() {
        addSnippetParam("sample.communityId", "foo");
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse(jsonList.isEmpty());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("The community which this resource or page is associated with does not exist.", json.getString("message"));
    }

    @Test
    public void testGetEntityNull() {
        addSnippetParam("sample.communityId", "");
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse(jsonList.isEmpty());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals(400, json.getInt("code"));
        Assert.assertEquals("Invalid argument 'undefined', expected valid entity identifier.", json.getString("message"));
    }

    @Test @Ignore
    public void testGetEntity() {
    	// only create a community for this test case
    	createCommunity = true;
    	createCommunity();
    	
        addSnippetParam("sample.communityId", community.getCommunityUuid());
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(2, jsonList.size());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        Assert.assertEquals("createEntity", json.getString("callback"));
        Assert.assertNotNull(json.getString("data"));
        json = (JsonJavaObject)jsonList.get(1);
        Assert.assertNotNull(json.getString("id"));
        Assert.assertNotNull(json.getString("service"));
        Assert.assertNotNull(json.getString("dataHandler"));
    }

}
