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
public class GetActivityDescendantsTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityNode() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		Activity activity = createActivity("CreatedActivityNodes-"+start);
		String activityUuid = activity.getActivityUuid();
		System.out.println("Created "+activityUuid);
    	List<String> tags = new ArrayList<String>();
    	tags.add("ibm");
		for (int i=0; i<2; i++) {
			start = System.currentTimeMillis();
			ActivityNode activityNode = createActivityNode(activityUuid, "CreatedNode-"+start);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Creating "+activityNode.getActivityNodeUuid()+" took "+duration+"(ms)");
		}
    	tags.add("sbt");
		for (int i=0; i<2; i++) {
			start = System.currentTimeMillis();
			ActivityNode activityNode = createActivityNode(activityUuid, "CreatedNode-"+start);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Creating "+activityNode.getActivityNodeUuid()+" took "+duration+"(ms)");
		}
		// prevent last activity being auto-deleted
		activity = null;
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("tag", "ibm");
		EntityList<ActivityNode> activityNodes = activityService.getActivityNodeDescendants(activityUuid, params);
		Assert.assertEquals(2, activityNodes.size());
		for (ActivityNode activityNode : activityNodes) {
			Assert.assertNotNull("Invalid activity id", activityNode.getActivityUuid());
			System.out.println(activityNode.toXmlString());
		}
		
	}
	
}
