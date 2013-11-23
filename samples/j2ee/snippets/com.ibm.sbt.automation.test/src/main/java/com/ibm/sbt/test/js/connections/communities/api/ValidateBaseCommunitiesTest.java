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
package com.ibm.sbt.test.js.connections.communities.api;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest;
import com.ibm.sbt.automation.core.test.pageobjects.JavaScriptPreviewPage;
import com.ibm.sbt.services.client.connections.communities.Community;

/**
 * @author mwallace
 *  
 * @date 29 Jul 2013
 */
public class ValidateBaseCommunitiesTest extends BaseCommunitiesTest {
	
	private int index = 0;
    
    @Test
    public void testCreateCommunity1() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity2() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity3() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity4() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity5() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity6() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity7() {
    	testCreateCommunity();
    }
    
    @Test
    public void testCreateCommunity8() {
    	testCreateCommunity();
    }
    
    public void testCreateCommunity() {
    	Community comm = getCommunity(community.getCommunityUuid(), false);
    	Assert.assertNotNull("Create community failed", comm);
    	deleteCommunity(comm);
    	comm = getCommunity(comm.getCommunityUuid(), false);
    	Assert.assertNull("Delete community failed", comm);
    	community = null;
    }
    
    /* (non-Javadoc)
     * @see com.ibm.sbt.automation.core.test.connections.BaseCommunitiesTest#createCommunityName()
     */
    @Override
    protected String createCommunityName() {
    	return super.createCommunityName() + " - " + (++index);
    }
}
