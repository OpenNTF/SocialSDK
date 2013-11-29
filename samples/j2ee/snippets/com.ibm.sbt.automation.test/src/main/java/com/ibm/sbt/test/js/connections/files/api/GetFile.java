package com.ibm.sbt.test.js.connections.files.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileServiceException;

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
		} catch (FileServiceException e) {
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
