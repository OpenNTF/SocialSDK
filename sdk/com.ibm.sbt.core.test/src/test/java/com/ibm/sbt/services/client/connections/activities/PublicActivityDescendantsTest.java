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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class PublicActivityDescendantsTest extends BaseActivityServiceTest {
	
	private static String activityUuid = null;
	
	@Test
	public void testDescendantsPublicActivity() throws ClientServicesException, XMLException {
		Activity activity = createActivity();
		activityUuid = activity.getActivityUuid();
		for (int i=0; i<3; i++) {
			ActivityNode activityNode = createActivityNode(activityUuid, createActivityNodeTitle());
		}
		
		System.out.println(activityUuid);
		this.activity = null;
		
		String orgid = TestEnvironment.getProperty("customerid");
		orgid = StringUtil.isEmpty(orgid) ? "*" : orgid;
		activity.addMember(Member.TYPE_ORGANIZATION, orgid, Member.ROLE_READER);

		EntityList<ActivityNode> descendants = activityService.getActivityDescendants(activityUuid);
		Assert.assertEquals(3, descendants.size());

		ActivityService activityServiceAlt = new ActivityService(getAltEndpoint());
		EntityList<ActivityNode> descendantsAlt = activityServiceAlt.getActivityDescendants(activityUuid);
		Assert.assertEquals(3, descendantsAlt.size());
		EntityList<ActivityNode> childrenAlt = activityServiceAlt.getActivityNodeChildren(activityUuid);
		Assert.assertEquals(3, childrenAlt.size());
	}
	
/*	
	@Test
	public void testReadDescendantsPublicActivityAlt() throws ClientServicesException, XMLException {
		ActivityService activityService = new ActivityService(getAltEndpoint());
		EntityList<ActivityNode> descendants = activityService.getActivityNodeDescendants(activityUuid);
		Assert.assertEquals(3, descendants.size());
	}
		
	@Test
	public void testReadDescendantsPublicActivity() throws ClientServicesException, XMLException {
		EntityList<ActivityNode> descendants = activityService.getActivityNodeDescendants(activityUuid);
		Assert.assertEquals(3, descendants.size());
	}
*/
	
}
