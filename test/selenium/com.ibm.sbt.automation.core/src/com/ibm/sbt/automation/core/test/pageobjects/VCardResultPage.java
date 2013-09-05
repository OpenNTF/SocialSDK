package com.ibm.sbt.automation.core.test.pageobjects;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class VCardResultPage extends BaseResultPage{
	
	private ResultPage delegate;
	
	public VCardResultPage(ResultPage delegate) {
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
	 * Return the html span WebElement which must be there for a card to appear.
	 * @return the WebElement
	 */
	public WebElement getVCardSpan() {
		// We do a partial match here on the span id of the control. It corresponds across all vCard snippets.
		return getWebElement().findElement(By.cssSelector("span[id^='uniqName_']"));
	}
	
	/**
	 * Check that the vCard container is displayed on the page
	 * @return true if displayed
	 */
	public boolean isDisplayed() {
		return getVCardSpan().isDisplayed();
	}
	/**
	 * Get the html a WebElement which is clicked in the case of non-inline cards.
	 * 
	 * @return the WebElement
	 */
	public WebElement getCardAttachPoint(){
		return getVCardSpan().findElement(By.xpath(".//a"));
	}
	
	/**
	 * Get the html div WebElement which contains the VCards when they are rendered. This does not work for inline profile cards, use getInlineProfileCardDiv instead.
	 * 
	 * @return the WebElement
	 */
	public WebElement getCardDiv(){
		return getWebElement().findElement(By.id("cardDiv"));
	}
	
	/**
	 * Get the html ul representing the navigation options in the community card. 
	 * 
	 * @return the WebElement
	 */
	public WebElement waitForCommunityCardNav(){
		WebDriverWait wait = new WebDriverWait(getWebDriver(), 5);
		WebElement result = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("bizCardNav"))); // wait until the community card nav is available.
		
		return result;
	}
	
	public List<WebElement> getCardDivs(){
		return getWebElement().findElements(By.cssSelector("span[id^='uniqName_'] a"));
	}
	
	/**
	 * Returns the html div WebElement which contains the inline profile VCard.
	 * 
	 * @return the WebElement
	 */
	public WebElement getInlineProfileCardDiv(){
		return getVCardSpan().findElement(By.xpath(".//span//div"));
	}
	
	/**
	 * Check multiple profile cards for displayability.
	 * 
	 * @param cardAttachPoints An array containing the attach points of each card
	 * @return true if all cards could be displayed, false otherwise
	 */
	public boolean areProfileCardsDisplayable(List<WebElement> cardAttachPoints){
		for(WebElement cardAttachPoint: cardAttachPoints){
			if(!isProfileCardDisplayable(cardAttachPoint))
				return false;
		}
		
		return true;
	}
	
	/**
	 * This method hovers over the card's attach point and then clicks the hover menu which appears. This should bring up the VCard.
	 * 
	 * @return True if the ProfileCard appeared, false if not.
	 */
	public boolean isProfileCardDisplayable(WebElement cardAttachPoint){
		WebDriver driver = getWebDriver();
		
		new Actions(driver).moveToElement(cardAttachPoint).perform(); // hover over the attachpoint to make the semtagmenu appear.
		
		WebDriverWait wait = new WebDriverWait(driver, 2);
		WebElement semtagmenu = wait.until(ExpectedConditions.elementToBeClickable(By.id("semtagmenu"))); // wait until the hover menu is clickable.
		
		WebElement semTagHoverMenu = semtagmenu.findElement(By.xpath(".//a"));
		new Actions(driver).click(semTagHoverMenu).perform(); // click the hovering menu
		WebElement vCardDiv = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("cardDiv")));
		
		return vCardDiv.isDisplayed();
	}
}
