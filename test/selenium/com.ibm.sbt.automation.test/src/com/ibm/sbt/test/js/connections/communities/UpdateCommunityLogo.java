/*
 * � Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.connections.communities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;

/**
 * 
 * @author VineetKanwal
 * 
 **/
public class UpdateCommunityLogo extends BaseCommunitiesTest {
	File file;

	public UpdateCommunityLogo() {
		setAuthType(AuthType.AUTO_DETECT);
	}

	@Before
	public void init() {
		try {
			file = new File("JSCommunityLogoUpdate" + System.currentTimeMillis() + ".jpg");
			BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
			Graphics g = image.getGraphics();
			g.drawString("Hello World!!!", 10, 20);
			ImageIO.write(image, "jpg", file);
		} catch (FileNotFoundException e) {
			Assert.fail("Error creating test file: " + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Assert.fail("Error creating test file: " + e.getMessage());
			e.printStackTrace();
		}

	}

	@After
	public void destroy() {
		try {
			if (file != null) {
				file.delete();
			}
		} catch (Exception e) {
			Assert.fail("Error deleting test file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateCommunityLogo() {
		// Disabling for Dojo 1.4.3 which does not support FormData
		String jsLib = System.getProperty(TestEnvironment.PROP_JAVASCRIPT_LIB);
		if (StringUtil.isEmpty(jsLib)) {
			jsLib = environment.getProperty(TestEnvironment.PROP_JAVASCRIPT_LIB);
		}
		if ("dojo143".equalsIgnoreCase(jsLib)) {
			return;
		}
		UpdateCommunityLogoPage crudPage = launchSnippet();		
		WebElement webElement = waitForText("success", "Successfully loaded community ID", 20);
		String successMessage = webElement.getText();		
		Assert.assertTrue(successMessage.contains("Successfully loaded community ID"));
		boolean uploaded = crudPage.updateCommunityLogo();
		Assert.assertTrue("Unable to update community logo", uploaded);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition()
	 */
	@Override
	public String getAuthenticatedCondition() {
		return "idWithText";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch()
	 */
	@Override
	public String getAuthenticatedMatch() {
		return "uploadBtn";
	}

	// Internals

	private UpdateCommunityLogoPage launchSnippet() {
		ResultPage resultPage = launchSnippet("Social_Communities_Update_Community_Logo");

		return new UpdateCommunityLogoPage(resultPage);
	}

	/*
	 * Page object for the Connections_Communities_Create_Update_Delete_File snippet
	 */
	class UpdateCommunityLogoPage extends BaseResultPage {

		private ResultPage delegate;

		public UpdateCommunityLogoPage(ResultPage delegate) {
			this.delegate = delegate;

			setWebDriver(delegate.getWebDriver());
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText()
		 */
		@Override
		public String getText() {
			return delegate.getText();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement ()
		 */
		@Override
		public WebElement getWebElement() {
			return delegate.getWebElement();
		}

		public WebElement getSuccess() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("success"));
		}

		public WebElement getError() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("error"));
		}

		public WebElement getFileControl() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("your-files"));
		}

		public WebElement getUpdateBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("updateBtn"));
		}

		public void setFile() {
			WebElement fileCotrol = getFileControl();
			fileCotrol.sendKeys(file.getAbsolutePath());
		}

		public void clickUpdate() {
			getUpdateBtn().click();
		}

		/**
		 * Update the current file and return the true if successful and otherwise return false
		 */
		public boolean updateCommunityLogo() {
			setFile();
			clickUpdate();
			WebElement webElement = waitForText("success", "Community Logo updated successfuly", 50);

			String text = webElement.getText();

			boolean result = text.startsWith("Community Logo updated successfuly");

			return result;
		}

	}

}
