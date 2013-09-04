package com.ibm.sbt.automation.core.test;

import java.util.List;
import org.openqa.selenium.WebElement;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.VCardResultPage;

public class BaseVCardTest extends BaseTest{
	
	/**
	 * Check if the vCard is displayed on the page
	 * @param snippetId
	 * @return true if displayed
	 */
	protected boolean checkVCard(String snippetId) {
		VCardResultPage resultPage = launchVCard(snippetId);
		return resultPage.getVCardSpan().isDisplayed();
	}
	
	protected boolean checkCommunityVCard(String snippetId) {
		VCardResultPage resultPage = launchVCard(snippetId);
		
		boolean pageDisplayed = resultPage.getVCardSpan().isDisplayed(); // revert to checking the span for now.
		//boolean communityCardNavDisplayed = resultPage.waitForCommunityCardNav().isDisplayed();
		
		return pageDisplayed;// && communityCardNavDisplayed;
	}
	
	protected boolean checkProfileVCard(String snippetId) {
		VCardResultPage resultPage = launchVCard(snippetId);
		
		return checkProfileVCard(resultPage);
	}
	
	protected boolean checkProfileVCard(VCardResultPage resultPage) {
        boolean pageDisplayed = resultPage.isDisplayed();
        WebElement cardAttachPoint = resultPage.getCardAttachPoint();
        boolean profileCardDisplayable = resultPage.isProfileCardDisplayable(cardAttachPoint);
        
        return pageDisplayed && profileCardDisplayable;
    }
	
	protected boolean checkProfileVCards(String snippetId){
		VCardResultPage resultPage = launchVCard(snippetId);
		
		boolean pageDisplayed = resultPage.isDisplayed();
        List<WebElement> cardList = resultPage.getCardDivs();
		boolean profileCardsDisplayable = resultPage.areProfileCardsDisplayable(cardList);
		
		
		return pageDisplayed && profileCardsDisplayable;
	}
	
	protected boolean checkProfileVCardInline(String snippetId) {
		VCardResultPage resultPage = launchVCard(snippetId);
		
		boolean pageDisplayed = resultPage.isDisplayed();
		boolean profileInlineCardDisplayed = resultPage.getInlineProfileCardDiv().isDisplayed();
		
		return pageDisplayed && profileInlineCardDisplayed;
	}
	
	/**
	 * Launch the vCard snippet 
	 * @param snippetId
	 * @return the result page
	 */
	protected VCardResultPage launchVCard(String snippetId) {
        ResultPage resultPage = super.launchSnippet(snippetId, authType);
        return wrapResultPage(resultPage);
	}
	
	 /**
     * Wrap the environment result page in a VCardResultPage 
     * @param resultPage
     * @return the result page
     */	
	protected VCardResultPage wrapResultPage(ResultPage resultPage) {
		return new VCardResultPage(resultPage);
	}
	
    @Override
    protected boolean isEnvironmentValid() {
    	return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
}
