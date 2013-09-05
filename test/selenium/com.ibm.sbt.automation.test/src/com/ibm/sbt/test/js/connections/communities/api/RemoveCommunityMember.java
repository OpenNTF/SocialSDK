/*
 * © Copyright IBM Corp. 2012
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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.communities.Member;

/**
 * @author mwallace
 * 
 * @date 5 Mar 2013
 */
public class RemoveCommunityMember extends BaseCommunitiesTest {

    static final String SNIPPET_ID = "Social_Communities_API_RemoveCommunityMember";

    @Test
    public void testRemoveMember() {
    	String id = getProperty("sample.email2");
    	if (environment.isSmartCloud()) {
    		id = getProperty("smartcloud.id2");
    	}
        addMember(community, id, "member");
            	
        addSnippetParam("sample.communityId", community.getCommunityUuid());
        addSnippetParam("sample.id2", id);
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        JsonJavaObject json = previewPage.getJson();
        Assert.assertFalse("Remove member failed", hasMember(community, getProperty("sample.email2")));
    }

}
