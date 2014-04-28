/*
 * © Copyright IBM Corp. 2012
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

/**
 * @author mkataria
 * @date Dec 17, 2012
 */

package com.ibm.sbt.services.client.connections.activitystreams;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.test.lib.TestEnvironment;

public class ActivityStreamServiceTest extends BaseUnitTest {
	
	protected ActivityStreamService service;
	protected CommunityService communityService;
	
	@Before
	public void initBlogServiceTest() {
		if (service==null) {
			service = new ActivityStreamService();
		}
	}

	@Test
	public final void testGetUpdatesFromUser() throws ActivityStreamServiceException {
		String userId = TestEnvironment.getCurrentUserUuid();
		ActivityStreamEntityList updates = service.getUpdatesFromUser(userId);

		for (ActivityStreamEntity asentry : updates) {
			System.err.println("asentry.getActor() " + asentry.getActor().getName());
			assertEquals(asentry.getActor().getName(), "Frank Adams");
		}
	}

	public Community createTestCommunity() throws ClientServicesException {
		if (communityService==null){
			communityService = new CommunityService();
		}

		Community community = new Community(communityService, "");
		community.setTitle("JavaTestCommunity " + System.currentTimeMillis());
		community.setContent("Java Community Content");
		String type = "public";
		if (TestEnvironment.isSmartCloudEnvironment()) {
			type = "private";
		}
		community.setCommunityType(type);
		community = community.save();
		community = community.load();
		return community;
	}

	@Ignore
	@Test
	public final void testGetUpdatesFromCommunity() throws ActivityStreamServiceException, ClientServicesException{
		//Ignored because a method in ActivityStreamService to post to a Community is unimplemented
		Community community = createTestCommunity();
		ActivityStreamEntityList updates = service
				.getUpdatesFromCommunity(community.getCommunityUuid());

		System.err.println("number of updates from community : " + updates.size());

		for (ActivityStreamEntity asentry : updates) {
			if (null != asentry.getCommunity()) { // Updates can also come in from news service, ignore
													// those
				System.err.println("communityid " + asentry.getCommunity().getCommunityName());
				assertEquals(asentry.getCommunity().getCommunityId(),
						"b4f12458-3cc2-49d2-9cf3-08d3fcbd81d5");
			}

		}
	}

	public void deleteTestCommunity(Community community) throws Exception {
		TestEnvironment.setRequiresAuthentication(true);

		if (community != null) {
			communityService.deleteCommunity(community.getCommunityUuid());
		}
	}

	@Ignore
	@Test
	public final void testPostEntry() throws ActivityStreamServiceException {
		JsonJavaObject postPayload = new JsonJavaObject();
		JsonJavaObject actor = new JsonJavaObject();
		String tobeposted = new Double(Math.random()).toString();
		actor.put("id", "@me");

		JsonJavaObject object = new JsonJavaObject();
		object.put("summary", "update from junit");
		object.put("objectType", "note");
		object.put("id", tobeposted);
		object.put("displayName", "random update display");
		object.put("url", "http://www.ibm.com");

		postPayload.put("actor", actor);
		postPayload.put("verb", ASVerb.POST.getVerbType());
		postPayload.put("title", tobeposted);
		postPayload.put("content", "testpostback");
		postPayload.put("updated", new Date().getTime());
		postPayload.put("object", object);
		System.err.println(postPayload.toString());

		String postId = service.postEntry(postPayload);

		ActivityStreamEntityList updates = service.getAllUpdates();
		System.err.println("updates found " + updates.size());
		System.out.println("Expected Id "+postId);
		for (ActivityStreamEntity update : updates) {
			//System.err.println("update.getEventTitle()" + update.getEventTitle());
			//System.err.println("tobeposted" + tobeposted);
			System.out.println("Id "+update.getId());
			if (postId.equals(update.getId())){
				assertEquals(postId, update.getId());
				return;
			}
		}
		fail("Posted entry was not found");
	}

	@Test
	public final void testSearchForTags() throws ActivityStreamServiceException {
		String searchfortag = "test";
		ActivityStreamEntityList updates = service.searchByTags(searchfortag);

		for (ActivityStreamEntity asentry : updates) {
			System.err.println("asentry.getEventTitle() " + asentry.getPlainTitle());
			assertTrue(asentry.getEventTitle().contains("#" + searchfortag));
		}
	}
}
