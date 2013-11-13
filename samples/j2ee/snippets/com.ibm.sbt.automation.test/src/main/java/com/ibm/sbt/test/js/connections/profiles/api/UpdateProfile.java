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
package com.ibm.sbt.test.js.connections.profiles.api;

import junit.framework.Assert;

import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.test.connections.BaseProfilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;


/**
 * @author projsaha
 *  
 * @date 3 May 2013
 */
public class UpdateProfile extends BaseProfilesTest {
    
    static final String SNIPPET_ID = "Social_Profiles_API_UpdateProfile"; 
    static final String SNIPPET_ID2 = "Social_Profiles_API_UpdateProfilePattern2"; 
	
    @Test
    public void testUpdateProfile() { 	    	
    	addSnippetParam("sample.updateProfileJobTitle", "Software Engineer");
        addSnippetParam("sample.updateProfileBuilding", "1");
        addSnippetParam("sample.updateProfileFloor", "2nd");
        addSnippetParam("sample.updateProfileTelephoneNumber", "343343"); // missing input here
        
    	
    	JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();        
        Assert.assertEquals("Software Engineer", json.getString("jobTitle"));   
        Assert.assertEquals("1", json.getString("building"));  
        Assert.assertEquals("2nd", json.getString("floor"));  
        Assert.assertEquals("343343", json.getString("telephoneNumber"));  
    }   
    
    @Test
    public void testUpdateProfilePattern2() {    
    	addSnippetParam("sample.updateProfileJobTitle", "Software Associate Engineer");
        addSnippetParam("sample.updateProfileBuilding", "2");
        addSnippetParam("sample.updateProfileFloor", "3rd");
        addSnippetParam("sample.updateProfileTelephoneNumber", "343344"); // missing input here
        
    	JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID2);
        JsonJavaObject json = previewPage.getJson();        
        Assert.assertEquals("Software Associate Engineer-1", json.getString("jobTitle"));   
        Assert.assertEquals("2-1", json.getString("building"));  
        Assert.assertEquals("3rd-1", json.getString("floor"));  
        Assert.assertEquals("343344", json.getString("telephoneNumber"));  
    }   
}
