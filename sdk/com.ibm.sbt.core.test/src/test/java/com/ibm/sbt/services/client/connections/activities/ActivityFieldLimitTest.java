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
public class ActivityFieldLimitTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testFieldsLimit() throws ClientServicesException, XMLException {
		Activity activity = createActivity();		
		
		int limit = 50;
		
		for (int i=0; i<limit; i++) {
			TextField textField = new TextField();
			textField.setName("test_text_"+i);
			textField.setPosition(1000);
			textField.setSummary("Test_Text_Field");
			activity.addField(textField);
		}
		
		//ActivitySerializer serializer = new ActivitySerializer(activity);
		//System.out.println(serializer.generateUpdate());
		
		long start = System.currentTimeMillis();
		
		activity.update();
		
		long duration = System.currentTimeMillis() - start;
		System.out.println("Update with " + limit + " fields took: "+duration+"(ms)");
		
		start = System.currentTimeMillis();
		
		Activity read = activity.getActivityService().getActivity(activity.getActivityUuid());
		//System.out.println(read.toXmlString());
		
		Field[] fields = read.getFields();
		
		duration = System.currentTimeMillis() - start;
		System.out.println("Read with " + fields.length + " fields took: "+duration+"(ms)");
			
		Assert.assertNotNull(fields);
		Assert.assertEquals(limit, fields.length);
	}

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testFieldsScale() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		int limit = 50;
		int count = 0;
		for (int i=0; i<limit; i++) {
			for (int j=0; j<10; j++) {
				TextField textField = new TextField();
				textField.setName("test_text_"+count);
				textField.setPosition(1000);
				textField.setSummary("Test_Text_Field");
				activity.addField(textField);
				count++;
			}
			long start = System.currentTimeMillis();
			activity.update();
			long duration = System.currentTimeMillis() - start;
			System.out.println("Update with " + count + " fields took: "+duration+"(ms)");
		}
	}

}
