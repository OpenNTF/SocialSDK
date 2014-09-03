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

import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class CreateActivitiesTest extends BaseActivityServiceTest {

	/*
	@Test
	public void testCreateActivities() throws ClientServicesException {
		for (int i=0; i<25; i++) {
			long start = System.currentTimeMillis();
			Activity activity = createActivity("CreatedActivity-"+start);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Creating "+activity.getActivityUuid()+" took "+duration+"(ms)");
			
			// wait  minute between creations
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
		// prevent last activity being auto-deleted
		activity = null;
	}
	*/
	
	@Test
	public void testCreateActivityNode() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		Activity activity = createActivity("CreatedActivityNodes-"+start);
		String activityUuid = activity.getActivityUuid();
		System.out.println(activity.toXmlString());
		System.out.println("Created "+activityUuid);
		for (int i=0; i<25; i++) {
			start = System.currentTimeMillis();
			ActivityNode activityNode = createActivityNode(activityUuid, "CreatedNode-"+start);
			long duration = System.currentTimeMillis() - start;
			System.out.println("Creating "+activityNode.getActivityNodeUuid()+" took "+duration+"(ms)");
			
			// wait  minute between creations
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
			}
		}
		// prevent last activity being auto-deleted
		activity = null;
	}
	
	@Test
	public void testCreateActivity() throws ClientServicesException, XMLException {
		long start = System.currentTimeMillis();
		Activity activity = createActivity("CreatedActivityNodes-"+start);
		String activityUuid = activity.getActivityUuid();
		System.out.println(activity.toXmlString());
		System.out.println("Created "+activityUuid);
		// prevent last activity being auto-deleted
		activity = null;
	}
	
}
