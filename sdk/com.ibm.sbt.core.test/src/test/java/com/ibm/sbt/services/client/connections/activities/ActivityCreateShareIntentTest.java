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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.activities.serializers.ActivitySerializer;

/**
 * @author mwallace
 *
 */
public class ActivityCreateShareIntentTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivities() throws ClientServicesException, XMLException {
		Activity activityDefault = new Activity();
		activityDefault.setTitle("Default");
		activityService.createActivity(activityDefault);
		
		Activity activityInternal = new Activity();
		activityInternal.setTitle("Internal");
		activityInternal.setExternal(false);
		ActivitySerializer serializer = new ActivitySerializer(activityInternal);
		System.out.println(serializer.generateCreate());
		activityService.createActivity(activityInternal);
		System.out.println(activityInternal.toXmlString());
		
		
		Activity activityExternal = new Activity();
		activityExternal.setTitle("External");
		activityExternal.setExternal(true);
		activityService.createActivity(activityExternal);
		
		Assert.assertEquals("Invalid activity visibilty", false, activityDefault.isExternal());
		Assert.assertEquals("Invalid activity visibilty", false, activityInternal.isExternal());
		Assert.assertEquals("Invalid activity visibilty", true, activityExternal.isExternal());
	}

}
