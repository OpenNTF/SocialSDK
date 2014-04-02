package com.ibm.sbt.automation.core.test;

import org.junit.Assert;
import org.openqa.selenium.WebElement;

import com.ibm.sbt.automation.core.test.pageobjects.WrapperResultPage;
import com.ibm.sbt.services.client.connections.communities.Community;
/*
 * © Copyright IBM Corp. 2012
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
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;

/**
 * @author Francis 
 * @date 26 Mar 2013
 */
public class BaseActivityStreamTest extends BaseWrapperTest{

	private Community community;
	private CommunityService communityService;
	
    /**
     * creates community, initialising context if needed.
     * @return
     * @throws CommunityServiceException
     */
	public String createCommunity(){
		createContext();
		if(communityService == null){
			communityService = new CommunityService(this.getTestEnvironment().getEndpointName());
		}
		
		String communityUuid = null;;
		if(community == null){
			try {
				communityUuid = communityService.createCommunity("TestTitle" + System.currentTimeMillis(), "Test content.", "public");
				community = communityService.getCommunity(communityUuid);
			} catch (CommunityServiceException e) {
				e.printStackTrace();
				Assert.fail("Problem creating test community.");
			}
		} else{
			communityUuid = community.getCommunityUuid();
		}
		
		return communityUuid;
	}
	
	public void destroyCommunity() {
		try {
			communityService.deleteCommunity(community.getCommunityUuid());
		} catch (CommunityServiceException e) {
			e.printStackTrace();
		}
		destroyContext();
	}
	
	/**
     * Default constructor
     */
    public BaseActivityStreamTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
	
	/**
	 * Check if the ActivityStream is displayed on the page
	 * @param snippetId
	 * @return true if displayed
	 */
	protected boolean checkActivityStream(String snippetId) {
		WrapperResultPage resultPage = launchSnippet(snippetId);
		
		WebElement activityStreamFrame = resultPage.getActivityStreamFrame();
		
		// switch context into the iframe.
		switchContextToIframe(resultPage, activityStreamFrame);
		
	    WebElement activityStream = resultPage.getActivityStream();
		
		WebElement newsFeedNode = resultPage.getNewsFeedNode();
		
		return (activityStream!=null) && (newsFeedNode!=null);
	}
	
	@Override
    protected boolean isEnvironmentValid() {
	    // don't run the test in jquery
        return super.isEnvironmentValid() && !environment.isLibrary("jquery");
    }
}
