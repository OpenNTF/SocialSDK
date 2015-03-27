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

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class ActivityFieldPerformanceTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateLargeActivity() throws ClientServicesException {
		Activity activity = createActivity();
		this.activity = null;
		System.out.println(activity.getActivityUuid());
		
		for (int i=0; i<100; i++) {
			ActivityNode activityNode = new ActivityNode();
			activityNode.setActivityUuid(activity.getActivityUuid());
			activityNode.setTitle("Entry"+i);
			activityNode.setType(ActivityNode.TYPE_ENTRY);
			
			for (int j=0; j<10; j++) {
				TextField textField = new TextField();
				textField.setName("Field"+j);
				textField.setSummary("Field"+j);
				activityNode.addField(textField);
			}
			activityService.createActivityNode(activityNode);
			
			System.out.print(".");
		}
		System.out.println("\nCreated 100 entries");
		
		StringBuilder sb = new StringBuilder();
		
		int count = 0;
		try {
		for (int i=0; i<100; i++) {
			for (int j=0; j<10; j++) {
				//createActivityNode(activity.getActivityUuid(), "Entry" + count);
				
				TextField textField = new TextField();
				textField.setName("Field" + count);
				textField.setSummary("Field" + count);
				activity.addField(textField);	
				
				System.out.print(".");

				count++;
				
				if ((count % 100) == 0) System.out.println("");
			}
			
			long startUpdate = System.currentTimeMillis();
			activity.update();
			long durationUpdate = System.currentTimeMillis() - startUpdate;
			
			long startRead = System.currentTimeMillis();
			activity = activityService.getActivity(activity.getActivityUuid());
			long durationRead = System.currentTimeMillis() - startRead;
			
			sb.append(count).append(",").append(durationUpdate).append(",").append(durationRead).append("\n");
		}
		System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		long start = System.currentTimeMillis();
		activity = activityService.getActivity(activity.getActivityUuid());
		long duration = System.currentTimeMillis() - start;
		
		System.out.println("Reading activity with " + activity.getFields().length + " fields, takes: " + duration + "(ms)");
				
		start = System.currentTimeMillis();
		activity.setContent("Updated: "+System.currentTimeMillis());
		activity.update();
		duration = System.currentTimeMillis() - start;
		
		System.out.println("Saving activity with " + count + " fields, takes: " + duration + "(ms)");
		
		System.out.println(sb.toString());
	}	
	
	@Test 
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testUpdateActivity() throws ClientServicesException {
		String activityUuid = System.getProperty("activityUuid");
		
		long start = System.currentTimeMillis();
		Activity activity = activityService.getActivity(activityUuid);
		long duration = System.currentTimeMillis() - start;
		
		System.out.println("Reading activity with " + activity.getFields().length + " fields, takes: " + duration + "(ms)");
		
		start = System.currentTimeMillis();
		activity.setContent("Updated: "+System.currentTimeMillis());
		activity.update();
		duration = System.currentTimeMillis() - start;
		
		System.out.println("Saving activity with " + activity.getFields().length + " fields, takes: " + duration + "(ms)");
	}	

}
