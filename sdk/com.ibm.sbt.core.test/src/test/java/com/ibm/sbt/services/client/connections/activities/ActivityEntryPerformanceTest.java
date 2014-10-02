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

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ActivityEntryPerformanceTest extends BaseActivityServiceTest {

	@Test
	public void testCreateActivityEntries() throws ClientServicesException {
		
		System.setErr(new PrintStream(new ByteArrayOutputStream())); 
		
		Activity activity = createActivity();
		String activityUuid = activity.getActivityUuid();
		String title = activity.getThemeId();
		System.out.println("#,NodeUUID,Time(ms)");
		for (int i=0; i<500; i++) {
			long start = System.currentTimeMillis();
			ActivityNode activityNode = createActivityNode(activityUuid, title + " - " + i, ActivityNode.TYPE_ENTRY);
			long duration = System.currentTimeMillis() - start;
			System.out.println(i+","+activityNode.getActivityNodeUuid()+","+duration);
		}

		/*
		long start = System.currentTimeMillis();
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", "1");
		params.put("ps", "1");
		activityService.getActivityNodeDescendants(activityUuid, params);
		long duration = System.currentTimeMillis() - start;
		System.out.println("Reading all activity descendants took "+duration+"(ms)");
		
		start = System.currentTimeMillis();
		params.put("tag", "personal");
		activityService.getActivityNodeChildren(activityUuid, params);
		duration = System.currentTimeMillis() - start;
		System.out.println("Reading all activity children took "+duration+"(ms)");
		*/
	}
	
}
