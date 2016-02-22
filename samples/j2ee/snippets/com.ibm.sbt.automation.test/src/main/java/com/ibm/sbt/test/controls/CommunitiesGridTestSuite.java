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
package com.ibm.sbt.test.controls;



import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import java.util.ArrayList;

import com.ibm.sbt.automation.core.test.BaseGridTestSetup;
import com.ibm.sbt.test.controls.grid.communities.BootstrapCommunitiesGrid;
import com.ibm.sbt.test.controls.grid.communities.CommunityActionGrid;
import com.ibm.sbt.test.controls.grid.communities.CommunityMembersGrid;
import com.ibm.sbt.test.controls.grid.communities.CustomTemplateCommunity;
import com.ibm.sbt.test.controls.grid.communities.MyCommunitiesGrid;
import com.ibm.sbt.test.controls.grid.communities.OneClickToJoin;
import com.ibm.sbt.test.controls.grid.communities.PublicCommunitiesDijit;
import com.ibm.sbt.test.controls.grid.communities.PublicCommunitiesGrid;

/**
 * @author mwallace
 * 
 * @since 6 Mar 2013
 */
@RunWith(Suite.class)
@SuiteClasses({BootstrapCommunitiesGrid.class, CommunityMembersGrid.class, CommunityActionGrid.class, CustomTemplateCommunity.class, MyCommunitiesGrid.class,OneClickToJoin.class, PublicCommunitiesDijit.class,
        PublicCommunitiesGrid.class })
public class CommunitiesGridTestSuite {
	private BaseGridTestSetup setup ;
	 
	@Before
	public void setup(){
		setup = new BaseGridTestSetup();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("tag1"); 
		tags.add("tag2"); 
		setup.createCommunity("TestCommunity", "public", "content", tags, false);
	}
	
	@After
    public void cleanup() {
		setup.deleteCommunity();
    }
}
