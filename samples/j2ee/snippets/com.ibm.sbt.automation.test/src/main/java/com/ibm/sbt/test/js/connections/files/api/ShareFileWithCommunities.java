package com.ibm.sbt.test.js.connections.files.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class ShareFileWithCommunities extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_ShareFileWithCommunities";

	@Before
	public void init() {
		createFile();
		createCommunity();
		addSnippetParam("sample.fileId", fileEntry.getFileId());
		addSnippetParam("sample.fileCommunityId", community.getCommunityUuid());
	}

	@After
	public void destroy() {
		deleteFileAndQuit();
	}

	@Test
	public void testShareFileWithCommunities() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		Assert.assertEquals(json.getAsInt("status"), 204);
	}

}
