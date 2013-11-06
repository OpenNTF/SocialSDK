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
package com.ibm.sbt.automation.core.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.SampleFrameworkResultPage;

/**
 * @author Francis
 * @date 24 May 2013
 */
public class BaseSampleFrameworkTest extends BaseTest {

    public BaseSampleFrameworkTest() {
        authType = AuthType.NONE;
    }

    /**
     * Launch the Sample Framework
     * 
     * @param snippetId
     * @return the result page
     */
    protected SampleFrameworkResultPage launchSampleFramework() {
        ResultPage resultPage = super.launchSnippet("");
        return wrapResultPage(resultPage);
    }

    /**
     * Wrap the environment result page in a SampleFrameworkResultPage
     * 
     * @param resultPage
     * @return the result page
     */
    protected SampleFrameworkResultPage wrapResultPage(ResultPage resultPage) {
        return new SampleFrameworkResultPage(resultPage);
    }

    @Override
    protected boolean isEnvironmentValid() {
        return super.isEnvironmentValid();
    }
    
    public void toIframeContext(SampleFrameworkResultPage resultPage) {
        WebElement iframeNode = resultPage.getPreviewFrame();
        resultPage.getWebDriver().switchTo().frame(iframeNode);
    }
    
    public void clickLeafNode(SampleFrameworkResultPage resultPage) {
        WebElement leafNode = resultPage.getTreeLeaf();
        leafNode.click();
    }
    
    /*
     * Check if the tree is displayed on the page
     * 
     * @param snippetId - the sample framework page
     * @return true if displayed
     */
    public boolean checkTree(SampleFrameworkResultPage resultPage) {
        return resultPage.getTree().isDisplayed();
    }
    
    public boolean checkSmartcloudNavBar(SampleFrameworkResultPage resultPage){
        return resultPage.getSmartcloudNavBar().isDisplayed();
    }
    
    /*
     * Check if the Sample Framework's main content is displayed on the page
     * 
     * @param resultPage - The sample framework page
     * @return true if displayed
     */
    public boolean checkMainContent(SampleFrameworkResultPage resultPage) {
        return resultPage.getMainContent().isDisplayed();
    }
    
    /*
     * Check if clicking a leaf node populates the iframe. NOTE: Switch context to the iframe first.
     * 
     * @param snippetId - the sample framework page
     * @return true if displayed
     */
    public boolean checkIframe(SampleFrameworkResultPage resultPage) {
        WebDriverWait wait = new WebDriverWait(resultPage.getWebDriver(), 20l);
        WebElement iframeBody = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));
        String bodyClass = iframeBody.getAttribute("class");
        
        return bodyClass != null;
    }
   
@Override
public String getAuthenticatedCondition() {
    return "idWithText";
}
@Override
public String getAuthenticatedMatch() {
    return "tree";
}

}
