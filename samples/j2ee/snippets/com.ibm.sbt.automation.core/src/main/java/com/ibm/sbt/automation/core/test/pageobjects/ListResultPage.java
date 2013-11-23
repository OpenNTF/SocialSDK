/**
 * 
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
     * @return
     */
    public WebElement getList() {
        WebElement resultEl = getWebElement();
        return resultEl.findElement(By.tagName("ul"));
    }
    
    /**
     * Return a list of li WebElement for the Grid that was created on this page
     * 
     * @return
     */
    public List<WebElement> getListItems() {
        WebElement resultEl = getWebElement();
        return resultEl.findElements(By.tagName("li"));
    }
    	
}
