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
		createContext();
		addSnippetParam("sample.id1", environment.getCurrentUserUuid());
		addSnippetParam("sample.displayName1", environment.getCurrentUserDisplayName()); 
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
