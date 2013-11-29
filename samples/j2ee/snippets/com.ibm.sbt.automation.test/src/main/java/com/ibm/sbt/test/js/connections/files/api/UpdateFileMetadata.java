package com.ibm.sbt.test.js.connections.files.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class UpdateFileMetadata extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_UpdateFileMetadata";

	static final String SNIPPET_ID_FILE = "Social_Files_API_FileUpdate";

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
	public void testUpdateFile() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		assertTrue(json.getString("getLabel").contains("New Label"));
		assertEquals("New Summary", json.getString("getSummary"));
		assertEquals("public", json.getString("getVisibility"));
	}

	@Test
	public void testFileUpdate() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_FILE);
		JsonJavaObject json = previewPage.getJson();
		assertTrue(json.getString("getLabel").contains("New Label"));
		assertEquals("New Summary", json.getString("getSummary"));
		assertEquals("public", json.getString("getVisibility"));
	}
}
