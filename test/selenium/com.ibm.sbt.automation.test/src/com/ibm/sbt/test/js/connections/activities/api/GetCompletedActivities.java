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
package com.ibm.sbt.test.js.connections.activities.api;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseAuthServiceTest;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseActivitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 * 
 * @date 6 Mar 2013
 */
public class GetCompletedActivities extends BaseActivitiesTest {

    static final String SNIPPET_ID = "Social_Activities_API_GetCompletedActivities";
    
    public GetCompletedActivities() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetCompletedActivities() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        // TODO create soem activities
        //Assert.assertFalse("Get my activities returned no activities", jsonList.isEmpty());
    }

}
