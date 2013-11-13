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
package com.ibm.sbt.automation.core.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.StringUtil;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class JavaScriptTesterPage extends BaseResultPage {
    
    private ResultPage delegate;
    
    public JavaScriptTesterPage(ResultPage delegate) {
        this.delegate = delegate;
        
        setWebDriver(delegate.getWebDriver());
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText()
     */
    @Override
    public String getText() {
        return delegate.getText();
    }

    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement()
     */
    @Override
    public WebElement getWebElement() {
        return delegate.getWebElement();
    }
    
    /**
     * Return the contents of the result div
     */
    public String getResult() {
        WebElement webElement = getWebElement();
        return webElement.findElement(By.id("result")).getText();
    }
    
    public boolean isPass() {
        String result = getResult();
        return result.startsWith("Pass");
    }
    
    public boolean isFail() {
        String result = getResult();
        return result.startsWith("Fail");
    }

    public String getFailReason() {
        String result = getResult();
        if (result.startsWith("Fail: ")) {
            return result.substring("Fail: ".length());
        }
        return null;
    }

    /**
     * Return the contents of the result div
     */
    public String getTrace() {
        WebElement webElement = getWebElement();
        String text = webElement.findElement(By.id("trace")).getText();
        return StringUtil.isEmpty(text) ? "Page contained no trace." : text;
    }

    /**
     * Return the contents of the result div
     */
    public String getError() {
        WebElement webElement = getWebElement();
        String text = webElement.findElement(By.id("error")).getText();
        return StringUtil.isEmpty(text) ? "Page contained no error." : text;
    }

}
