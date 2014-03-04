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

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.transformers.TransformerException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileService;
import com.ibm.sbt.services.client.connections.files.FileServiceException;
import com.ibm.sbt.services.client.connections.files.model.FileCreationParameters;

/**
 * @author mwallace
 * 
 * @date 13 Mar 2013
 */
public class BaseFilesTest extends BaseApiTest {

	protected FileService fileService;
	protected File fileEntry;
	protected File folder;
	protected CommunityService communityService;
	protected Community community;
	private boolean failIfAfterDeletionFails = true;

	private final String[] ErrorMessages = { "Error received. Error Code",
			"Error, unable to load:",
			"Caused by: com.ibm.sbt.services.client.ClientServicesException",
			"Exception occurred", "Caused by:", "HTTP Status 500",
			"HTTP Status 404" };
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

	protected CommunityService getCommunityService() {
		if (communityService == null) {
			communityService = new CommunityService(getEndpointName());
		}
		return communityService;
	}

	protected FileService getFileService() {
		try {
			loginConnections();
		} catch (AuthenticationException e) {
			fail("Error logging in to Connections ", e);
			e.printStackTrace();
			return null;
		}
		setFailIfAfterDeletionFails(true);

		if (fileService == null) {
			fileService = new FileService(getEndpointName());
		}
		return fileService;
	}

	public void createCommunity() {

		String type = "public";
		if (environment.isSmartCloud()) {
			type = "private";
		}
		String name = createCommunityName();
		// System.out.println(name);
		community = createCommunity(name, type, name, "tag1,tag2,tag3");

	}

	protected String createCommunityName() {
		return this.getClass().getName() + "#" + this.hashCode()
				+ " Community - " + System.currentTimeMillis();
	}

	protected Community createCommunity(String title, String type,
			String content, String tags) {
		return createCommunity(title, type, content, tags, true);
	}

	protected Community createCommunity(String title, String type,
			String content, String tags, boolean retry) {
		Community community = null;
		try {
			loginConnections();
			CommunityService communityService = getCommunityService();

			long start = System.currentTimeMillis();
			community = new Community(communityService, "");
			community.setTitle(title);
			community.setCommunityType(type);
			community.setContent(content);
			community.setTags(tags);
			String communityUuid = communityService.createCommunity(community);
			community = communityService.getCommunity(communityUuid);

			long duration = System.currentTimeMillis() - start;
			Trace.log("Created test community: " + communityUuid + " took "
					+ duration + "(ms)");
		} catch (AuthenticationException pe) {
			if (pe.getCause() != null) {
				pe.getCause().printStackTrace();
			}
			fail("Error authenicating: ", pe);
		} catch (CommunityServiceException cse) {
			// TODO remove this when we upgrade the QSI
			Throwable t = cse.getCause();
			if (t instanceof ClientServicesException) {
				ClientServicesException csex = (ClientServicesException) t;
				int statusCode = csex.getResponseStatusCode();
				if (statusCode == 500 && retry) {
					return createCommunity(title + " (retry)", type, content,
							tags, false);
				}
			}
			fail("Error creating test community with title: '" + title + "'",
					cse);
		}

		return community;
	}

	protected void fail(String message, Exception cse) {
		String failure = message;

		Throwable cause = cse.getCause();
		if (cause != null) {
			cause.printStackTrace();
			failure += ", " + cause.getMessage();
		} else {
			cse.printStackTrace();
			failure += ", " + cse.getMessage();
		}

		Assert.fail(failure);
	}

	public void createFolder() {
		setFailIfAfterDeletionFails(true);
		fileService = getFileService();
		try {
			folder = fileService.createFolder("TestFolder");
			Trace.log("Created test folder: " + folder.getFileId());
		} catch (FileServiceException e) {
			e.printStackTrace();
			fail("Error creating test folder: ", e);
		} catch (TransformerException te) {
			te.printStackTrace();
			fail("Error creating test folder: ", te);
		}

	}

	public void createFile() {
		String id = "File" + System.currentTimeMillis() + ".txt";
		createFile(id);
	}

	public void createFile(String id) {
		try {
			setFailIfAfterDeletionFails(true);
			fileService = getFileService();
			String content = "Content uploaded by Create Delete File java sample";

			FileCreationParameters p = new FileCreationParameters();
			p.visibility = FileCreationParameters.Visibility.PUBLIC;
			p.tags = new ArrayList<String>();
			p.tags.add("text");
			Map<String, String> params = p.buildParameters();

			fileEntry = fileService.uploadFile(
					new ByteArrayInputStream(content.getBytes()), id,
					content.length(), params);

			params = new HashMap<String, String>();
			fileService.addCommentToFile(fileEntry.getFileId(),
					"Comment added by BaseFilesTest", params);

			Trace.log("Created test file: " + fileEntry.getFileId());
		} catch (FileServiceException fse) {
			fileEntry = null;
			fse.printStackTrace();
			fail("Error creating test file: ", fse);
		} catch (TransformerException te) {
			te.printStackTrace();
			fail("Error creating test file: ", te);
		}
	}

	public void deleteFileAndQuit() {

		if (fileEntry != null) {
			if (fileService == null)
				fileService = getFileService();

			try {
				fileService.deleteFile(fileEntry.getFileId());
			} catch (FileServiceException fse) {
				fileEntry = null;
				if (failIfAfterDeletionFails()) {
					fail("Error deleting test file: ", fse);
					fse.printStackTrace();
				}
			}
		}
		if (folder != null) {
			if (fileService == null)
				fileService = getFileService();

			try {
				fileService.deleteFolder(folder.getFileId());
			} catch (FileServiceException fse) {
				folder = null;
				if (failIfAfterDeletionFails()) {
					fail("Error deleting test folder: ", fse);
					fse.printStackTrace();
				}
			}
		}
		if (community != null) {
			if (communityService == null)
				communityService = getCommunityService();
			try {
				communityService.deleteCommunity(community.getCommunityUuid());
			} catch (CommunityServiceException e) {
				fail("Error deleting test community: ", e);
				e.printStackTrace();
			}
		}

	}

	public void deleteFile(File file) {
		fileService = getFileService();
		if (file != null) {
			try {
				fileService.deleteFile(file.getFileId());
			} catch (FileServiceException fse) {
				if (failIfAfterDeletionFails()) {
					fail("Error deleting test file: ", fse);
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

	/**
	 * @param snippetId
	 * @return
	 */
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
	 * Return true if the result page contains no error message.
	 */
	protected boolean containsNoError(String result) {
		boolean retVal = true;
		if (StringUtil.isEmpty(result)) {
			noErrorMsg = "Empty result was returned for: " + getSnippetId();
			retVal = false;
		} else {
			for (int i = 0; i < ErrorMessages.length; i++) {
				if (result.contains(ErrorMessages[i])) {
					noErrorMsg = "Error message for: " + getSnippetId() + ": "
							+ result;
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

	protected java.io.File createLocalFile() {
		String name = this.getClass().getName() + "#" + this.hashCode() + "-"
				+ System.currentTimeMillis();
		return createLocalFile(name, ".tmp", 1024);
	}

	protected java.io.File createLocalFile(String name, String ext, int size) {
		try {
			java.io.File tempFile = java.io.File.createTempFile(name, ext);

			FileWriter fw = new FileWriter(tempFile.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			char[] chars = new char[size];
			Arrays.fill(chars, 'X');
			bw.write(chars);
			bw.flush();
			bw.close();
			fw.close();
			return tempFile;
		} catch (IOException ioe) {
			fail("Unable to create temporary local file: ", ioe);
			return null;
		}
	}

}
