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

package com.ibm.sbt.automation.core.test.pageobjects.AcmeSample;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AcmeFlightsPage {
	
	private AcmeResultPage flightsPage;
	
	//elements on the page
	private WebElement flightsTable;
	private WebElement bookFlightButton;
	
	//xpath/ids to get the elements
	private final String flightTableId = "flightsTable";
	private final String bookButtonXpath = "tbody/tr[3]/td[6]/button";            //the xPath for the book button for flight 103
	
	
	public AcmeFlightsPage(AcmeResultPage resultPage){
		
		this.flightsPage = resultPage;
		this.flightsTable = flightsPage.getWebElement().findElement(By.id(flightTableId));
		
		this.bookFlightButton = this.flightsTable.findElement(By.xpath(bookButtonXpath));
	}

	public void clickTheBookButton(){
		bookFlightButton.click();
	}
	
	public boolean isFlightsTableDisplayed(){
		return flightsTable.isDisplayed();
	}
}
