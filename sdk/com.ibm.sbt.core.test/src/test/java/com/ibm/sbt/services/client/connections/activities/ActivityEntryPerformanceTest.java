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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityEntryPerformanceTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivityEntries() throws ClientServicesException {
		
		System.setErr(new PrintStream(new ByteArrayOutputStream())); 
		
		Activity activity = createActivity();
		String activityUuid = activity.getActivityUuid();
		String title = activity.getThemeId();

		FileField fileField = new FileField();
		fileField.setName("test_file");
		fileField.setHidden(true);
		fileField.setPosition(2000);
		
		byte[] bytes = new byte[Integer.valueOf(1024)];
		Arrays.fill(bytes, (byte)0);
				
		StringWriter stringWriter = new StringWriter();
		PrintWriter writer = new PrintWriter(stringWriter);
		writer.println("#,NodeUUID,Time(ms)");
		for (int i=0; i<500; i++) {
			try {
				long start = System.currentTimeMillis();
				
				System.out.print(i + " ");
				
				ActivityNode activityNode = new ActivityNode();
				activityNode.setActivityUuid(activityUuid);
				activityNode.setTitle(title + " - " + i);
				activityNode.setType(ActivityNode.TYPE_ENTRY);
				for (int j=0; j<5; j++) {
					TextField textField = new TextField();
					textField.setName("test_text"+j);
					textField.setPosition(1000);
					textField.setSummary("Test_Text_Field");
					activityNode.addField(textField);
				}
				activityNode.addField(fileField);
				activityNode.addAttachment(new ActivityAttachment("test_file", new String(bytes), "text/plain"));
				
				activityNode = activityService.createActivityNode(activityNode);
				
				long duration = System.currentTimeMillis() - start;
				writer.println(i+","+activityNode.getActivityNodeUuid()+","+duration);				
			} catch (Exception e) {
			}
		}
		
		System.out.println(stringWriter.toString());

		/*
		long start = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", "1");
		params.put("ps", "1");
		activityService.getActivityNodeDescendants(activityUuid, params);
		long duration = System.currentTimeMillis() - start;
		System.out.println("Reading all activity descendants took "+duration+"(ms)");
		
		start = System.currentTimeMillis();
		params.put("tag", "personal");
		activityService.getActivityNodeChildren(activityUuid, params);
		duration = System.currentTimeMillis() - start;
		System.out.println("Reading all activity children took "+duration+"(ms)");
		*/
	}
	
}
