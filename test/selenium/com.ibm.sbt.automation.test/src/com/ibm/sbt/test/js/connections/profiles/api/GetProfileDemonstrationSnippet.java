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
import com.ibm.sbt.services.client.connections.profiles.Profile;

public class GetProfileDemonstrationSnippet extends BaseProfilesTest {

	public GetProfileDemonstrationSnippet() {
		setAuthType(AuthType.AUTO_DETECT);
	}

	@Test
	public void testGetProfile() {

		Profile profile = getProfile(getProperty("sample.id1"));
		GetProfilePage getProfilePage = launchSnippet();
		String profileName = getProfilePage.getProfileName();
		Assert.assertNotNull("Unable to load profile", profileName);
		Assert.assertEquals("Frank Adams", profileName);
		Assert.assertEquals(profile.getUserid(), getProfilePage.getProfileId());
		Assert.assertEquals(profile.getJobTitle(), getProfilePage.getProfileJobTitle());
	}

	private GetProfilePage launchSnippet() {
		ResultPage resultPage = launchSnippet("Social_Profiles_Get_Profile");
		waitForText("name", "Frank Adams", 20);
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

		public String getProfileId() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("userId")).getText();
		}
		
		public String getProfileJobTitle() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("jobTitle")).getText();
		}

		/**
		 * Return the profile name of the profile that was loaded
		 */
		public String getProfileName() {
			WebElement webElement = waitForText("name", "Frank Adams", 20);
			return (webElement == null) ? null : webElement.getText();
		}
	}

}
