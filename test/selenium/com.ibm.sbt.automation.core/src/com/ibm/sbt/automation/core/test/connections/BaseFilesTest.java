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
package com.ibm.sbt.automation.core.test.connections;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.client.connections.files.model.FileCreationParameters;


/** @author mwallace
 * 
 * @date 13 Mar 2013 */
public class BaseFilesTest extends BaseApiTest {

	protected FileService fileService;
	protected File fileEntry;
	protected File folder;
	private boolean failIfAfterDeletionFails = true;

	private final String[] ErrorMessages = { "Error received. Error Code", "Error, unable to load:",
			"Caused by: com.ibm.sbt.services.client.ClientServicesException", "Exception occurred", "Caused by:", "HTTP Status 500", "HTTP Status 404" };
	private String noErrorMsg = null;

	public BaseFilesTest() {
		super();
		setAuthType(AuthType.AUTO_DETECT);
	}

	protected boolean failIfAfterDeletionFails() {
		return failIfAfterDeletionFails;
	}

	protected void setFailIfAfterDeletionFails(boolean failIfAfterDeletionFails) {
		this.failIfAfterDeletionFails = failIfAfterDeletionFails;
	}

	protected FileService getFileService() {
		try {
			loginConnections();
		} catch (AuthenticationException e) {
			Assert.fail("Error logging in to Connections " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		setFailIfAfterDeletionFails(true);

		if (fileService == null) {
			fileService = new FileService(getEndpointName());
		}
		return fileService;
	}
	
	public void createFolder() {
		setFailIfAfterDeletionFails(true);
		fileService = getFileService();
		try {
			folder = fileService.createFolder("TestFolder");		
			Trace.log("Created test folder: " + folder.getFileId());			
		} catch (FileServiceException e) {
			e.printStackTrace();
			Assert.fail("Error creating test folder: " + e.getMessage());
		} catch (TransformerException te) {
			te.printStackTrace();
			Assert.fail("Error creating test folder: " + te.getMessage());
		}
		
	}

	public void createFile() {

		try {
			setFailIfAfterDeletionFails(true);
			fileService = getFileService();
			String content = "Content uploaded by Create Delete File java sample";
			String id = "File" + System.currentTimeMillis() + ".txt";

			
			FileCreationParameters p = new FileCreationParameters();
			p.visibility = FileCreationParameters.Visibility.PUBLIC;
			p.tags = new ArrayList<String>();
			p.tags.add("text");
			Map<String, String> params = p.buildParameters();			
			
			fileEntry = fileService.uploadFile(new ByteArrayInputStream(content.getBytes()), id, content.length(), params);

			params = new HashMap<String, String>();
			fileService.addCommentToFile(fileEntry.getFileId(), "Comment added by BaseFilesTest", params);

			Trace.log("Created test file: " + fileEntry.getFileId());
		} catch (FileServiceException fse) {
			fileEntry = null;
	        fse.printStackTrace();
			Assert.fail("Error creating test file: " + fse.getMessage());
		} catch (TransformerException te) {
			te.printStackTrace();
			Assert.fail("Error creating test file: " + te.getMessage());
		}
	}

	public void deleteFileAndQuit() {
		if (fileEntry != null) {
			try {
				fileService.deleteFile(fileEntry.getFileId());
			} catch (FileServiceException fse) {
				fileEntry = null;
				if (failIfAfterDeletionFails()) {
					Assert.fail("Error deleting test file: " + fse.getMessage());
					fse.printStackTrace();
				}
			}
		}
		if (folder != null) {
			try {
				fileService.deleteFolder(folder.getFileId());
			} catch (FileServiceException fse) {
				folder = null;
				if (failIfAfterDeletionFails()) {
					Assert.fail("Error deleting test folder: " + fse.getMessage());
					fse.printStackTrace();
				}
			}
		}

	}

	/** @return */
	protected boolean checkNoError(String snippetId) {
		String text = executeSnippet1(snippetId);
		return containsNoError(text);
	}

	/** @param snippetId
	 * @return */
	protected String executeSnippet1(String snippetId) {
		ResultPage resultPage = launchSnippet(snippetId, authType);
		String text = resultPage.getText();

		// dumpResultPage(resultPage);

		if (text != null && text.startsWith("Show Snippet Code")) {
			text = text.substring("Show Snippet Code".length());
		}
		return (text == null) ? null : text.trim();
	}

	/*
	 * Return true if the result page contains no error message. */
	protected boolean containsNoError(String result) {
		boolean retVal = true;	
		if (StringUtil.isEmpty(result)) {
			noErrorMsg  = "Empty result was returned for: " + getSnippetId();
			retVal = false;
		} else {
			for (int i = 0; i < ErrorMessages.length; i++) {
				if (result.contains(ErrorMessages[i])) {
					noErrorMsg = "Error message for: " + getSnippetId() + ": " + result;
					retVal = false;
				}
			}
		}
		if (!retVal) {
			Trace.log(noErrorMsg);
		}
		return retVal;
	}
	
	/**
     * @return the noErrorMsg
     */
    public String getNoErrorMsg() {
        return noErrorMsg;
    }

}
