package com.ibm.sbt.test.js.connections.files.api;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class PinFileAndRemovePinFromFile extends BaseFilesTest {

	static final String SNIPPET_ID_PIN = "Social_Files_API_PinFile";
	static final String SNIPPET_ID_REMOVE_PIN = "Social_Files_API_RemovePinFromFile";

	static final String SNIPPET_ID_PIN_FILE = "Social_Files_API_FilePin";
	static final String SNIPPET_ID_REMOVE_PIN_FILE = "Social_Files_API_FileUnpin";

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
	public void testPinFileAndRemovePinFromFile() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_PIN);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));

		previewPage = executeSnippet(SNIPPET_ID_REMOVE_PIN);
		json = previewPage.getJson();
		assertEquals(fileEntry.getFileId(), json.getString("fileId"));
	}

	@Test
	public void testFilePinFileAndRemovePin() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_PIN_FILE);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));

		previewPage = executeSnippet(SNIPPET_ID_REMOVE_PIN_FILE);
		json = previewPage.getJson();
		assertEquals(fileEntry.getFileId(), json.getString("fileId"));
	}
}
