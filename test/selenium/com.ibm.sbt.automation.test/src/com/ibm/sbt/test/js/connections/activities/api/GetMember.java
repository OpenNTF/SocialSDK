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
import com.ibm.sbt.services.client.connections.activity.Member;

public class GetMember extends BaseActivitiesTest {
	
	static final String SNIPPET_ID = "Social_Activities_API_GetMember";
	
	Activity activity;
	String id;
	Member member;

	@Before
	public void init() {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityId());
		
		id = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		id = getProperty("smartcloud.id2");
    	}
    	member = addMember(activity.getActivityId(), id);  
    	addSnippetParam("sample.memberId", member.getMemberId());    
	}
	
	@After
	public void destroy() {
		deleteActivity(activity.getActivityId());
	}
	
	@Test
	public void testGetActivityMember() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		JsonJavaObject json = previewPage.getJson();
		Assert.assertEquals(json.getAsString("getUserId"), id);
	}
}
