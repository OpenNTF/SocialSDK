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
package com.ibm.sbt.automation.core.test.connections;

import java.util.List;

import junit.framework.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntityList;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamService;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntity;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;

/**
 * @author rajmeetbal
 *  
 * @date 10 May 2013
 */

public class BaseActivityStreamsTest extends BaseApiTest {
    
    protected ActivityStreamService activityStreamService;
    protected ActivityStreamEntity asEntry;
    protected boolean postEntry = true;

    public BaseActivityStreamsTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    protected ActivityStreamService getActivityStreamService() {
        if (activityStreamService == null) {
            activityStreamService = new ActivityStreamService();
        }
        return activityStreamService;
    }
    
    protected ActivityStreamEntity getLastCreatedEntry() {
        try {
            loginConnections();
            ActivityStreamService asService = getActivityStreamService();
            ActivityStreamEntityList entries = asService.getAllUpdates();
            asEntry = entries.iterator().next();
            Trace.log("Last created entry: "+asEntry.getId());
        } catch (AuthenticationException pe) {
            Assert.fail("Error authenicating: " + pe.getMessage());
            pe.printStackTrace();
        } catch (SBTServiceException e) {
        	Assert.fail("SBTServiceException: " + e.getMessage());
            e.printStackTrace();
		}
        return asEntry;
    }

    
    protected void createEntry(String userType, String groupType, String applicationType) {
    	createContext();
    	try {
			loginConnections();
		} catch (AuthenticationException e1) {
			Assert.fail("AuthenticationException: "+e1.getMessage());
		}
    	JsonJavaObject postPayload = new JsonJavaObject();
    	
		JsonJavaObject actor = new JsonJavaObject();
        actor.putString("id", "@me");

		JsonJavaObject object = new JsonJavaObject();
		object.putString("id", "objectid");
		object.putString("displayName", "Display Name for Misscrblogging sameple");
		postPayload.putObject("actor", actor);
		postPayload.putString("verb", "@invite");
		postPayload.putObject("object", object);

		ActivityStreamService service = new ActivityStreamService();
		try {
			service.postEntry(userType, groupType, applicationType, postPayload);
		} catch (SBTServiceException e) {
			Assert.fail("SBTServiceException: " + e.getMessage());
		}
    }
    
    protected void createCommunityWithTags(String tags) {
    	createContext();
    	CommunityService communityService = new CommunityService();
    	Community community = null;
    	try{
			community = new Community(communityService, "");
			community.setTitle(this.getClass().getName() + "#" + this.hashCode() + " Community - " + System.currentTimeMillis());
			community.setContent("Test Community created to test Activity Streams");
			community.setTags(tags);
			String communityId = communityService.createCommunity(community);
    	}catch(CommunityServiceException cse){
    		Assert.assertNull("CommunityServiceException in testing SearchByFilters");
    	}
    }
    
    protected String getLastEntryId(List jsonList){
    	JsonJavaObject jjo = null;
    	String id = null;
    	if(jsonList.iterator().hasNext()){
    		jjo = (JsonJavaObject)jsonList.iterator().next();
    		id = jjo.getJsonObject("dataHandler").getJsonObject("data").getJsonObject("object").getString("id");
    	}
    	return id;
    }
    
    protected boolean isLatestEntryFound(List jsonList, String latestEntry){
    	JsonJavaObject jjo = null;
    	String id = null;
    	while(jsonList.iterator().hasNext()){
    		jjo = (JsonJavaObject)jsonList.iterator().next();
    		id = jjo.getJsonObject("dataHandler").getJsonObject("data").getJsonObject("object").getString("id");
    		if(latestEntry.equals(id)){
    			return true;
    		}
    	}
    	return false;
    }
    
    protected void postAStatusUpdate() {
    	createContext();
    	try {
			loginConnections();
		} catch (AuthenticationException e1) {
			Assert.fail("AuthenticationException: "+e1.getMessage());
		}
    	JsonJavaObject postPayload = new JsonJavaObject();
    	
		JsonJavaObject actor = new JsonJavaObject();
        actor.putString("id", "@me");

		JsonJavaObject object = new JsonJavaObject();
		object.putString("id", "objectid");
		object.putString("displayName", "Display Name for Misscrblogging sameple");
		postPayload.putObject("actor", actor);
		postPayload.putString("verb", "@invite");
		postPayload.putObject("object", object);

		ActivityStreamService service = new ActivityStreamService();
		try {
			service.postEntry("@me", "@status", "@all", postPayload);
		} catch (SBTServiceException e) {
			Assert.fail("SBTServiceException: " + e.getMessage());
		}
    }

}
