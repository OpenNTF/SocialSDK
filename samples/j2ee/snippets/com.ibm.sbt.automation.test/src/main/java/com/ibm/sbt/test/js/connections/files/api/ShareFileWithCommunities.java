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
