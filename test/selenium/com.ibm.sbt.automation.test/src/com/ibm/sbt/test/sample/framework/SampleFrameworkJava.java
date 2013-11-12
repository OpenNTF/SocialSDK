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
package com.ibm.sbt.test.sample.framework;

import static org.junit.Assert.assertTrue;
import java.util.List;
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
public class SampleFrameworkJava extends BaseSampleFrameworkTest{
	
	public SampleFrameworkJava(){
		snippetType = SnippetType.JAVAFRAMEWORK;
	}
	
	
	@Test
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
     * Check if the Sample Framework's main content is displayed on the page
     * 
     * @param snippetId
     * @return true if displayed
     */
    protected boolean checkSampleFramework() {
        SampleFrameworkResultPage resultPage = launchSampleFramework();
        return resultPage.getMainContent().isDisplayed();
    }
    
    /*
     * Check if the js snippet has some content, and test that the nav bar works.
     * 
     * @param snippetId - the sample framework page
     * @return true if displayed
     */
    private boolean checkCodeDivs(SampleFrameworkResultPage resultPage) {
        WebElement jspDiv = resultPage.getJspSnippetDiv();
        String jspContent = jspDiv.getAttribute("innerHTML");
        WebElement ulNav = resultPage.getCodeNav();
        List<WebElement> tabList = ulNav.findElements(By.xpath(".//a"));
        WebDriverWait wait = new WebDriverWait(resultPage.getWebDriver(), 2);
        tabList.get(1).click();
        boolean docDivDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(SampleFrameworkResultPage.DOCSNIPPETDIV))).isDisplayed();

        return jspContent != null && docDivDisplayed;
    }
}
