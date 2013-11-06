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
package com.ibm.sbt.test.js.connections.profiles.api;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.connections.BaseProfilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;


public class UpdateProfileDemonstrationSnippet extends BaseProfilesTest {

	public UpdateProfileDemonstrationSnippet() {
		setAuthType(AuthType.AUTO_DETECT);
	}

	@Test
	public void testUpdateProfile() {
		// Profile profile = getProfile(getProperty("sample.id1"));
		GetProfilePage getProfilePage = launchSnippet();
		String profileLoadMessage = getProfilePage.getProfileMessage();
		Assert.assertNotNull("Unable to load profile", profileLoadMessage);
		Assert.assertEquals(
				"Successfully loaded profile entry for Frank Adams",
				profileLoadMessage);
		String profileUpdateMessage = getProfilePage.updateProfile();
		Assert.assertNotNull("Unable to load profile", profileLoadMessage);
		Assert.assertEquals(
				"Successfully updated profile entry for Frank Adams",
				profileUpdateMessage);

	}
	
	
	private GetProfilePage launchSnippet() {
		ResultPage resultPage = launchSnippet("Social_Profiles_Update_Profile");
		waitForText("success", "Successfully loaded profile entry for ", 20);
		return new GetProfilePage(resultPage);
	}

	/*
	 * Page object for the Social_Profiles_Get_Profile
	 */
	class GetProfilePage extends BaseResultPage {

		private ResultPage delegate;

		public GetProfilePage(ResultPage delegate) {
			this.delegate = delegate;
			setWebDriver(delegate.getWebDriver());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText()
		 */
		@Override
		public String getText() {
			return delegate.getText();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement
		 * ()
		 */
		@Override
		public WebElement getWebElement() {
			return delegate.getWebElement();
		}

		/**
		 * Return the text of the success element
		 */
		public String getProfileMessage() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("success")).getText();
		}

		public WebElement getProfileFloor() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("floor"));
		}

		public WebElement getProfileBuilding() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("building"));
		}

		public WebElement getProfileJobTitle() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("jobTitle"));
		}

		public WebElement getLoadBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("loadBtn"));
		}

		public WebElement getUpdateBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("updateBtn"));
		}

		public void setProfileBuilding(String building) {
			WebElement profileBuilding = getProfileBuilding();
			profileBuilding.clear();
			profileBuilding.sendKeys(building);
		}

		public void setProfileFloor(String floor) {
			WebElement profileFloor = getProfileFloor();
			profileFloor.clear();
			profileFloor.sendKeys(floor);
		}

		public void setProfileJobTitle(String jobTitle) {
			WebElement profileJobTitle = getProfileJobTitle();
			profileJobTitle.clear();
			profileJobTitle.sendKeys(jobTitle);
		}
		
		public void clickUpdate() {
			getUpdateBtn().click();
		}

		
		/**
		 * Create a new community and return the uuid if successful and
		 * otherwise return null
		 */
		public String updateProfile() {
			String building = "Profile Building" ;					
			String floor = "Profile floor";
			String jobtTitle = "Test job title";
			return updateProfile(building, floor, jobtTitle);
		}

		/**
		 * Create a new community and return the uuid if successful and
		 * otherwise return null
		 */
		public String updateProfile(String building, String floor, String jobTitle) {
			setProfileBuilding(building);
			setProfileFloor(floor);
			setProfileJobTitle(jobTitle);

			clickUpdate();

			return getUpdateProfileMessage();
		}
		
		public String getUpdateProfileMessage(){
			waitForText("success", "Successfully updated profile entry for ", 20);
			return getProfileMessage();
		}
	}

}
