/*
 * © Copyright IBM Corp. 2013
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

package com.ibm.sbt.services.client.connections.communities;

import org.junit.Test;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.Version;

/**
 * Tests for the java connections Communities API a test class provides its own
 * tests extending the test endpoint abstract class
 * 
 * @author Swati Singh
 * @date Dec 12, 2012
 */

public class DeleteCommunityServiceTest extends BaseCommunityServiceTest {

	@Test
	public final void testDeleteCommunity() throws Exception {
		//FIXME: Test is Broken
		/*String communityUuid = community.getCommunityUuid();
		communityService.deleteCommunity(communityUuid);
		if (communityService.getApiVersion().lessThan(new Version(5, 0))){
			thrown.expect(ClientServicesException.class);
			thrown.expectMessage("404:Not Found");
			thrown.expectMessage("Request to url");
			community = communityService.getCommunity(communityUuid);
		}*/
	}
}