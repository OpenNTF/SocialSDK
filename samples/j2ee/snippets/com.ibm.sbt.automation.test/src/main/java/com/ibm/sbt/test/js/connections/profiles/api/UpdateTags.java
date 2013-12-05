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
package com.ibm.sbt.test.js.connections.profiles.api;

import java.util.List;

import org.junit.Assert;

import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.test.connections.BaseProfilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;


/**
 * @author rajmeetbal
 *  
 * @date 20 Nov 2013
 */
public class UpdateTags extends BaseProfilesTest {
    
    static final String SNIPPET_ID = "Social_Profiles_API_UpdateTags"; 
	
    @Test
    public void testUpdateTagsUsingEmail() {    
    	addSnippetParam("sample.email1", getProperty("sample.email1"));
    	addSnippetParam("sample.email2", getProperty("sample.email2"));
    	JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);       
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("Create tags returned no results", jsonList.isEmpty());
        for (int i=0; i<jsonList.size(); i++) {
            JsonJavaObject json = (JsonJavaObject)jsonList.get(i);
            Assert.assertFalse("Created tags do not match the expected tag values", "testTag1".equals(json.getString("getTerm")) || "testTag2".equals(json.getString("getTerm")));           
        }
    } 
    
    @Test
    public void testUpdateTagsInvalidSourceUser() {    
    	addSnippetParam("sample.email1", "");
    	addSnippetParam("sample.email2", getProperty("sample.id2"));
            
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
    }
    
    @Test
    public void testUpdateTagsInvalidTargetUser() {    
    	addSnippetParam("sample.email1", getProperty("sample.id1"));
    	addSnippetParam("sample.email2", "");
            
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
    }
    
}
