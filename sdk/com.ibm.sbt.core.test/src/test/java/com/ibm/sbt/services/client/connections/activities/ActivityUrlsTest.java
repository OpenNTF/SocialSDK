/*
 * � Copyright IBM Corp. 2014
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

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.client.base.NamedUrlPart;

/**
 * @author mwallace
 *
 */
public class ActivityUrlsTest extends BaseActivityServiceTest {

	@Test
	public void testActivityUrls() throws XMLException, IOException {
		NamedUrlPart activityUuid = ActivityUrls.activityPart("11111-22222-33333-44444");
		NamedUrlPart activityNodeUuid = ActivityUrls.activityNodePart("11111-22222-33333-44444");
		NamedUrlPart memberId = ActivityUrls.memberPart("66666-77777-88888");
		
		Assert.assertEquals("activities/service/atom2/service", ActivityUrls.ACTIVITIES_SERVICE.format(activityService));
		Assert.assertEquals("activities/service/atom2/activity?activityUuid=11111-22222-33333-44444", ActivityUrls.ACTIVITY.format(activityService, activityUuid));
		Assert.assertEquals("activities/service/atom2/tags", ActivityUrls.ACTIVITY_TAGS.format(activityService));
		Assert.assertEquals("activities/service/atom2/entrytemplates?activityUuid=11111-22222-33333-44444", ActivityUrls.ACTIVITY_TEMPLATES.format(activityService, activityUuid));
		Assert.assertEquals("activities/service/atom2/everything", ActivityUrls.ALL_ACTIVITIES.format(activityService));
		Assert.assertEquals("activities/service/atom2/completed", ActivityUrls.COMPLETED_ACTIVITIES.format(activityService));
		Assert.assertEquals("activities/service/atom2/activities", ActivityUrls.MY_ACTIVITIES.format(activityService));
		Assert.assertEquals("activities/service/atom2/trash", ActivityUrls.TRASHED_ACTIVITIES.format(activityService));
		Assert.assertEquals("activities/service/atom2/todos", ActivityUrls.TODO_ACTIVITIES.format(activityService));
		Assert.assertEquals("activities/service/atom2/activitynode?activityNodeUuid=11111-22222-33333-44444", ActivityUrls.ACTIVITY_NODE.format(activityService, activityNodeUuid));
		Assert.assertEquals("activities/service/atom2/trashednode?activityNodeUuid=11111-22222-33333-44444", ActivityUrls.TRASHED_ACTIVITY_NODE.format(activityService, activityNodeUuid));
		Assert.assertEquals("activities/service/atom2/acl?activityUuid=11111-22222-33333-44444", ActivityUrls.ACTIVITY_ACL.format(activityService, activityUuid));
		Assert.assertEquals("activities/service/atom2/acl?activityUuid=11111-22222-33333-44444&memberid=66666-77777-88888", ActivityUrls.ACTIVITY_MEMBER.format(activityService, activityUuid, memberId));
	}
	
}
