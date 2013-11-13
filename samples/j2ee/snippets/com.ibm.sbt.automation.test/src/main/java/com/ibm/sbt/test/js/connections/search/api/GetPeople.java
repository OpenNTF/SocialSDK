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
package com.ibm.sbt.test.js.connections.search.api;

import java.util.List;

import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseSearchTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

/**
 * @author mwallace
 *  
 * @date 25 Mar 2013
 */
public class GetPeople extends BaseSearchTest {
    
    static final String SNIPPET_ID = "Social_Search_API_GetPeople";

    public GetPeople() {
        setAuthType(AuthType.AUTO_DETECT);
    }

    @Test
    public void testGetPeople() {
        addSnippetParam("sample.searchQuery", "Frank");
        
        JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
        List jsonList = previewPage.getJsonList();
        for (int i=0; i<jsonList.size(); i++) {
        	assertFacetValueValid((JsonJavaObject)jsonList.get(i));
        }
    }
    
}
