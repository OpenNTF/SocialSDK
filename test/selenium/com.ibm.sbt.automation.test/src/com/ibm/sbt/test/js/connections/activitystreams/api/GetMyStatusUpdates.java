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
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseActivityStreamsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author rajmeetbal
 *  
 * @date 08 May 2013
 */
public class GetMyStatusUpdates extends BaseActivityStreamsTest {
    
	static final String SNIPPET_POST_ID = "Social_ActivityStreams_API_PostAStatusUpdate";
    static final String SNIPPET_ID = "Social_ActivityStreams_API_GetMyStatusUpdates";

    public GetMyStatusUpdates() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Test
    public void testGetMyStatusUpdates() {
    	JavaScriptPreviewPage postPreviewPage = executeSnippet(SNIPPET_POST_ID);
        JsonJavaObject json = postPreviewPage.getJson();
        String newUpdateId = json.getJsonObject("entry").getString("id");
        Assert.assertNotNull("While testing GetMyStatusUpdates new Update Id found null", newUpdateId);
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("GetMyStatusUpdates returned no results", jsonList.isEmpty());
        if(!jsonList.isEmpty()){
        	Assert.assertFalse("While testing GetMyStatusUpdates latest created status update is not found in the list of status updates", !isLatestEntryFound(jsonList, newUpdateId));
        }	
    }
    
}
