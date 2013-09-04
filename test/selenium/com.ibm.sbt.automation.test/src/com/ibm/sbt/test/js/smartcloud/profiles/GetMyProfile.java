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
package com.ibm.sbt.test.js.smartcloud.profiles;

import org.junit.Assert;
import org.junit.Test;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.automation.core.test.smartcloud.BaseProfilesTest;
import com.ibm.sbt.services.client.SBTServiceException;

/**
 * @author Lorenzo Boccaccia
 * @date 22 May 2013
 */
public class GetMyProfile extends BaseProfilesTest {

    static final String SNIPPET_ID = "Social_Profiles_SmartCloud_API_GetMyProfile";

    @Test
    public void testGetMyProfile() throws SBTServiceException {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertFalse("Get Profile  returned no results", json.isEmpty());
    }

    @Test
    public void testOrgIdRegression() throws SBTServiceException {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertFalse("Get Profile  returned no results", json.isEmpty());
        String orgId = json.getString("getOrgId");
        Assert.assertFalse(Integer.valueOf(orgId) == 0);
    }
}