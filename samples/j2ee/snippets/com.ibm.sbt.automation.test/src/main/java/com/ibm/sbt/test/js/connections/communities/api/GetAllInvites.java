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

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.communities.Invite;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class GetAllInvites extends BaseCommunitiesTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_GetAllInvites";
    
    public GetAllInvites() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetAllInvites() {
    	String name2 = getProperty("sample.displayName2");
    	String userid2 = getProperty("sample.userId2");
    	String email2 = getProperty("sample.email2");
    	if (environment.isSmartCloud()) {
        	name2 = getProperty("smartcloud.displayName2");
        	userid2 = getProperty("smartcloud.userId2");
        	email2 = getProperty("smartcloud.email2");
    	}
        Invite invite = createInvite(community, userid2);
    	
        addSnippetParam("CommunityService.communityUuid", community.getCommunityUuid());
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse("Get all invites returned no invites", jsonList.isEmpty());
        assertInviteValid((JsonJavaObject)jsonList.get(0), invite);
    }

}
