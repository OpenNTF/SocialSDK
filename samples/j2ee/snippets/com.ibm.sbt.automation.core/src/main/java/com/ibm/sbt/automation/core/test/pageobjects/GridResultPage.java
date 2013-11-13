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
package com.ibm.sbt.automation.core.test.pageobjects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author mwallace
 *  
 * @date 5 Mar 2013
 */
public class GridResultPage extends BaseResultPage {
    
    private ResultPage delegate;
    /* The div containing the grid on this page */
    
    //public String GridContainerCSS = "div[id^='sbt_controls_grid_Grid_']";
    public String gridId = "gridDiv";
   
    public GridResultPage(ResultPage delegate) {
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
    	//return getWebElement().findElement(By.cssSelector(GridContainerCSS));
    	return delegate.getWebElement().findElement(By.id(gridId));
    }
    
    /**
     * Return the table WebElement for the Grid that was created on this page
     * 
     * @return
     */
    public WebElement getTable() {
        WebElement resultEl = getWebElement();
        return resultEl.findElement(By.tagName("table"));
    }
    
    /**
     * Return the tbody WebElement for the Grid that was created on this page
     * 
     * @return
     */
    public WebElement getTableBody() {
        WebElement resultEl = getWebElement();
        return resultEl.findElement(By.tagName("tbody"));
    }
    
    /**
     * Return a list of tr WebElement for the Grid that was created on this page
     * 
     * @return
     */
    public List<WebElement> getTableRows() {
        WebElement resultEl = getWebElement();
        return resultEl.findElements(By.tagName("tr"));
    }
    
    /**
     * Return a WebElement for the pager div that was created on this page
     * 
     * @return
     */
    public GridPagerPage getGridPager() {
        return new GridPagerPage(this);
    }
    
    /**
     * Return a WebElement for the sort div that was created on this page
     * 
     * @return
     */
    public GridSorterPage getSortingPager() {
        return new GridSorterPage(this);
    }

}
