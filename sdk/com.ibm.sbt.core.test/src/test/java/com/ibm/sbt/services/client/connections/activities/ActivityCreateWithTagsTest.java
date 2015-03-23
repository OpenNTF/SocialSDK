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

/**
 * @author mwallace
 *
 */
public class ActivityCreateWithTagsTest extends BaseActivityServiceTest {

	@Test
	@org.junit.Ignore("Issue with Mime Depdency")
	public void testCreateActivityWithTags() throws ClientServicesException, XMLException {
    	List<String> tags = new ArrayList<String>();
        tags.add("client=46454e5f-cabe-4f00-a05a-69f15798f304");
        tags.add("drafts=c20eae58-176b-40b8-9ab7-93377a906692");
        tags.add("features=24010961-430e-498d-98f0-ed0b792c5bed");
        tags.add("flags=ea840904-5269-4175-bbc4-e85e5eb9fafb");
        tags.add("invites=a1230057-f896-423a-a0f9-cbfaa22455d2");
        tags.add("muted=09bc3c51-4837-4039-96b4-c97391f87a2d");
        tags.add("notification=f9229f27-60f0-41aa-9a00-532fec2c0438");
        tags.add("oldsearches=dc5e13b8-e047-4e98-a274-8b2080420ad2");
        tags.add("personal");
        tags.add("privacy=40947664-2c2b-424f-a8c6-bad03f03ea7d");
        tags.add("profile=7e30000f-f8e4-488e-8799-93f0e4bc4016");
        tags.add("readmarks=1653b46c-c523-441e-b822-99b5100cf0b1");
        tags.add("usages=513a38dd-edcb-4301-92f5-f8dd3822a1d0");
        tags.add("user=20133257");
        tags.add("user_licenses=7df630a7-7128-47ba-aeac-193097fd8c8b");
    	
		Activity activity = new Activity();
		activity.setTitle("Untitled");
		activity.setTags(tags);
		activity.setContent("");
		
		Activity created = activityService.createActivity(activity);
		
		//https://apps.na.collabservtest.lotus.com/activities/service/atom2/activities?nodetype=activity&page=1&priority=all&tag=user%3D20133257&ps=1&completed=yes
		Map<String, String> params = new HashMap<String, String>();
		params.put("nodetype", "activity");
		params.put("page", "1");
		params.put("priority", "all");
		params.put("tag", "user=20133257");
		params.put("ps", "1");
		params.put("completed", "yes");
		EntityList<Activity> activities = activityService.getMyActivities(params);
		
		//System.out.println("CREATED: " + created.toXmlString());
		
		Assert.assertEquals("Invalid activity count", 1, activities.size());
		Assert.assertEquals("Invalid activity id", created.getId(), activities.get(0).getId());
		Assert.assertEquals("Invalid activity tags", created.getTags().size(), activities.get(0).getTags().size());
	}

}
