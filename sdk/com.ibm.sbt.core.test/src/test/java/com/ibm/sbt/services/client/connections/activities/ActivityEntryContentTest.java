/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class ActivityEntryContentTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivityNodeSummary() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		StringBuilder content = new StringBuilder();
		for (int i=0; i<4096; i++) {
			content.append('0');
		}
		
		System.out.println("BYTES: " + content.toString().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + content.toString().length());

		Activity activity = createActivity();
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		activityNode.setSummary(content.toString());
		
		ActivityNode createdNode = activityService.createActivityNode(activityNode);
		
		ActivityNode readNode = activityService.getActivityNode(createdNode.getActivityNodeUuid());
		
		//System.out.println("CREATED: " + createdNode.toXmlString());
		//System.out.println("READ: " + readNode.toXmlString());
		
		String summary = readNode.getSummary().trim();
		
		System.out.println("READ BYTES: " + summary.getBytes("UTF-8").length);
		System.out.println("READ LENGTH: " + summary.length());
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivityNodeTextField() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		StringBuilder content = new StringBuilder();
		for (int i=0; i<512; i++) {
			content.append('0');
		}
		
		System.out.println("BYTES: " + content.toString().getBytes("UTF-8").length);
		System.out.println("LENGTH: " + content.toString().length());

		Activity activity = createActivity();
		
		TextField textField = new TextField();
		textField.setName("content");
		textField.setPosition(1000);
		textField.setSummary(content.toString());
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		activityNode.addField(textField);
		
		ActivityNode createdNode = activityService.createActivityNode(activityNode);
		
		ActivityNode readNode = activityService.getActivityNode(createdNode.getActivityNodeUuid());
		
		//System.out.println("CREATED: " + createdNode.toXmlString());
		//System.out.println("READ: " + readNode.toXmlString());
		
		String summary = ((TextField)readNode.getFieldByName("content")).getSummary();
		
		System.out.println("READ BYTES: " + summary.getBytes("UTF-8").length);
		System.out.println("READ LENGTH: " + summary.length());
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivityNodeCompare() throws ClientServicesException, XMLException, UnsupportedEncodingException {
		createActivity();
		
		StringBuilder summary = new StringBuilder();
		for (int i=0; i<512; i++) {
			summary.append('0');
		}
		
		StringBuilder content = new StringBuilder();
		for (int i=0; i<2560; i++) {
			content.append('0');
		}
		
		long start = 0;
		
		Activity summaryActivity = new Activity();
		summaryActivity.setTitle(createActivityTitle());
		summaryActivity.setContent(content.toString());
		
		start = System.currentTimeMillis();
		summaryActivity = activityService.createActivity(summaryActivity);
		System.out.println("Create summary activity took: "+(System.currentTimeMillis()-start)+"(ms)");
		
		Activity fieldActivity = new Activity();
		fieldActivity.setTitle(createActivityTitle());
		fieldActivity.setContent(content.toString());
		for (int i=0; i<5; i++) {
			TextField textField = new TextField();
			textField.setName("content"+i);
			textField.setPosition(1000);
			textField.setSummary(summary.toString());
			fieldActivity.addField(textField);			
		}
		
		start = System.currentTimeMillis();
		fieldActivity = activityService.createActivity(fieldActivity);
		System.out.println("Create field activity took: "+(System.currentTimeMillis()-start)+"(ms)");

		Activity activity = createActivity();
		
		ActivityNode summaryNode = new ActivityNode();
		summaryNode.setActivityUuid(activity.getActivityUuid());
		summaryNode.setTitle(createActivityTitle());
		summaryNode.setType(ActivityNode.TYPE_ENTRY);
		summaryNode.setSummary(content.toString());
		
		start = System.currentTimeMillis();
		summaryNode = activityService.createActivityNode(summaryNode);
		System.out.println("Create summary node took: "+(System.currentTimeMillis()-start)+"(ms)");
		
		ActivityNode fieldNode = new ActivityNode();
		fieldNode.setActivityUuid(activity.getActivityUuid());
		fieldNode.setTitle(createActivityTitle());
		fieldNode.setType(ActivityNode.TYPE_ENTRY);
		for (int i=0; i<5; i++) {
			TextField textField = new TextField();
			textField.setName("content"+i);
			textField.setPosition(1000);
			textField.setSummary(summary.toString());
			fieldNode.addField(textField);			
		}
		
		start = System.currentTimeMillis();
		activityService.createActivityNode(fieldNode);
		System.out.println("Create field node took: "+(System.currentTimeMillis()-start)+"(ms)");
	}

}
