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
package com.ibm.sbt.test.js.connections.forums.api;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseForumsTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 19 Mar 2013
 */
public class CreateForumTopic extends BaseForumsTest {
    
	static final String SNIPPET_ID = "Social_Forums_API_CreateForumTopic";

    public CreateForumTopic() {
    	setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testCreateForumTopic() {
    	addSnippetParam("ForumService.forumUuid", forum.getForumUuid());
    	
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        assertForumTopicProperties(json);
    }
    
}
