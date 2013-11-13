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
package com.ibm.sbt.test.js.connections.communities.api;

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
public class CommunityCreateBody extends BaseApiTest {
    
    static final String SNIPPET_ID = "Social_Communities_API_CommunityCreateBody";
    
    static final String RequestBody = "\n<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">\n<title type=\"text\">Community Title</title>\n<content type=\"html\">Community Content</content>\n<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>\n\n<category term=\"tag1\"></category>\n\n<category term=\"tag2\"></category>\n\n<category term=\"tag3\"></category>\n\n<snx:communityType>public</snx:communityType>\n\n\n</entry>\n";

    public CommunityCreateBody() {
        setAuthType(AuthType.NONE);
    }

    @Test
    public void testCreateBody() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        Assert.assertFalse(jsonList.isEmpty());
        Assert.assertEquals(RequestBody, ((JsonJavaObject)jsonList.get(0)).getString("requestBody"));
        Assert.assertEquals(RequestBody, ((JsonJavaObject)jsonList.get(1)).getString("requestBody"));
    }

}
