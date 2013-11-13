package com.ibm.sbt.test.js.connections.activities.api;

import org.junit.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.automation.core.test.connections.BaseActivitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.activity.Activity;
import com.ibm.sbt.services.client.connections.activity.Member;

public class DeleteActivityMember extends BaseActivitiesTest {
	
	static final String SNIPPET_ID = "Social_Activities_API_DeleteActivityMember";
	
	Activity activity;
	String memberId;

	@Before
	public void init() {
		activity = createActivity();
		addSnippetParam("sample.activityId", activity.getActivityId());
		
		String id2 = getProperty("sample.id2");
    	if (environment.isSmartCloud()) {
    		id2 = getProperty("smartcloud.id2");
    	}
    	Member member = addMember(activity.getActivityId(), id2);  
    	memberId = member.getMemberId();
    	addSnippetParam("sample.memberId", member.getMemberId());             
	}
	
	@After
	public void destroy() {
		deleteActivity(activity.getActivityId());
	}
	
	@Test
	public void testDeleteActivityMember() {
		JavaScriptPreviewPage previewPage = executeSnippet(SNIPPET_ID);
		String content = previewPage.getText();
		Assert.assertEquals(memberId, content.trim());
	}
}
