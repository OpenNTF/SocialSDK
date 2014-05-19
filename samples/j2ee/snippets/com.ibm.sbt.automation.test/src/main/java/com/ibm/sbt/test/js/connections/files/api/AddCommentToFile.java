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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;

public class AddCommentToFile extends BaseFilesTest {

	static final String SNIPPET_ID_FILE = "Social_Files_API_FileAddComment";

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
	public void testFileAddComment() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID_FILE);
		JsonJavaObject json = previewPage.getJson();
		assertTrue(json.getString("getContent").startsWith("Comment Added from JS Sample"));
		assertEquals(fileEntry.getAuthor().getId(), json.getJsonObject("getAuthor").getString("authorUserId"));
		assertEquals(fileEntry.getAuthor().getName(), json.getJsonObject("getAuthor").getString("authorName"));
		if (!StringUtil.isEmpty(fileEntry.getAuthor().getEmail())) {
			assertEquals(fileEntry.getAuthor().getEmail(), json.getJsonObject("getAuthor").getString("authorEmail"));
		}
		assertEquals(fileEntry.getAuthor().getUserState(), json.getJsonObject("getAuthor").getString("authorUserState"));
		assertEquals("Re: " + fileEntry.getTitle(), json.getString("getTitle"));
		assertEquals("1", json.getString("getVersionLabel"));
		assertEquals(fileEntry.getAuthor().getId(), json.getJsonObject("getModifier").getString("modifierUserId"));
		assertEquals(fileEntry.getAuthor().getUserState(), json.getJsonObject("getModifier").getString("modifierUserState"));
		assertEquals("en", json.getString("getLanguage"));
		assertEquals("false", json.getString("getDeleteWithRecord"));
	}

}
