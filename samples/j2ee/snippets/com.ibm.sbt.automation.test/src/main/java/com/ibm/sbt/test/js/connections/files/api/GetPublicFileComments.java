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
import com.ibm.sbt.services.client.connections.files.Comment;
import com.ibm.sbt.services.client.connections.files.FileServiceException;

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
			comments = fileService.getAllFileComments(fileEntry.getFileId(), new HashMap<String, String>());
		} catch (FileServiceException e) {
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
