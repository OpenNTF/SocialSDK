package com.ibm.sbt.automation.core.test;

import org.openqa.selenium.WebElement;
import com.ibm.sbt.automation.core.test.pageobjects.GridResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.VCardResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.WrapperResultPage;
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

/**
 * @author Francis 
 * @date 16 Jul 2013
 */
public class BaseWrapperTest extends BaseTest {
    /**
     * Default constructor
     */
    public BaseWrapperTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    /**
     * Check if the FileGrid is present inside the iframe.
     * @param snippetId
     * @return true if displayed
     */
    protected boolean checkFileGridWrapper(String snippetId) {
        WrapperResultPage resultPage = launchSnippet(snippetId);
        switchContextToIframe(resultPage, resultPage.getFileGridFrame());
        GridResultPage innerIframePage = new GridResultPage(resultPage);
        innerIframePage.gridId="innerGridDiv";
        BaseGridTest gridTest = new BaseGridTest();
        
        return gridTest.checkGrid(innerIframePage, true, true, snippetId);
    }
    
    /**
     * Check if the VCard is present inside the iframe.
     * @param snippetId
     * @return
     */
    protected boolean checkProfileCardWrapper(String snippetId){
        WrapperResultPage resultPage = launchSnippet(snippetId);
        switchContextToIframe(resultPage, resultPage.getProfileCardFrame());
        VCardResultPage innerIframePage = new VCardResultPage(resultPage);
        BaseVCardTest vCardTest = new BaseVCardTest();
        
        return vCardTest.checkProfileVCard(innerIframePage);
    }

    /**
     * Launch the ActivityStream snippet 
     * @param snippetId
     * @return the result page
     */
    @Override
    public WrapperResultPage launchSnippet(String snippetId) {
        WrapperResultPage resultPage = new WrapperResultPage(super.launchSnippet(snippetId, authType));
        return resultPage;
    }
    
    protected WrapperResultPage switchContextToIframe(WrapperResultPage resultPage, WebElement iframe){
        resultPage.getWebDriver().switchTo().frame(iframe);
        return resultPage;
    }
    
    @Override
    protected boolean isEnvironmentValid() {
        return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
}
