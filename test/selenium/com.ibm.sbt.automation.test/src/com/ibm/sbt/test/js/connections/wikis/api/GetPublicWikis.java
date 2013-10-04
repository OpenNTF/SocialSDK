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
package com.ibm.sbt.test.js.connections.wikis.api;

import java.util.List;

import org.junit.Test;

import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseWikisTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class GetPublicWikis extends BaseWikisTest {
    
    static final String SNIPPET_ID = "Social_Wikis_API_GetPublicWikis";

    public GetPublicWikis() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetPublicWikis() {
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        for (int i=0; i<jsonList.size(); i++) {
        	//assertWikiValid((JsonJavaObject)jsonList.get(i), true);
        }
    }
    
}
