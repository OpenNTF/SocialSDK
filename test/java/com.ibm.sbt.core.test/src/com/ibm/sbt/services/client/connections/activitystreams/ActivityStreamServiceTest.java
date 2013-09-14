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
import java.util.Date;
import org.junit.Ignore;
import org.junit.Test;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.SBTServiceException;

public class ActivityStreamServiceTest extends BaseUnitTest {

	@Ignore
	@Test
	public final void testGetUpdatesFromUser() {
		try {
			ActivityStreamService service = new ActivityStreamService();
			authenticateEndpoint(service.getEndpoint(), "fadams", "passw0rd");
			ActivityStreamEntityList updates = service
					.getUpdatesFromUser("0EE5A7FA-3434-9A59-4825-7A7000278DAA");

			for (ActivityStreamEntity asentry : updates) {
				System.err.println("asentry.getActor() " + asentry.getActor().getName());
				assertEquals(asentry.getActor().getName(), "Frank Adams");
			}
		} catch (SBTServiceException e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public final void testGetUpdatesFromCommunity() {
		try {
			ActivityStreamService service = new ActivityStreamService();
			authenticateEndpoint(service.getEndpoint(), "fadams", "passw0rd");
			ActivityStreamEntityList updates = service
					.getUpdatesFromCommunity("b4f12458-3cc2-49d2-9cf3-08d3fcbd81d5");

			System.err.println("number of updates from community : " + updates.size());

			for (ActivityStreamEntity asentry : updates) {
				if (null != asentry.getCommunity()) { // Updates can also come in from news service, ignore
														// those
					System.err.println("communityid " + asentry.getCommunity().getCommunityName());
					assertEquals(asentry.getCommunity().getCommunityId(),
							"b4f12458-3cc2-49d2-9cf3-08d3fcbd81d5");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Ignore
	@Test
	public final void testPostEntry() {
		try {
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

			ActivityStreamService service = new ActivityStreamService();
			authenticateEndpoint(service.getEndpoint(), "fadams", "passw0rd");
			service.postEntry(postPayload);

			ActivityStreamEntityList updates = service.getAllUpdates();
			System.err.println("updates found " + updates.size());
			for (ActivityStreamEntity update : updates) {
				System.err.println("update.getEventTitle()" + update.getEventTitle());
				System.err.println("tobeposted" + tobeposted);

				assertEquals(update.getEventTitle(), tobeposted); // Just check the 1st update
				break;
			}

		} catch (Throwable e) {

		}
	}

	@Ignore
	@Test
	public final void testSearchForTags() {
		try {
			String searchfortag = "test";
			ActivityStreamService service = new ActivityStreamService();
			authenticateEndpoint(service.getEndpoint(), "fadams", "passw0rd");
			ActivityStreamEntityList updates = service.searchByTags(searchfortag);

			for (ActivityStreamEntity asentry : updates) {
				System.err.println("asentry.getEventTitle() " + asentry.getPlainTitle());
				assertTrue(asentry.getEventTitle().contains("#" + searchfortag));
			}
		} catch (SBTServiceException e) {
			e.printStackTrace();
		}
	}
}
