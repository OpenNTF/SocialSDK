/*
 * © Copyright IBM Corp. 2013
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
package com.ibm.sbt.test.js.connections.activities.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseActivitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.activities.Activity;

public class DeleteAndRestoreActivity extends BaseActivitiesTest {

	static final String SNIPPET_ID = "Social_Activities_API_DeleteAndRestoreActivity";

	Activity activity;

	@Before
	public void init() throws ClientServicesException {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityUuid());
	}

	@After
	public void destroy() throws ClientServicesException {
		deleteActivity(activity.getActivityUuid());
	}

	@Override
	protected boolean isEnvironmentValid() {
		//TODO: enable test after fix of this ticket 
		// https://swgjazz.ibm.com:8004/jazz/web/projects/OCS#action=com.ibm.team.workitem.viewWorkItem&id=135926
		// sc respond a 301 and the browser attempt a cross domain request
		if (getTestEnvironment().isSmartCloud()) return false;
		return super.isEnvironmentValid();
	}

	@Test
	public void testDeleteAndRestoreActivity() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		Assert.assertEquals(json.getAsInt("status"), 204);
	}
}
