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

public class GetActivityNode extends BaseActivitiesTest {

	static final String SNIPPET_ID = "Social_Activities_API_GetActivityNode";

	Activity activity;
	ActivityNode activityNode;

	@Before
	public void init() {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityId());
		activityNode = createActivityNode(activity.getActivityId(), "Entry");
		addSnippetParam("sample.activityNodeId", activityNode.getActivityId());
	}

	@After
	public void destroy() {
		deleteActivityNode(activityNode.getActivityId());
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testGetActivity() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		String activityNodeId = json.getAsString("getActivityNodeUuid");
		Assert.assertNotNull(activityNodeId);
	}
}
