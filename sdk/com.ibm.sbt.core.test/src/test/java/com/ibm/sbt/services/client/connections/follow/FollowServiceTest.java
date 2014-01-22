/*
 *  Copyright IBM Corp. 2013
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
package com.ibm.sbt.services.client.connections.follow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.follow.model.Source;
import com.ibm.sbt.services.client.connections.follow.model.Type;

/*
 * @author Manish Kataria 
 */

public class FollowServiceTest extends BaseUnitTest { 
	
	public static String communityId;
	
	@Before
	public void createCommunity(){
		if(StringUtil.isNotEmpty(communityId)){
		}else{
			try {
				CommunityService service = new CommunityService();
				Community community = new Community(service, "");
				community.setTitle("Test Followservice Community " + System.currentTimeMillis());
				community.setContent("Java Community Content");
				String type = "public";
				community.setCommunityType(type);
				community = community.save();
				communityId = community.getCommunityUuid();
			} catch (Exception e) {
				e.printStackTrace();
				fail("Error calling Communityservice.save() caused by: "+e.getMessage());
			}
		}
		
	}
	
	@Test @Ignore
	public void getFollowedResource() {
		try {
			FollowService service = new FollowService();
			FollowedResource resource = service.getFollowedResource(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType(),communityId);
			assertNotNull(resource);
			assertEquals(communityId, resource.getResourceId());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling FollowService.getFollowedResource() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getFollowedResources() {
		try {
			FollowService service = new FollowService();
			EntityList<FollowedResource> resources = service.getFollowedResources(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType());
			assertNotNull(resources);

		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling FollowService.getFollowedResources() caused by: "+e.getMessage());
		}
	}
	

	@Test
	public void startFollowing() {
		try {
			
			CommunityService service = new CommunityService();
			Community community = new Community(service, "");
			community.setTitle("Test StartFollow Community " + System.currentTimeMillis());
			community.setContent("Java Community Content");
			String type = "public";
			community.setCommunityType(type);
			community = community.save();
			
			FollowService followService = new FollowService();
			followService.stopFollowing(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType(), community.getCommunityUuid());
			
			FollowedResource resource = followService.startFollowing(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType(), community.getCommunityUuid());
			assertEquals(community.getCommunityUuid(), resource.getResourceId());
			
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling FollowService.startFollowing() caused by: "+e.getMessage());
		}
	}
	
	
	@Test
	public void stopFollowing() {
		try {
			FollowService service = new FollowService();
			boolean success  = service.stopFollowing(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType(), communityId);
			assertEquals(success, true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling FollowService.stopFollowing() caused by: "+e.getMessage());
		}
	}

}
