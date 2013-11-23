package com.ibm.sbt.automation.core.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class WrapperResultPage extends BaseResultPage{
	
	private ResultPage delegate;
	
	public WrapperResultPage(ResultPage delegate) {
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
	 * Return the WebElement containing the ActivityStream's iframe on this page
	 * @return the WebElement
	 */
	public WebElement getActivityStreamFrame() {
		return getFrame(By.cssSelector("iframe[id^='uniqName_']"));
	}
	
	public WebElement getFileGridFrame(){
	    return getFrame(By.cssSelector("iframe[id^='uniqName_']"));
	}
	
	public WebElement getProfileCardFrame(){
	    return getFrame(By.cssSelector("iframe[id^='uniqName_']"));
	}
	
	/**
	 * Get the frame that this page wraps.
	 * @param by
	 * @return
	 */
	public WebElement getFrame(By by){
	    return getWebElement().findElement(by);
	}
	
	/**
	 * Return the WebElement containing the ActivityStream on this page, switch to the iframe context first.
	 * @return the WebElement
	 */
	public WebElement getActivityStream() {
		return getWebElement().findElement(By.cssSelector("#activityStream"));
	}
	
	/**
	 * Return the ul WebElement containing the list of news items on the page, switch to the iframe context first.
	 * @return
	 */
	public WebElement getNewsFeedNode() {
		return getWebElement().findElement(By.cssSelector("[dojoattachpoint=\"newsFeedNode\"]"));
	}
	
	/**
	 * Check that the ActivityStream container is displayed on the page
	 * @return true if displayed
	 */
	public boolean isDisplayed() {
		return getActivityStream().isDisplayed();
	}
}
