package com.ibm.sbt.test.js.connections.files.api;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class DeleteFile extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_DeleteFile";

	static final String SNIPPET_ID_FILE = "Social_Files_API_FileRemove";

	@Before
	public void init() {
		createFile();
		addSnippetParam("sample.fileId", fileEntry.getFileId());
	}

	@After
	public void destroy() {
		deleteFileAndQuit();
	}

	@Test
	public void deleteFile() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));
		fileEntry = null;
	}

	@Test
	public void fileRemove() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_FILE);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));
		fileEntry = null;
	}
}
