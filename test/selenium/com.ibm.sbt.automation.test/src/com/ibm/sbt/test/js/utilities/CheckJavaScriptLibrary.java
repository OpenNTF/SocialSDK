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
package com.ibm.sbt.test.js.utilities;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseAuthServiceTest;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * @author priand
 */
public class CheckJavaScriptLibrary extends BaseTest {

	public static final String SNIPPET_ID = "Utilities_Check_JavaScript_Library";
	
    @Test
    public void testNoError() {
        String text = executeSnippet(SNIPPET_ID);
        assertTrue(StringUtil.indexOfIgnoreCase(text, "dojo")>=0);
        assertTrue(StringUtil.indexOfIgnoreCase(text, "1.8.0")>=0);
    }
    
    /**
     * @param snippetId
     * @return
     */
    protected String executeSnippet(String snippetId) {
        ResultPage resultPage = launchSnippet(snippetId, authType);
        String text =  resultPage.getText();
        
        //dumpResultPage(resultPage);
                
        if (text != null && text.startsWith("Show Snippet Code")) {
            text = text.substring("Show Snippet Code".length());
        }
        return (text == null) ? null : text.trim();
    }    
}
