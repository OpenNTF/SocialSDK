package com.ibm.sbt.test.js.connections.activities.api;

import java.util.List;

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

public class GetActivityNodes extends BaseActivitiesTest {
	
	static final String SNIPPET_ID = "Social_Activities_API_GetActivityNodes";
	
	Activity activity;
	ActivityNode activityNode1;
	ActivityNode activityNode2;

	@Before
	public void init() {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityId());		
		activityNode1 = createActivityNode(activity.getActivityId(), "Entry");	
		activityNode2 = createActivityNode(activity.getActivityId(), "ToDo");	
	}
	
	@After
	public void destroy() {
		deleteActivityNode(activityNode1.getActivityId());
		deleteActivityNode(activityNode2.getActivityId());
		deleteActivity(activity.getActivityId());		
	}
	
	@Test
	public void testGetActivityNodes() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		 List jsonList = previewPage.getJsonList();
		 Assert.assertFalse("Get activity nodes returned no entries", jsonList.isEmpty());
	}
}
