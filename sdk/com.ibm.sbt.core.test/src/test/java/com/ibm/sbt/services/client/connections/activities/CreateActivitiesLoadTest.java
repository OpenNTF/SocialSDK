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
import java.util.List;

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class CreateActivitiesLoadTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityNodes() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		
    	List<String> tags1 = new ArrayList<String>();
    	tags1.add("personal");
    	tags1.add("ibmsbt");
		
    	List<String> tags2 = new ArrayList<String>();
    	tags2.add("storage");
    	tags2.add("ibmsbt");
		
    	List<String> tags3 = new ArrayList<String>();
    	tags3.add("profile");
    	tags3.add("ibmsbt");
		
		for (int j=0; j<99; j++) {
			Activity activity = createActivity("Load Test Activity - "+start, (j%2 == 0) ? tags1 : tags2);
			String activityUuid = activity.getActivityUuid();
			System.out.print(".");
			createActivityNode(activityUuid, "Personal Node - " +start + " - ibmsbtpersonal", ActivityNode.TYPE_ENTRY, tags1);
			createActivityNode(activityUuid, "Storage Node - "  +start + " - ibmsbtstorage", ActivityNode.TYPE_ENTRY, tags2);
			createActivityNode(activityUuid, "Profile Node - "  +start + " - ibmsbtprofile", ActivityNode.TYPE_ENTRY, tags3);
		}

		// prevent last activity being auto-deleted
		activity = null;
		
	}
	
}
