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
package com.ibm.sbt.automation.core.test;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class BaseServiceTest extends BaseTest {
    
    private String noErrorMsg;
    private String expectedErrorMsg;
    
    private final String[] ErrorMessages = {
                "Error received. Error Code",
                "Error, unable to load:",
                "Caused by: com.ibm.sbt.services.client.ClientServicesException",
                "Exception occurred",
                "Caused by:",
                "HTTP Status 500",
                "HTTP Status 404",
                "Problem occurred",
                "Username:"//when stuck on login screen
    };
    
    /**
     * 
     * @return
     */
    protected boolean checkNoError(String snippetId, boolean allowEmpty) {
        String text = executeSnippet(snippetId);
        System.out.println("executeSnippet: "+text);
        return containsNoError(text, allowEmpty);
    }

    /**
     * 
     * @return
     */
    protected boolean checkNoError(String snippetId) {
        return checkNoError(snippetId, false);
    }

    /**
     * 
     * @return
     */
    protected boolean checkExpected(String snippetId, String expected) {
        String text = executeSnippet(snippetId);
        return matchesExpected(text, expected);
    }
    
    
    /*
     * Return true if the result page contains no error message.
     */
    protected boolean containsNoError(String result) {
    	return containsNoError(result, false);
    }
    
    /*
     * Return true if the result page contains no error message.
     */
    protected boolean containsNoError(String result, boolean allowEmpty) {
        boolean retVal = true;
        if (!allowEmpty && StringUtil.isEmpty(result)) {
            noErrorMsg = "Empty result was returned for: " + getSnippetId();
            retVal = false;
        } else {
            for (int i=0; i<ErrorMessages.length; i++) {
                if (result.contains(ErrorMessages[i])) {
                    noErrorMsg = "Error message for: " + getSnippetId() + ": " + result;
                    retVal = false;
                }
            }
        }
        if (!retVal) {
            Trace.log(noErrorMsg);
        }
        return retVal;
    }

    /*
     * Return true if the result page contains or matches the expected result.
     */
    protected boolean matchesExpected(String result, String expected) {
        boolean matches = result.equalsIgnoreCase(expected) || result.contains(expected);
        if (!matches) {
            expectedErrorMsg = "Match failure for " + getSnippetId() + 
                " expected: '" + expected + "' result: '" + result + "'";
            Trace.log(expectedErrorMsg);
        }
        return matches;
    }
    
    /**
     * @return the noErrorMsg
     */
    public String getNoErrorMsg() {
        return noErrorMsg;
    }
    
    /**
     * @return the expectedErrorMsg
     */
    public String getExpectedErrorMsg() {
        return expectedErrorMsg;
    }
    
    /**
     * @param snippetId
     * @return
     */
    protected JavaScriptPreviewPage executeSnippet1(String snippetId) {
    	 ResultPage resultPage = launchSnippet(snippetId, authType, 0);
         JavaScriptPreviewPage previewPage = new JavaScriptPreviewPage(resultPage);
         Trace.log(previewPage.getText());
         return previewPage;
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
