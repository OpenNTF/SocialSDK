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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public interface ResultPage {

    /**
     * Return the page content as a string
     * 
     * @return
     */
    public String getText();
    
    /**
     * Return the page content as a web element
     * @return
     */
    public WebElement getWebElement();
    
    /**
     * Return the web driver
     * 
     * @return
     */
    public WebDriver getWebDriver();

    /**
     * Set the web driver
     * 
     * @param webDriver
     */
    public void setWebDriver(WebDriver webDriver);

}
