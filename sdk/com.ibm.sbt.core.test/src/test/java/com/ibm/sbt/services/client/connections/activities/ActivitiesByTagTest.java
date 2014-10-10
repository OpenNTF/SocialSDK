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

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.test.lib.TestEnvironment;

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
    	
    	Activity activity = createActivity(createActivityTitle(), tags, true);
    	
    	this.activity = null;
    			
		String orgid = TestEnvironment.getProperty("customerid");
		orgid = StringUtil.isEmpty(orgid) ? "*" : orgid;
		activity.addMember(Member.TYPE_ORGANIZATION, orgid, Member.ROLE_READER);
		//String userid = System.getProperty("UserIdAlt");
		//activity.addMember(Member.TYPE_PERSON, userid, Member.ROLE_READER);
		
		//System.out.println(activity.toXmlString());
    	
    	Map<String, String> parameters = new HashMap<String, String>();
    	parameters.put("nodetype", "activity");
    	parameters.put("tag", "fethard");
    	parameters.put("priority", "all");
    	parameters.put("completed", "yes");

		EntityList<Activity> activities = activityService.getMyActivities(parameters);
		
		Assert.assertTrue(activities.size() > 0);
		Assert.assertEquals(activity.getActivityUuid(), activities.get(0).getActivityUuid());
		Assert.assertEquals(2, activities.get(0).getTags().size());	
		
		ActivityService activityServiceAlt = new ActivityService(getAltEndpoint());
		EntityList<Activity> activitiesAlt = activityServiceAlt.getMyActivities(parameters);
		Assert.assertTrue(activitiesAlt.size()>0);
		Assert.assertEquals(activity.getActivityUuid(), activitiesAlt.get(0).getActivityUuid());
		Assert.assertEquals(2, activitiesAlt.get(0).getTags().size());	
	}
	
}
