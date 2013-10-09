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

public class UpdateActivityNode extends BaseActivitiesTest {

	static final String SNIPPET_ID = "Social_Activities_API_UpdateActivityNode";

	Activity activity;
	ActivityNode activityNode;

	@Before
	public void init() {
		activity = createActivity();
		activityNode = createActivityNode(activity.getActivityId(), "Entry");
		addSnippetParam("sample.activityNodeId", activityNode.getActivityId());
	}

	@After
	public void destroy() {
		deleteActivity(activity.getActivityId());
	}

	@Test
	public void testUpdateActivityNode() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		String activityNodeId = json.getAsString("getActivityNodeUuid");
		Assert.assertNotNull(activityNodeId);
		Assert.assertTrue("Title not updated successfuly", json.getAsString("getTitle").startsWith("New Title Updated by JS Snippet"));
		Assert.assertEquals(activityNode.getActivityId(), activityNodeId);
	}
}
