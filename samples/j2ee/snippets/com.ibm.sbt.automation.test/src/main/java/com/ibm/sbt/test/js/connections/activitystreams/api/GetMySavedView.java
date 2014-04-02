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
package com.ibm.sbt.test.js.connections.activitystreams.api;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseActivityStreamsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamServiceException;

/**
 * @author rajmeetbal
 *  
 * @date 08 May 2013
 */
public class GetMySavedView extends BaseActivityStreamsTest {
    
    static final String SNIPPET_ID = "Social_ActivityStreams_API_GetMySavedView";

    public GetMySavedView() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Before
    public void setup(){
    	try {
			saveEntry();
		} catch (ActivityStreamServiceException e) {
			e.printStackTrace();
			Assert.fail("Problem saving event.");
		}
    }
    
    @Test
    public void testGetMySavedView() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("GetMySavedView returned no results", jsonList.isEmpty());
    }

}
