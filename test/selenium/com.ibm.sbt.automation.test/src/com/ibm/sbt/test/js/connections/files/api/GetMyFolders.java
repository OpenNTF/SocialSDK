package com.ibm.sbt.test.js.connections.files.api;

import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.files.File;
import com.ibm.sbt.services.client.connections.files.FileServiceException;

public class GetMyFolders extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_GetMyFolders";

	private List<File> folders;

	@Before
	public void init() {
		try {
			fileService = getFileService();
			folders = fileService.getMyFolders(new HashMap<String, String>());
		} catch (FileServiceException e) {
			Assert.fail(e.getMessage());
			e.printStackTrace();
		}
	}

	@Test
	public void testGetMyFolders() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		try {
			@SuppressWarnings({ "rawtypes" })
			List jsonList = previewPage.getJsonList();
			Assert.assertEquals(jsonList.size(), folders.size());
			for (int i = 0; i < jsonList.size(); i++) {
				JsonJavaObject fileJsonObj = (JsonJavaObject) jsonList.get(i);
				Assert.assertTrue("snippet loaded fodler not found in list", existsFolderWithLabel(fileJsonObj.getString("getLabel")));
			}
		} catch (Exception ex) {
			Assert.fail(previewPage.getJson().getJsonObject("cause").getString("message"));
		}
	}

	private boolean existsFolderWithLabel(String label) {
		for (File entry : folders) {
			if (label == null) {
				if (entry.getLabel() == null)
					return true;
				else
					continue;
			}
			if (entry.getLabel().equals(label)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public WebElement waitForResult(int timeout) {
		if(folders == null){
			return super.waitForResult(timeout);
		}
		return waitForJsonList(folders.size(), timeout);
	}

}
