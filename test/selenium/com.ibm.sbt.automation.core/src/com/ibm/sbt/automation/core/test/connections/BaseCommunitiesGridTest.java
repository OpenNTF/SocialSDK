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
package com.ibm.sbt.automation.core.test.connections;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.BaseGridTest;
import com.ibm.sbt.automation.core.test.pageobjects.GridPagerPage;
import com.ibm.sbt.automation.core.test.pageobjects.GridResultPage;
import com.ibm.sbt.automation.core.test.pageobjects.GridSorterPage;
import com.ibm.sbt.automation.core.test.pageobjects.ResultPage;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.Member;

public class BaseCommunitiesGridTest extends BaseGridTest {
	private BaseCommunitiesTest communitiesTest;
	
	public BaseCommunitiesGridTest() {
		communitiesTest = new BaseCommunitiesTest();
	}
	
	@Before
	public void init() {
		communitiesTest.createCommunity();
		
	}
	
	@After
	public void destroy() {
		communitiesTest.deleteCommunityAndQuit();
	}
	
	public String getCommunityUuid() {
		return communitiesTest.community.getCommunityUuid();
	}
	
	public void addMember(Member member) throws Exception {
		communitiesTest.community.addMember(member);  
	}
	
	public CommunityService getCommunityService() {
		return communitiesTest.communityService;
	}
}
