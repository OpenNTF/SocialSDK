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

import java.util.HashMap;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.files.Comment;

public class GetPublicFileComments extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_GetPublicFileComments";

	private List<Comment> comments;

	@Before
	public void init() {
		if (environment.isSmartCloud()) {
			return;
		}
		createFile();
		addSnippetParam("sample.fileId", fileEntry.getFileId());
		try {
			comments = fileService.getMyFileComments(fileEntry.getFileId(), new HashMap<String, String>());
		} catch (ClientServicesException e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@After
	public void destroy() {
		if (environment.isSmartCloud()) {
			return;
		}
		deleteFileAndQuit();
	}

	@Test
	public void testGetPublicFileComments() {
		if (environment.isSmartCloud()) {
			return;
		}
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		try {
			@SuppressWarnings({ "rawtypes" })
			List jsonList = previewPage.getJsonList();
			Assert.assertEquals(jsonList.size(), comments.size());
			for (int i = 0; i < jsonList.size(); i++) {
				JsonJavaObject fileJsonObj = (JsonJavaObject) jsonList.get(i);
				Assert.assertTrue("comment was not found in comment list", existComments(fileJsonObj.getString("getContent")));				
			}
		} catch (Exception ex) {
			Assert.fail(previewPage.getJson().getJsonObject("cause").getString("message"));
		}
	}

	private boolean existComments(String content) {
		for (Comment comment : comments) {
			if (content == null) {
				if (comment.getComment() == null)
					return true;
				else
					continue;
			}
			if (comment.getComment().equals(content)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public WebElement waitForResult(int timeout) {
		if(comments == null){
			return super.waitForResult(timeout);
		}
		return waitForJsonList(comments.size(), timeout);
	}

}
