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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author mwallace
 *
 */
public class ListResultPage extends BaseResultPage {

    private ResultPage delegate;
    
    public String gridId = "gridDiv";
   
    public ListResultPage(ResultPage delegate) {
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
     * Return the WebElement for the grid container on this page
     * @return the container
     */
    public WebElement getGridContainer() {
    	return delegate.getWebElement().findElement(By.id(gridId));
    }
    
    /**
     * Return the ul WebElement for the Grid that was created on this page
     * 
     * @return {WebElement}
     */
    public WebElement getList() {
        WebElement resultEl = getWebElement();
        return resultEl.findElement(By.tagName("ul"));
    }
    
    /**
     * Return a list of li WebElement for the Grid that was created on this page
     * 
     * @return {List<WebElement>}
     */
    public List<WebElement> getListItems() {
        WebElement resultEl = getWebElement();
        return resultEl.findElements(By.tagName("li"));
    }
    	
}
