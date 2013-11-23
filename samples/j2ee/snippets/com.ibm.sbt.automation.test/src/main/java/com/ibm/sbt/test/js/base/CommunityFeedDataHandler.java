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
package com.ibm.sbt.test.js.base;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class CommunityFeedDataHandler extends BaseApiTest {
    
    static final String SNIPPET_ID = "Toolkit_Base_CommunityFeedDataHandler";

    @Test
    public void testCommunityFeedDataHandler() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertEquals(5, jsonList.size());
        JsonJavaObject json = (JsonJavaObject)jsonList.get(4);
        Assert.assertEquals("http://communities.ibm.com:2006/service/atom/community/instance?communityUuid=07c9947b-bdf3-4106-95d5-949bd5e0fd9f", json.getString("getEntityId"));
        Assert.assertEquals("http://communities.ibm.com:2006/service/atom/community/instance?communityUuid=07c9947b-bdf3-4106-95d5-949bd5e0fd9f", json.getString("communityUuid")); 
        Assert.assertEquals("Public Community 2", json.getString("title"));
        Assert.assertEquals("This is a test public community", json.getString("summary"));
        Assert.assertEquals("https://qs.renovations.com:444/communities/service/html/communityview?communityUuid=07c9947b-bdf3-4106-95d5-949bd5e0fd9f", json.getString("alternateUrl"));
        Assert.assertEquals("https://qs.renovations.com:444/communities/service/atom/community/instance?communityUuid=07c9947b-bdf3-4106-95d5-949bd5e0fd9f", json.getString("selfUrl"));
        Assert.assertEquals("https://qs.renovations.com:444/communities/service/html/image?communityUuid=07c9947b-bdf3-4106-95d5-949bd5e0fd9f&lastMod=1364454917628", json.getString("logoUrl"));
        //TODO: fix after switch to domino 90 Assert.assertEquals(new String[] { "community","tag1","tag2","tag3" }, json.getAsArray("tags").toArray());
        // TODO validate the content is not returned as part of the feed
        //Assert.assertEquals("<p dir=\"ltr\">\r\n\t\tThis is a test public community</p>", json.getString("content"));
        Assert.assertEquals(7, json.getInt("memberCount"));
        Assert.assertEquals("public", json.getString("communityType"));
        Assert.assertEquals("2013-03-28T07:15:17.628Z", json.getString("published"));
        Assert.assertEquals("2013-03-28T07:15:20.291Z", json.getString("updated"));
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("authorUid"));
        Assert.assertEquals("Frank Adams", json.getString("authorName"));
        Assert.assertEquals("frankadams@renovations.com", json.getString("authorEmail"));
        Assert.assertEquals("0EE5A7FA-3434-9A59-4825-7A7000278DAA", json.getString("contributorUserid"));
        Assert.assertEquals("Frank Adams", json.getString("contributorName"));
        Assert.assertEquals("frankadams@renovations.com", json.getString("contributorEmail"));
    }

}





