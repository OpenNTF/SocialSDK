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

public class AddMember extends BaseActivitiesTest {
	
	static final String SNIPPET_ID = "Social_Activities_API_AddMember";
	
	Activity activity;

	@Before
	public void init() {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityId());
		
		String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		id2 = getProperty("smartcloud.id2");
    	}    	      
        addSnippetParam("sample.userId2", id2);
	}
	
	@After
	public void destroy() {
		deleteActivity(activity.getActivityId());
	}
	
	@Test
	public void testAddMemberToActivity() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		Assert.assertEquals(json.getAsInt("status"), 201); // created
	}
}
