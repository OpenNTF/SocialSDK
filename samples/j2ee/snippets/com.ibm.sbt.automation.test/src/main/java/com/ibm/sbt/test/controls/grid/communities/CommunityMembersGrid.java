/*
 * � Copyright IBM Corp. 2012
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
package com.ibm.sbt.test.controls.grid.communities;

import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesGridTest;
import com.ibm.sbt.services.client.connections.communities.Member;

/**
 * @author Benjamin Jakobus
 * 
 * @date 24 Oct 2013
 */
public class CommunityMembersGrid extends BaseCommunitiesGridTest {

    public CommunityMembersGrid() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Test
    @Ignore
    public void testCreateCommunity() {
    	// Check grid
    	assertTrue("Expected the test to generate a grid", checkGrid("Social_Communities_Controls_Community_Members",true,true));
    }
    
    @Before
    public void initCommunity() {
    	addSnippetParam("sample.userId3", getCommunityUuid());	
    	try {
    		addMember(new Member(getCommunityService(), "sample.userId3"));  
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @After
    public void destroyCommunity() {
    	destroy();
    }
}
