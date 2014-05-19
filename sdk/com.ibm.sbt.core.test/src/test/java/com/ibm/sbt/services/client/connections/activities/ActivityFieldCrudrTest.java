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

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.activities.serializers.ActivitySerializer;
import com.ibm.sbt.services.client.connections.common.Link;
import com.ibm.sbt.services.client.connections.common.Person;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class ActivityFieldCrudrTest extends BaseActivityServiceTest {

	@Test
	public void testCreateDateField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		Date date = new Date();
		date.setTime(1397650699000L);
		
		DateField dateField = new DateField();
		dateField.setName("test_date");
		dateField.setPosition(1000);
		dateField.setDate(date);
		
		activity.addField(dateField);
		activity.update();
		
		//ActivitySerializer serializer = new ActivitySerializer(activity);
		//System.out.println(serializer.generateUpdate());
		
		Activity read = activity.getActivityService().getActivity(activity.getActivityUuid());
		//System.out.println(read.toXmlString());
		
		Field[] fields = read.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof DateField);
		Assert.assertEquals("test_date", ((DateField)fields[0]).getName());
		Assert.assertEquals(1000, ((DateField)fields[0]).getPosition());
		Assert.assertNotNull(((DateField)fields[0]).getDate());
		//Assert.assertEquals(dateFormat.format(date), dateFormat.format(((DateField)fields[0]).getDate()));
		//Assert.assertEquals(date.getTime(), ((DateField)fields[0]).getDate().getTime());
	}
	
	@Test
	public void createTextField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		TextField textField = new TextField();
		textField.setName("test_text");
		textField.setPosition(1000);
		textField.setSummary("Test_Text_Field");
		
		activity.addField(textField);
		activity.update();
		
		ActivitySerializer serializer = new ActivitySerializer(activity);
		System.out.println(serializer.generateUpdate());
		
		Activity read = activity.getActivityService().getActivity(activity.getActivityUuid());
		System.out.println(read.toXmlString());
		
		Field[] fields = read.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof TextField);
		Assert.assertEquals("test_text", ((TextField)fields[0]).getName());
		Assert.assertEquals(1000, ((TextField)fields[0]).getPosition());
		Assert.assertEquals("Test_Text_Field", ((TextField)fields[0]).getSummary());
	}
	
	@Test
	public void createHiddenTextField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		TextField textField = new TextField();
		textField.setName("test_hidden_text");
		textField.setPosition(1000);
		textField.setSummary("Hidden_Text_Field");
		textField.setHidden(true);
		
		activity.addField(textField);
		activity.update();
		
		ActivitySerializer serializer = new ActivitySerializer(activity);
		System.out.println(serializer.generateUpdate());
		
		Activity read = activity.getActivityService().getActivity(activity.getActivityUuid());
		System.out.println(read.toXmlString());
		
		Field[] fields = read.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof TextField);
		Assert.assertTrue(((TextField)fields[0]).isHidden());
		Assert.assertEquals("test_hidden_text", ((TextField)fields[0]).getName());
		Assert.assertEquals(1000, ((TextField)fields[0]).getPosition());
		Assert.assertEquals("Hidden_Text_Field", ((TextField)fields[0]).getSummary());
	}
	
	@Test
	public void createLinkField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		LinkField linkField = new LinkField();
		linkField.setName("test_link");
		linkField.setPosition(1000);
		linkField.setLink(new Link("IBM", "http://www.ibm.com"));
		
		activity.addField(linkField);
		activity.update();
		
		ActivitySerializer serializer = new ActivitySerializer(activity);
		System.out.println(serializer.generateUpdate());
		
		Activity read = activity.getActivityService().getActivity(activity.getActivityUuid());
		System.out.println(read.toXmlString());
		
		Field[] fields = read.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof LinkField);
		Assert.assertEquals("test_link", ((LinkField)fields[0]).getName());
		Assert.assertEquals(1000, ((LinkField)fields[0]).getPosition());
		Assert.assertEquals("IBM", ((LinkField)fields[0]).getLink().getTitle());
		Assert.assertEquals("http://www.ibm.com", ((LinkField)fields[0]).getLink().getHref());
	}
	
	
	@Test
	public void createPersonField() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		
		String name = TestEnvironment.getSecondaryUserDisplayName();
		String email = TestEnvironment.getSecondaryUserEmail();
		String userid = TestEnvironment.getSecondaryUserUuid();
		
		PersonField personField = new PersonField();
		personField.setName("test_person");
		personField.setPosition(1000);
		personField.setPerson(new Person(name, email, userid));
		
		activity.addField(personField);
		activity.update();
		
		ActivitySerializer serializer = new ActivitySerializer(activity);
		System.out.println(serializer.generateUpdate());
		
		Activity read = activity.getActivityService().getActivity(activity.getActivityUuid());
		System.out.println(read.toXmlString());
		
		Field[] fields = read.getFields();
		
		Assert.assertNotNull(fields);
		Assert.assertEquals(1, fields.length);
		Assert.assertTrue(fields[0] instanceof PersonField);
		Assert.assertEquals("test_person", ((PersonField)fields[0]).getName());
		Assert.assertEquals(1000, ((PersonField)fields[0]).getPosition());
		Assert.assertEquals(name, ((PersonField)fields[0]).getPerson().getName());
		//Assert.assertEquals(email, ((PersonField)fields[0]).getPerson().getEmail());
		Assert.assertEquals(userid, ((PersonField)fields[0]).getPerson().getUserid());
		Assert.assertNotNull(((PersonField)fields[0]).getPerson().getUserState());
	}
}
