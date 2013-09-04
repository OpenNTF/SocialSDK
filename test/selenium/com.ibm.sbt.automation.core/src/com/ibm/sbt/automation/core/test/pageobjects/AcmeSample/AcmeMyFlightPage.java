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

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AcmeMyFlightPage {
	
	private AcmeResultPage myFlightsPage;
	
	//page elements
	private WebElement myFlightsTable;
	
	
	//ids/xPath to retrieve elements
	private final String myFlightsTableId = "myFlightsTable";
	
	public AcmeMyFlightPage(AcmeResultPage resultPage){
		
		this.myFlightsPage = resultPage;
		
		myFlightsTable = this.myFlightsPage.getWebElement().findElement(By.id(myFlightsTableId));
	}
	
	public boolean checkFlight103IsBooked(){
		
		boolean flight103IsBooked = false;
		
		List<WebElement> flights = getMyFlights();
		
		for(int i=0;i<flights.size();i++){
			if(flights.get(i).getText().contains("103")){
				flight103IsBooked = true;
			}
		}

		return flight103IsBooked;
	}
	
	public List<WebElement> getMyFlights(){
		List<WebElement> flights = myFlightsTable.findElements(By.tagName("tr"));
		return flights;
	}

}
