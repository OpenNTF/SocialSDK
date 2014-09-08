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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activity.ActivityRequiredException;

import junit.framework.Assert;
import junit.framework.TestFailure;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.experimental.categories.Categories.ExcludeCategory;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivitiesByTagTest extends BaseActivityServiceTest {
	
	@Test
	public void testGetActivitiesByTag() throws ClientServicesException, XMLException, UnsupportedEncodingException {
    	List<String> tags = new ArrayList<String>();
    	tags.add("fethard");
    	tags.add("ibmsbt");
    	

    	Activity activity = createActivity(createActivityTitle(), tags);
    	
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("nodetype", "activity");
    	parameters.put("tag", "fethard");

		EntityList<Activity> activities = activityService.getMyActivities(parameters);
		
		Assert.assertEquals(1, activities.size());
		Assert.assertEquals(activity.getActivityUuid(), activities.get(0).getActivityUuid());
	}
	
}
