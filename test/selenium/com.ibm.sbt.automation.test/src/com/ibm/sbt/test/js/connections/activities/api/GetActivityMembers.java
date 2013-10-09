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
import com.ibm.sbt.services.client.connections.activity.Member;

public class GetActivityMembers extends BaseActivitiesTest {
	
	static final String SNIPPET_ID = "Social_Activities_API_GetActivityMembers";
	
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
	public void testGetActivityMembers() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		 List jsonList = previewPage.getJsonList();
	     Assert.assertFalse("No members for activity ", jsonList.isEmpty());
	}
}
