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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class ActivityFileFieldTest extends BaseActivityServiceTest {

	@Test
	public void testCreateFileField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		FileField fileField = new FileField();
		fileField.setName("test_file");
		fileField.setPosition(1000);

		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(createActivityTitle());
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		activityNode.addField(fileField);
		activityNode.addAttachment(new ActivityAttachment("test_file", "MyFileContent", "text/plain"));
		
		ActivityNode createdNode = activityService.createActivityNode(activityNode);
		
		ActivityNode readNode = activityService.getActivityNode(createdNode.getActivityNodeUuid());
		
		Field[] fields = readNode.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof FileField);
		Assert.assertEquals("test_file", ((FileField)fields[0]).getName());
		Assert.assertEquals(1000, ((FileField)fields[0]).getPosition());
		Assert.assertNotNull(((FileField)fields[0]).getEnclosureLink());
	}
	
	@Test
	public void testAddFileField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		ActivityNode activityNode = createActivityNode();
		
		FileField fileField = new FileField();
		fileField.setName("test_file");
		fileField.setPosition(1000);
		
		activityNode.addField(fileField);
		activityNode.addAttachment(new ActivityAttachment("test_file", "MyFileContent", "text/plain"));
		activityNode.setSummary("xxx");
		activityNode.update();
		
		ActivityNode readNode = activityService.getActivityNode(activityNode.getActivityNodeUuid());
		
		System.out.println(readNode.toXmlString());
		
		Field[] fields = readNode.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof FileField);
		Assert.assertEquals("test_file", ((FileField)fields[0]).getName());
		Assert.assertEquals(1000, ((FileField)fields[0]).getPosition());
		Assert.assertNotNull(((FileField)fields[0]).getEnclosureLink());
	}
	
}
