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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.follow.model.Source;
import com.ibm.sbt.services.client.connections.follow.model.Type;

/**
 * @author Manish Kataria 
 * @author Carlos Manias
 */

public class FollowServiceTest extends BaseUnitTest { 
	
	protected Community community;
	protected FollowService followService;
	
	@Before
	public void initfollowServiceTest() throws CommunityServiceException {
		if (followService==null) {
			followService = new FollowService();
		}
		createCommunity();
	}

	public void createCommunity() throws CommunityServiceException{
		CommunityService service = new CommunityService();
		community = new Community(service, "");
		community.setTitle("Test Followservice Community " + System.currentTimeMillis());
		community.setContent("Java Community Content");
		String type = "public";
		community.setCommunityType(type);
		community = community.save();
	}

	@After
	public void deleteCommunity() throws Exception {
		if (!StringUtil.isEmpty(community.getCommunityUuid())) {
			CommunityService communityService = new CommunityService();
			communityService.deleteCommunity(community.getCommunityUuid());
		}
	}
	
	@Test
	public void getFollowedResource() throws FollowServiceException, CommunityServiceException {
		FollowedResource resource = followService.getFollowedResource(Source.COMMUNITIES.get(),Type.COMMUNITY.get(),community.getCommunityUuid());
		assertNotNull(resource);
		assertEquals(unRandomize(community.getCommunityUuid()), unRandomize(resource.getResourceId()));
	}
	
	@Test
	public void getFollowedResources() throws FollowServiceException, CommunityServiceException {
		EntityList<FollowedResource> resources = followService.getFollowedResources(Source.COMMUNITIES.get(),Type.COMMUNITY.get());
		assertNotNull(resources);
	}

	@Test
	public void startFollowing() throws FollowServiceException, CommunityServiceException {
		FollowedResource resource = followService.startFollowing(Source.COMMUNITIES.get(),Type.COMMUNITY.get(), community.getCommunityUuid());
		assertEquals(community.getCommunityUuid(), resource.getResourceId());
	}
	
	@Test
	public void stopFollowing() throws FollowServiceException, CommunityServiceException {
		followService.startFollowing(Source.COMMUNITIES.get(),Type.COMMUNITY.get(), community.getCommunityUuid());
		boolean success  = followService.stopFollowing(Source.COMMUNITIES.get(),Type.COMMUNITY.get(), community.getCommunityUuid());
		assertEquals(success, true);
	}

}
