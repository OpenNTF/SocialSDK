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
package com.ibm.sbt.automation.core.test;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.AcmeSample.AcmeFlightsPage;
import com.ibm.sbt.automation.core.test.pageobjects.AcmeSample.AcmeMyFlightPage;
import com.ibm.sbt.automation.core.test.pageobjects.AcmeSample.AcmeNavigationPage;
import com.ibm.sbt.automation.core.test.pageobjects.AcmeSample.AcmeResultPage;

public class BaseAcmeTest extends BaseTest{
	
	//the navigation links
	AcmeNavigationPage navPage;
	
	public boolean testAcmeAirlinesSample(){
		
		//launch the sample
		AcmeResultPage result = launchAcmeAirlines("/index.html");
		
		//maximize the window - Because the nav links are not visible when the browser is not maximized
		result.getWebDriver().manage().window().maximize();
		
		//check the home page appears
		WebElement page = result.getMainContent();
		assertTrue(page.isDisplayed());
		
		boolean navigationIsOkay = checkNavigationLinks(result);	
		assertTrue(navigationIsOkay);
		
		boolean flightsPageIsOkay = checkFlightsPage(result);
		assertTrue(flightsPageIsOkay);
		
		boolean myFlightsPageIsOkay = checkMyFlightsPage(result);
		assertTrue(myFlightsPageIsOkay);
		
		return navigationIsOkay && flightsPageIsOkay && myFlightsPageIsOkay;
	}
	
	public boolean checkMyFlightsPage(AcmeResultPage resultPage){
		
		if(this.navPage == null){
			this.navPage = new AcmeNavigationPage(resultPage);
		}
		
		navPage.goToMyFlightsPage();
		AcmeMyFlightPage myFlights = new AcmeMyFlightPage(resultPage);
		
		return myFlights.checkFlight103IsBooked();
	}
	
	public boolean checkFlightsPage(AcmeResultPage resultPage){
		
		if(this.navPage == null){
			this.navPage = new AcmeNavigationPage(resultPage);
		}
		
		navPage.goToFlightsPage();
		AcmeFlightsPage flightsPage = new AcmeFlightsPage(resultPage);
		
		flightsPage.clickTheBookButton();
		
		return flightsPage.isFlightsTableDisplayed();
	}
	
	public boolean checkNavigationLinks(AcmeResultPage resultPage){
		
		if(this.navPage == null){
			this.navPage = new AcmeNavigationPage(resultPage);
		}

		navPage.goToFlightsPage();
		
		navPage.goToMyFlightsPage();
		
		navPage.goToFlightStatusPage();
		
		navPage.goToServicesPage();
		
		return true;
	}
	
	protected AcmeResultPage launchAcmeAirlines(String snippetId) {
		this.setAuthType(this.authType.AUTO_DETECT);
		AcmeResultPage resultPage = launchAcmeSample(snippetId);
        return resultPage;
    }

	protected AcmeResultPage launchAcmeSample(String snippetId) {
        ResultPage resultPage = super.launchSnippet(snippetId, authType);
        return wrapResultPage(resultPage);
    }
	
	protected AcmeResultPage wrapResultPage(ResultPage resultPage) {
        return new AcmeResultPage(resultPage);
    }
	
	@Override
	public String getAuthenticatedMatch() {
		// TODO Auto-generated method stub
		return "mainContainer";
	}
}
