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

import com.ibm.sbt.services.client.ClientServicesException;

/**
 * @author mwallace
 *
 */
public class CreateActivityPerformanceTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivities() throws ClientServicesException {
		int create = 10;
		System.out.println("Creating "+create+" activities");
		for (int i=0; i<create; i++) {
			long start = System.currentTimeMillis();
			Activity activity = createActivity();
			long duration = System.currentTimeMillis() - start;
			System.out.println("Creating "+activity.getActivityUuid()+" took "+duration+"(ms)");
		}
	}
	
}
