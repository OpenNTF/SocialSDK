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

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityMoveEntryPerformanceTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testMoveEntry() throws ClientServicesException {
		
		long start = System.currentTimeMillis();

		Activity activitySrc = createActivity("ActivityMoveEntryPerformanceTest Source");
		Activity activityDst = createActivity("ActivityMoveEntryPerformanceTest Destination");
		
		
		byte[] bytes = new byte[Integer.valueOf(1024)];
		Arrays.fill(bytes, (byte)0);
						
		ActivityNode nodeMove = new ActivityNode();
		nodeMove.setActivityUuid(activitySrc.getActivityUuid());
		nodeMove.setTitle("NodeToMove");
		nodeMove.setType(ActivityNode.TYPE_ENTRY);
		for (int j=0; j<10; j++) {
			TextField textField = new TextField();
			textField.setName("test_text"+j);
			textField.setPosition(1000);
			textField.setSummary("Test_Text_Field");
			
			nodeMove.addField(textField);
		}
		for (int j=0; j<10; j++) {	
			FileField fileField = new FileField();
			fileField.setName("test_file"+j);
			fileField.setHidden(true);
			fileField.setPosition(2000);

			nodeMove.addField(fileField);
			nodeMove.addAttachment(new ActivityAttachment("test_file"+j, new String(bytes), "text/plain"));
		}
		activityService.createActivityNode(nodeMove);
		
		for (int i=0; i<300; i++) {
			try {
				ActivityNode activityNode = new ActivityNode();
				activityNode.setActivityUuid(activityDst.getActivityUuid());
				activityNode.setTitle("DestinationNode" + "-" + i);
				activityNode.setType(ActivityNode.TYPE_ENTRY);
				for (int j=0; j<5; j++) {
					TextField textField = new TextField();
					textField.setName("test_text"+j);
					textField.setPosition(1000);
					textField.setSummary("Test_Text_Field");
					activityNode.addField(textField);
				}
				activityNode = activityService.createActivityNode(activityNode);
				System.out.print(".");
			} catch (Exception e) {
			}
		}
		long duration = System.currentTimeMillis() - start;
		System.out.println("Created Activities took "+duration+"(ms)");
		
		// move node from source to destination
		start = System.currentTimeMillis();
		activityService.moveNode(nodeMove.getActivityNodeUuid(), activityDst.getActivityUuid());
		duration = System.currentTimeMillis() - start;
		System.out.println(activityService.getEndpoint().getUrl()+" Moving node:"+nodeMove.getActivityNodeUuid()+" to activity:"+activityDst.getActivityUuid()+" took "+duration+"(ms)");
		
		EntityList<ActivityNode> descendants = activityDst.getDescendants();
		Assert.assertEquals("Invalid activity node uuid", descendants.get(0).getActivityNodeUuid(), nodeMove.getActivityNodeUuid());
	}
	
	
}
