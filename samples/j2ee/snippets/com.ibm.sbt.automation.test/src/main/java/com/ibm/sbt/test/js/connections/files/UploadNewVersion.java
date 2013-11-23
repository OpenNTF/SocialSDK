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
package com.ibm.sbt.test.js.connections.files;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.environment.TestEnvironment;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;

/** 
 * 
 * @author VineetKanwal 
 * 
 **/
public class UploadNewVersion extends BaseFilesTest {
	File file;
	String fileId;

	@Before
	public void init() {
		try {
			createFile();
			addSnippetParam("sample.uploadNewVersionFileId", fileEntry.getFileId());
			file = new File("JSUpdateNewVersionTest " + System.currentTimeMillis() + ".txt");
			file.createNewFile();
			Trace.log("Created test file: " + file.getAbsolutePath());
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write("JS Update Version File Test File");
			writer.flush();
			writer.close();
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
			deleteFileAndQuit();
			if (file != null) {
				file.delete();
			}
		} catch (Exception e) {
			Assert.fail("Error deleting test file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	protected boolean isEnvironmentValid() {
		// Disabling for Dojo 1.4.3 which does not support FormData
		if (environment.isLibrary("dojo") && !environment.isLibraryVersionGreatherThan("160")) return false;
		//disabling for smartcloud
		if (environment.isSmartCloud()) return false;
		return super.isEnvironmentValid() ;
	}
	
	@Test
	public void testUpdateFile() {
		UpdateFilePage crudPage = launchSnippet();
		boolean updated = crudPage.uploadNewVersion();
		Assert.assertTrue("Unable to update the file", updated);		
	}

	/* (non-Javadoc)
	 * 
	 * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedCondition() */
	@Override
	public String getAuthenticatedCondition() {
		return "idWithText";
	}

	/* (non-Javadoc)
	 * 
	 * @see com.ibm.sbt.automation.core.test.BaseTest#getAuthenticatedMatch() */
	@Override
	public String getAuthenticatedMatch() {
		return "updateBtn";
	}

	// Internals

	private UpdateFilePage launchSnippet() {
		ResultPage resultPage = launchSnippet("Social_Files_Upload_New_Version");

		return new UpdateFilePage(resultPage);
	}

	/* Page object for the Social_Communities_Create_Update_Delete_File snippet */
	class UpdateFilePage extends BaseResultPage {

		private ResultPage delegate;

		public UpdateFilePage(ResultPage delegate) {
			this.delegate = delegate;

			setWebDriver(delegate.getWebDriver());
		}

		/* (non-Javadoc)
		 * 
		 * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getText() */
		@Override
		public String getText() {
			return delegate.getText();
		}

		/* (non-Javadoc)
		 * 
		 * @see com.ibm.sbt.automation.core.test.pageobjects.ResultPage#getWebElement () */
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

		public WebElement getFileId() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("fileId"));
		}

		public WebElement getFileControl() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("your-files"));
		}

		public WebElement getUploadBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("uploadBtn"));
		}

		public void setFile() {
			WebElement fileCotrol = getFileControl();
			fileCotrol.sendKeys(file.getAbsolutePath());
		}

		public void clickUpload() {
			getUploadBtn().click();
		}

		/** Update the current file and return the true if successful and otherwise return false */
		public boolean uploadNewVersion() {
			setFile();
			clickUpload();
			WebElement webElement = waitForText("success", "File with ID", 50);

			String text = webElement.getText();

			boolean result = text.startsWith("File with ID");			

			return result;
		}

	}

}
