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

import junit.framework.Assert;

import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.test.connections.BaseProfilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;


/**
 * @author projsaha
 *  
 * @date 3 May 2013
 */
public class CreateAndDeleteProfile extends BaseProfilesTest {
    
    static final String SNIPPET_ID1 = "Social_Profiles_API_CreateProfile";
    static final String SNIPPET_ID2 = "Social_Profiles_API_DeleteProfile";
	
    /**
	 * Test method which runs the createProfile sample followed by deleteProfile sample
	 */
    
    @Test
    public void testCreateAndDeleteProfile() {
    	String tst = String.valueOf(System.currentTimeMillis());
    	String displayName = "user " + tst;
    	String id = "user" + tst;
    	String email = "user"+tst+"@renovations.com";
    	String name = "user";
    	String surname = tst;
    	String dname = "CN="+displayName+"renovations";
    	
    	
		addSnippetParam("sample.createProfileEmail", email);
		addSnippetParam("sample.createProfileUid", id);
		addSnippetParam("sample.createProfileDistinguishedName", dname);
		addSnippetParam("sample.createProfileDisplayName", displayName);
		addSnippetParam("sample.createProfileGivenNames", name);
		addSnippetParam("sample.createProfileSurName", surname);
    	
    	
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID1);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertEquals(getProperty("sample.createProfileUid"), json.getString(id));
        Assert.assertEquals(getProperty("sample.createProfileEmail"), json.getString(email));
        Assert.assertEquals(getProperty("sample.createProfileDisplayName"), json.getString(displayName));
        previewPage = executeSnippet(SNIPPET_ID2);
        json = previewPage.getJson();
        Assert.assertEquals(getProperty("sample.createProfileUid"), json.getString(id));
        previewPage = executeSnippet(SNIPPET_ID2);
        json = previewPage.getJson();
        Assert.assertEquals(404, json.getInt("code"));
        Assert.assertEquals("CLFRN1172E: The request is invalid.", json.getString("message"));
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#getProperty(java.lang.String)
     */
    /*@Override
    public String getProperty(String name) {
        if (TestEnvironment.PROP_BASIC_USERNAME.equals(name)) {
            return "admin";
        }
        if (TestEnvironment.PROP_BASIC_PASSWORD.equals(name)) {
            return "passw0rd";
        }
        return super.getProperty(name);
    }*/
    
}
