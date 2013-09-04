package com.ibm.sbt.automation.core.environment;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseTest;
import com.ibm.sbt.automation.core.test.BaseTest.SnippetType;
import com.ibm.sbt.automation.core.test.pageobjects.PlaygroundResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;


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
 * @author mkataria
 * @author mwallace
 * 
 * @date Jan 10, 2013
 */
public class PlaygroundEnvironment extends TestEnvironment {
private String baseUrl;

    public PlaygroundEnvironment() {
        baseUrl = System.getProperty(TestEnvironment.PROP_SBT_PLAYGROUND_URL);
        if (StringUtil.isEmpty(baseUrl)) {
            baseUrl = getProperty(TestEnvironment.PROP_SBT_PLAYGROUND_URL);
        }
        if (!baseUrl.endsWith("/")) baseUrl = baseUrl.concat("/");
    }
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.TestEnvironment#login()
     */
    @Override
    public boolean login() {
        // TODO Auto-generated method stub
        return false;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.TestEnvironment#computeLaunchUrl(com.ibm.sbt.automation.core.test.BaseTest)
     */
    @Override
    public String computeLaunchUrl(BaseTest baseTest) {
        String url = null;
        if (baseTest.getSnippetType() == SnippetType.JAVASCRIPT) {
            url = baseUrl + "JavaScriptSnippets.xsp#snippet=" + baseTest.getSnippetId() + "&jsLibId=" + jsLib;
        } else {
            url = baseUrl + "JavaSnippets.xsp#snippet=" + baseTest.getSnippetId() + "&jsLibId=" + jsLib;
            
            return null;//url = baseUrl + "/javaPreview.jsp?snippet=" + baseTest.getSnippetId() + "&jsLibId=" + jsLib;
        }
        
        url = addSnippetParams(baseTest, url);
        
        return url;
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.environment.TestEnvironment#getPageObject(org.openqa.selenium.WebDriver, java.lang.String)
     */
    @Override
    public ResultPage getPageObject(WebDriver webDriver) {
        ResultPage resultPage = (ResultPage)PageFactory.initElements(unwrapPage(webDriver), PlaygroundResultPage.class);
        resultPage.setWebDriver(unwrapPage(webDriver));
        return resultPage;
    }
    
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
	    return "Test environment for Playground";
	}


    public WebDriver unwrapPage(WebDriver webDriver) {
        try {
            WebElement we = webDriver.findElement(By.xpath("//iframe[@id='preview']"));
            webDriver = webDriver.switchTo().frame(we);
        } catch (Throwable e) {
            //logger.info(e.getMessage());
        }
        return webDriver;
    }

}
