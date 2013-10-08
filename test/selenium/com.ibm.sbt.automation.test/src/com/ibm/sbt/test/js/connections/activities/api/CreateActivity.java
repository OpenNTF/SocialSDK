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

public class CreateActivity extends BaseActivitiesTest {

	static final String SNIPPET_ID = "Social_Activities_API_CreateActivity";

	String activityId = null;

	@After
	public void destroy() {
		deleteActivity(activityId);
	}

	@Test
	public void testCreateActivity() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		activityId = json.getAsString("getActivityId");
		Assert.assertNotNull(activityId);
	}
}
