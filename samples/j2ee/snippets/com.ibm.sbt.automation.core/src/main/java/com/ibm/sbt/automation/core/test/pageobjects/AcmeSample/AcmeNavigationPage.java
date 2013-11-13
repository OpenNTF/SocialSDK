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

public class AcmeNavigationPage {
	
	//The Page
	private AcmeResultPage navPage;
	
	//Navigation Links
	private WebElement navBar;
	private WebElement homePage;
	private WebElement flightsPageLink;
	private WebElement myFlightsPageLink;
	private WebElement flightStatusPageLink;
	private WebElement checkInAreaPageLink;
	private WebElement servicesPageLink;
	
	
	//Xpath/ids to find the 
	private final String navBarId = "navBar";
	private final String homeLink = "ul/li";
	private final String flightsLink = "ul/li[2]";
	private final String myFlightsLink = "ul/li[3]";
	private final String flightStatusLink = "ul/li[4]";
	private final String checkinLink = "ul/li[5]";
	private final String servicesLink = "ul/li[6]";
	
	public AcmeNavigationPage(AcmeResultPage resultPage){
		
		this.navPage = resultPage;
		
		this.navBar = navPage.getWebElement().findElement(By.id(navBarId));
		this.homePage = navBar.findElement(By.xpath(homeLink));
		this.flightsPageLink = navBar.findElement(By.xpath(flightsLink));
		this.myFlightsPageLink = navBar.findElement(By.xpath(myFlightsLink));
		this.flightStatusPageLink = navBar.findElement(By.xpath(flightStatusLink));
		this.checkInAreaPageLink = navBar.findElement(By.xpath(checkinLink));
		this.servicesPageLink = navBar.findElement(By.xpath(servicesLink));
	}
	
	public void goToHomePage(){
		this.homePage.click();
	}
	
	public void goToFlightsPage(){
		this.flightsPageLink.click();
	}
	
	public void goToMyFlightsPage(){
		this.myFlightsPageLink.click();
	}
	
	public void goToFlightStatusPage(){
		this.flightStatusPageLink.click();
	}
	
	public void goToCheckinPage(){
		this.checkInAreaPageLink.click();
	}
	
	public void goToServicesPage(){
		this.servicesPageLink.click();
	}
}
