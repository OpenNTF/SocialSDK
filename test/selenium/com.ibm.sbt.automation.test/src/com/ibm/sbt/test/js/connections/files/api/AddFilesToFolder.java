package com.ibm.sbt.test.js.connections.files.api;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class AddFilesToFolder extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_AddFilesToFolder";

	@Before
	public void init() {
		createFile();
		createFolder();
		addSnippetParam("sample.fileId", fileEntry.getFileId());
		addSnippetParam("sample.folderId", folder.getFileId());
	}

	@After
	public void destroy() {
		deleteFileAndQuit();
	}
	
	@Test
	public void testAddFilesToFolder() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));

	}
}
