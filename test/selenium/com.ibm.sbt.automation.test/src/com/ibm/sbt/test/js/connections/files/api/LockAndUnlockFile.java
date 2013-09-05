package com.ibm.sbt.test.js.connections.files.api;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class LockAndUnlockFile extends BaseFilesTest {

	static final String SNIPPET_ID_LOCK = "Social_Files_API_LockFile";
	static final String SNIPPET_ID_UNLOCK = "Social_Files_API_UnLockFile";

	static final String SNIPPET_ID_LOCK_FILE = "Social_Files_API_FileLock";
	static final String SNIPPET_ID_UNLOCK_FILE = "Social_Files_API_FileUnLock";

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
	public void testLockUnlockFile() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_LOCK);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));

		previewPage = executeSnippet(SNIPPET_ID_UNLOCK);
		json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));
	}

	@Test
	public void testFileLockUnlock() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_LOCK_FILE);
		JsonJavaObject json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));

		previewPage = executeSnippet(SNIPPET_ID_UNLOCK_FILE);
		json = previewPage.getJson();
		assertEquals("Success", json.getString("status"));
	}
}
