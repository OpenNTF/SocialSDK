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
public class CreateTags extends BaseProfilesTest {
    
    static final String SNIPPET_ID = "Social_Profiles_API_CreateTags"; 
	
    @Test
    public void testCreateTagsUsingEmail() {    
    	addSnippetParam("sample.email1", "FrankAdams@renovations.com");
    	addSnippetParam("sample.email2", "BillJordan@renovations.com");
    	JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);       
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("Create tags returned no results", jsonList.isEmpty());
        for (int i=0; i<jsonList.size(); i++) {
            JsonJavaObject json = (JsonJavaObject)jsonList.get(i);
            Assert.assertFalse("Created tags do not match the expected tag values", "testTag1".equals(json.getString("getTerm")) || "testTag2".equals(json.getString("getTerm")));           
        }
    } 
    
    @Test
    public void testCreateTagsInvalidSourceUser() {    
    	addSnippetParam("sample.email1", "");
    	addSnippetParam("sample.email2", "04F26086-1A63-D244-4825-7A70002586FA");
            
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
    }
    
    @Test
    public void testCreateTagsInvalidTargetUser() {    
    	addSnippetParam("sample.email1", "0EE5A7FA-3434-9A59-4825-7A7000278DAA");
    	addSnippetParam("sample.email2", "");
            
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(400, json.getInt("code"));
    }
    
}
