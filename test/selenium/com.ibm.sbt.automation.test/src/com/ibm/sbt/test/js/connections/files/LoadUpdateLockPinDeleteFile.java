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

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.BaseResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;

/**
 * 
 * @author VineetKanwal
 *
 */
public class LoadUpdateLockPinDeleteFile extends BaseFilesTest {
	
	@Before
	public void init() {
		createFile();
		addSnippetParam("sample.fileId", fileEntry.getFileId());
	}

	@Test
	public void testLoadUpdateLockPinDeleteFile() {
		LoadUpdateLockPinDeleteFilePage crudPage = launchSnippet();
		String uuid = crudPage.getLoadedFileId();
		Assert.assertNotNull("Unable to load file", uuid);
		boolean updated = crudPage.updateFile();
		Assert.assertTrue("Unable to update a file", updated);
		boolean lockedOrUnLocked = crudPage.lockUnlockFile();
		Assert.assertTrue("Unable to lock/unlock a file", lockedOrUnLocked);
		boolean pinOrUnpin = crudPage.pinUnpinFile();
		Assert.assertTrue("Unable to pin/unpin a file", pinOrUnpin);
		boolean deleted = crudPage.deleteFile();
		Assert.assertTrue("Unable to delete a file", deleted);
		fileEntry = null;
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
		return "fileId";
	}

	// Internals

	private LoadUpdateLockPinDeleteFilePage launchSnippet() {
		ResultPage resultPage = launchSnippet("Social_Files_Load_Update_Lock_Pin_Delete_File");

		waitForText("success", "Successfully loaded file:", 20);

		return new LoadUpdateLockPinDeleteFilePage(resultPage);
	}

	/* Page object for the Social_Communities_Create_Update_Delete_File snippet */
	class LoadUpdateLockPinDeleteFilePage extends BaseResultPage {

		private ResultPage delegate;

		public LoadUpdateLockPinDeleteFilePage(ResultPage delegate) {
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

		public WebElement getFileLabel() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("label"));
		}

		public WebElement getFileSummary() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("summary"));
		}

		public WebElement getFileVisibility() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("visibility"));
		}

		public WebElement getLoadBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("loadBtn"));
		}

		public WebElement getUpdateBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("updateBtn"));
		}

		public WebElement getDeleteBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("delete"));
		}

		public WebElement getLockUnlockBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("lockUnlock"));
		}

		public WebElement getPinUnpinBtn() {
			WebElement resultEl = getWebElement();
			return resultEl.findElement(By.id("pinUnPin"));
		}

		public void setFileLabel(String label) {
			WebElement fileLabel = getFileLabel();
			fileLabel.clear();
			fileLabel.sendKeys(label);
		}

		public void setFileSummary(String summary) {
			WebElement fileSummary = getFileSummary();
			fileSummary.clear();
			fileSummary.sendKeys(summary);
		}

		public void setFileVisibility(String visibility) {
			WebElement fileVisibility = getFileVisibility();
			fileVisibility.clear();
			fileVisibility.sendKeys(visibility);
		}

		public void clickLockUnlock() {
			getLockUnlockBtn().click();
		}

		public void clickPinUnpin() {
			getPinUnpinBtn().click();
		}

		public void clickUpdate() {
			getUpdateBtn().click();
		}

		public void clickDelete() {
			getDeleteBtn().click();
		}

		/** Update the current file and return the true if successful and otherwise return false */
		public boolean updateFile() {
			String label = "Updated Test Automation File " + System.currentTimeMillis();
			String summary = "The file was updated using the LoadUpdateLockPinDeleteFile unit test";
			String visibility = "public";
			return updateFile(label, summary, visibility);
		}

		/** Update the current file and return the true if successful and otherwise return false */
		public boolean updateFile(String label, String summary, String visibility) {
			setFileLabel(label);
			setFileSummary(summary);
			setFileVisibility(visibility);

			clickUpdate();

			WebElement webElement = waitForText("success", "Successfully updated file:", 20);
			if(webElement == null){
				webElement = getError();
				String error = webElement.getText();
				Trace.log("Error updating File with ID " + fileEntry.getFileId() + " : " + error);
				return false;
			}

			String text = webElement.getText();

			return text.startsWith("Successfully updated file:");
		}
		
		/** Lock/Unlock the current file and return the true if successful and otherwise return false */
		public boolean lockUnlockFile() {
			clickLockUnlock();

			WebElement webElement = waitForText("success", "Successfully", 20);

			if(webElement == null){
				webElement = getError();
				String error = webElement.getText();
				Trace.log("Error Locking/Unlocking File with ID " + fileEntry.getFileId() + " : " + error);
				return false;
			}
			
			String text = webElement.getText();

			return text.startsWith("Successfully");
		}
		
		/** Pin/Unpin the current file and return the true if successful and otherwise return false */
		public boolean pinUnpinFile() {
			clickPinUnpin();

			WebElement webElement = waitForText("success", "Successfully", 20);
			if(webElement == null){
				webElement = getError();
				String error = webElement.getText();
				Trace.log("Error pinning/unpinning File with ID " + fileEntry.getFileId() + " : " + error);
				return false;
			}

			String text = webElement.getText();

			return text.startsWith("Successfully");
		}


		/** Delete the current file and return the true if successful and otherwise return false */
		public boolean deleteFile() {			
			clickDelete();
			WebElement webElement = waitForText("success", "Deleted file:", 20);
			if(webElement == null){				
				webElement = getError();
				String error = webElement.getText();
				if(error.contains("ItemNotFound")) {
					Trace.log("File with ID " + fileEntry.getFileId() + " not found, seems already deleted!");
					return true;
				}
				else {
					Trace.log("Error deleting File with ID " + fileEntry.getFileId() + " : " + error);
					return false;
				}
			}

			String text = webElement.getText();

			return text.startsWith("Deleted file:");
		}

		/** Return the file id of the file that was last loaded */
		public String getLoadedFileId() {
			WebElement webElement = waitForText("success", "Successfully loaded file:", 20);
			
			if(webElement == null){
				webElement = getError();
				String error = webElement.getText();
				Trace.log("Error loading File with ID " + fileEntry.getFileId() + " : " + error);
				return null;
			}

			String text = webElement.getText();

			if (text.startsWith("Successfully loaded file:")) {
				return text.substring("Successfully loaded file: ".length());
			} else {
				return null;
			}
		}
	}

}
