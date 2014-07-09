package com.ibm.sbt.services.client.connections.communities;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.connections.communities.model.CommunityXPath;


/*
 * 
 * @author David Ryan
 * 
 */

public class CreateSubCommunity extends BaseUnitTest {

	protected CommunityService communityService;

	@Before
	public void initCommunityService() throws Exception {
		if (communityService == null) {
			communityService = new CommunityService();
		}
	}

	@Test
	public final void testCreateSubCommunity() throws Exception {
		Community parentCommunity = new Community();
		parentCommunity.setTitle("Test Parent Community"+System.currentTimeMillis());
		
		String parentId = communityService.createCommunity(parentCommunity);
		parentCommunity = communityService.getCommunity(parentId);
		
		Community subCommunity = new Community();
		subCommunity.setTitle("Sub Community"+System.currentTimeMillis());
		String subCommunityId = communityService.createSubCommunity(subCommunity,parentCommunity);		
		subCommunity = communityService.getCommunity(subCommunityId);
		
		assertTrue(subCommunity.isSubCommunity());
		assertTrue(parentCommunity.getSubCommunities().size() == 1);
		
	}

}