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

import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebElement;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseFilesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.files.File;

public class GetMyFolders extends BaseFilesTest {

	static final String SNIPPET_ID = "Social_Files_API_GetMyFolders";

	private List<File> folders;

	@Before
	public void init() {
		//try {
			fileService = getFileService();
			if (!environment.isSmartCloud() && folder == null) {
				createFolder();
			}
			//folders = fileService.getMyFolders(new HashMap<String, String>());
		//} catch (ClientServicesException e) {
			//Assert.fail(e.getMessage());
		//}
	}
	
	@After
	public void destroy() {
		if (!environment.isSmartCloud()) {
			deleteFileAndQuit();
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
