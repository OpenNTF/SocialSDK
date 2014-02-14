/*
 * � Copyright IBM Corp. 2013
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.follow.model.Source;
import com.ibm.sbt.services.client.connections.follow.model.Type;

/**
 * @author Manish Kataria 
 */

public class FollowServiceTest extends BaseUnitTest { 
	
	protected Community community;
	protected FollowService followService;
	
	@Before
	public void initfollowServiceTest() {
		if (followService==null) {
			followService = new FollowService();
		}
	}

	public void createCommunity(){
		try {
			CommunityService service = new CommunityService();
			Community community = new Community(service, "");
			community.setTitle("Test Followservice Community " + System.currentTimeMillis());
			community.setContent("Java Community Content");
			String type = "public";
			community.setCommunityType(type);
			community = community.save();
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling Communityservice.save() caused by: "+e.getMessage());
		}
	}

	@After
	public void deleteCommunity() throws Exception {
		if (!StringUtil.isEmpty(community.getCommunityUuid())) {
			CommunityService communityService = new CommunityService();
			communityService.deleteCommunity(community.getCommunityUuid());
		}
	}
	
	@Test
	public void getFollowedResource() {
		try {
			createCommunity();
			FollowedResource resource = followService.getFollowedResource(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType(),community.getCommunityUuid());
			assertNotNull(resource);
			assertEquals(community.getCommunityUuid(), resource.getResourceId());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling FollowService.getFollowedResource() caused by: "+e.getMessage());
		}
	}
	
	@Test
	public void getFollowedResources() {
		try {
			createCommunity();
			EntityList<FollowedResource> resources = followService.getFollowedResources(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType());
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
			createCommunity();
			boolean success  = followService.stopFollowing(Source.COMMUNITIES.getSourceType(),Type.COMMUNITIES.getType(), community.getCommunityUuid());
			assertEquals(success, true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Error calling FollowService.stopFollowing() caused by: "+e.getMessage());
		}
	}

}
