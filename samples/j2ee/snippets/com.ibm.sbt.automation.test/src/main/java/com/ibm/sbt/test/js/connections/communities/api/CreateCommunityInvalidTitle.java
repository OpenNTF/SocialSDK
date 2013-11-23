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
package com.ibm.sbt.test.js.connections.communities.api;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class CreateCommunityInvalidTitle extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CreateCommunityInvalidTitle";

    public CreateCommunityInvalidTitle() {
        setAuthType(AuthType.AUTO_DETECT);
        createCommunity = false;
    }

    @Test
    public void testCreateCommunityInvalidTitle() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID, 10000);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(2, jsonList.size());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(0);
        String communityUuid = json.getString("getCommunityUuid");
        Assert.assertNotNull(communityUuid);
        json = (JsonJavaObject)jsonList.get(1);
        String message = json.getString("message");
        Assert.assertEquals("A community with the requested name already exists, choose a different name and resubmit.", message);
        deleteCommunity(communityUuid);
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.BaseTest#waitForResult(int)
     */
    @Override
    public WebElement waitForResult(int timeout) {
        return waitForJsonList(2, timeout);
    }

}
