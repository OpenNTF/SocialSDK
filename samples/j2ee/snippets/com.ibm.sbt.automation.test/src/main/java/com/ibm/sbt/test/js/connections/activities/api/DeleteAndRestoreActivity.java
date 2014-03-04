package com.ibm.sbt.test.js.connections.activities.api;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseActivitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.ActivityNode;

public class DeleteAndRestoreActivity extends BaseActivitiesTest {

	static final String SNIPPET_ID = "Social_Activities_API_DeleteAndRestoreActivity";

	Activity activity;


	@Before
	public void init() {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityId());
	}

	@After
	public void destroy() {
		deleteActivity(activity.getActivityId());
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
