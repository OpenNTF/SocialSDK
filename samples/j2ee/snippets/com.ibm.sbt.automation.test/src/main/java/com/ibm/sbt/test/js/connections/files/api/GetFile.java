/*
 * © Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.js.connections.files.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.files.File;

public class GetFile extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_GetFile";

	File file;

	@Before
	public void init() {
		try {
			createFile();
			addSnippetParam("sample.fileId", fileEntry.getFileId());
			fileService = getFileService();
			file = fileService.getFile(fileEntry.getFileId(), true);
		} catch (ClientServicesException e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@After
	public void destroy() {
		deleteFileAndQuit();
	}

	@Test
	public void testGetFile() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		try {
			JsonJavaObject fileJson = previewPage.getJson();
			Assert.assertEquals(fileJson.getString("getLabel"), file.getLabel());
			Assert.assertEquals(fileJson.getString("getTitle"), file.getTitle());
			Assert.assertEquals(fileJson.getString("getVisibility"), file.getVisibility());
		} catch (Exception ex) {
			Assert.fail(previewPage.getJson().getJsonObject("cause").getString("message"));
		}
	}

}
