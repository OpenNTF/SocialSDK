package com.ibm.sbt.js.community;

import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.javascript.units.BaseRhinoTest;

import com.ibm.sbt.services.client.connections.communities.CommunityService;

public class CommunityTest extends BaseRhinoTest {

	@Test
	public void testEverythingWorks() throws Throwable {
		System.out.println(new CommunityService().getPublicCommunities()); 
	}

		@Test
	public void testPublicCommunity() throws Exception {
		String script;
		script = IOUtils.toString(getClass().getResourceAsStream(
				"TestCommunity.js"));
	
		executeScript(script, "test");
	}

	
}
