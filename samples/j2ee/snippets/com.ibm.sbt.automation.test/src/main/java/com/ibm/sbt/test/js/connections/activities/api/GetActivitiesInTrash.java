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

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.automation.core.test.connections.BaseActivitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.activities.Activity;

/**
 * @author mwallace
 * 
 * @since 6 Mar 2013
 */
public class GetActivitiesInTrash extends BaseActivitiesTest {

    static final String SNIPPET_ID = "Social_Activities_API_GetActivitiesInTrash";
    
    Activity activity;

   	@Before
   	public void init() throws ClientServicesException {
   		activity = createActivity();	
   		deleteActivity(activity.getActivityUuid());
   	}
   
   	
    @Test
    public void testGetActivitiesInTrash() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("Get activities In Trash returned no activities", jsonList.isEmpty());
    }

}
