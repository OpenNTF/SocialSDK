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
package com.ibm.sbt.test.sample.framework;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ibm.sbt.automation.core.test.BaseSampleFrameworkTest;
import com.ibm.sbt.automation.core.test.pageobjects.SampleFrameworkResultPage;

/**
 * @author Francis
 * @date 24 May 2013
 */
public class SampleFrameworkJavaScript extends BaseSampleFrameworkTest {

    public SampleFrameworkJavaScript() {
        snippetType = SnippetType.JAVASCRIPTFRAMEWORK;
    }

    @Test
    @Ignore
    public void testFramework() {
        SampleFrameworkResultPage resultPage = launchSampleFramework();
        assertTrue("Expected the main container to be displayed", checkMainContent(resultPage));
        assertTrue("Expected tree to be displayed", checkTree(resultPage));
        if(getTestEnvironment().isSmartCloud())
            assertTrue("Expected the smartcloud navigation bar to be present", checkSmartcloudNavBar(resultPage));
        
        clickLeafNode(resultPage);

        assertTrue("Expected the code divs to contain code after clicking leaf node", checkCodeDivs(resultPage));
        toIframeContext(resultPage);
        assertTrue("Expected iframe to contain html after clicking leaf node", checkIframe(resultPage));
    }

    /*
     * Check if the js snippet has some content, and test that the nav bar works.
     * 
     * @param snippetId - the sample framework page
     * @return true if displayed
     */
    private boolean checkCodeDivs(SampleFrameworkResultPage resultPage) {
        WebElement jsDiv = resultPage.getJsSnippetDiv();
        String jsContent = jsDiv.getAttribute("innerHTML");
        WebElement ulNav = resultPage.getCodeNav();
        List<WebElement> tabList = ulNav.findElements(By.xpath(".//a"));
        WebDriverWait wait = new WebDriverWait(resultPage.getWebDriver(), 5l);
        tabList.get(1).click();
        boolean htmlDivDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(SampleFrameworkResultPage.HTMLSNIPPETDIV))).isDisplayed();
        tabList.get(2).click();
        boolean cssDivDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(SampleFrameworkResultPage.CSSSNIPPETDIV))).isDisplayed();
        tabList.get(3).click();
        boolean docDivDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(SampleFrameworkResultPage.DOCSNIPPETDIV))).isDisplayed();

        return jsContent != null && htmlDivDisplayed && cssDivDisplayed && docDivDisplayed;
    }
}
