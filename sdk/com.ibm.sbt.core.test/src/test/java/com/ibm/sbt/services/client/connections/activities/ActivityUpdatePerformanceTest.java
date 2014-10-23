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

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class ActivityUpdatePerformanceTest extends BaseActivityServiceTest {

	@Test
	public void testUpdateActivity() throws ClientServicesException {
		String activityUuid = System.getProperty("activityUuid");
		
		long start = System.currentTimeMillis();
		Activity activity = activityService.getActivity(activityUuid);
		long duration = System.currentTimeMillis() - start;
		
		System.out.println("Reading activity with " + activity.getFields().length + " fields, takes: " + duration + "(ms)");
		
		start = System.currentTimeMillis();
		activity.setContent("Updated: "+System.currentTimeMillis());
		activity.update();
		duration = System.currentTimeMillis() - start;
		
		System.out.println("Saving activity with " + activity.getFields().length + " fields, takes: " + duration + "(ms)");
	}	

}
