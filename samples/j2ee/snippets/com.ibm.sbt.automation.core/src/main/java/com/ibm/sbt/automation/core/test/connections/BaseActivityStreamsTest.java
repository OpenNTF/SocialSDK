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

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.test.FlexibleTest;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.services.client.SBTServiceException;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntity;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntityList;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamService;
import com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamServiceException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;
import com.ibm.sbt.test.lib.MockEndpoint;
/**
 * @author rajmeetbal
 *  
 * @date 10 May 2013
 */

public class BaseActivityStreamsTest extends FlexibleTest {
    
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
    
    protected void createNotification() throws Exception {
    	
    	//TODO move endpoint for secondary user to test environment
    	Endpoint ep = EndpointFactory.getEndpoint("connections");
    	assertTrue("Only basic endpoint supported for this test ",ep instanceof BasicEndpoint || ep instanceof MockEndpoint);
    	
    	try {
    	
    	((BasicEndpoint)ep).setUser(getEnvironment().getSecondaryUsername());
    	((BasicEndpoint)ep).setPassword(getEnvironment().getSecondaryUserPassword());
    	
    	ActivityStreamService svc = new ActivityStreamService(ep);
    	JsonJavaObject postPayload = new JsonJavaObject();
    	postPayload.put("content", "Notification message content");

		String entryID = svc.postMicroblogEntry(getEnvironment().getCurrentUserUuid(), null, null, postPayload);
		System.out.println("Created Notification Entry "+entryID); 
    	} finally {
    		((BasicEndpoint)ep).setUser(getEnvironment().getCurrentUsername());
    		((BasicEndpoint)ep).setPassword(getEnvironment().getCurrentUserPassword());
    	}

    }
    
    protected ActivityStreamEntity getLastCreatedEntry() {
        try {
            ActivityStreamService asService = getActivityStreamService();
            ActivityStreamEntityList entries = asService.getAllUpdates();
            asEntry = entries.iterator().next();
            Trace.log("Last created entry: "+asEntry.getId());
        } catch (SBTServiceException e) {
        	fail("SBTServiceException: " + e.getMessage());
            e.printStackTrace();
		}
        return asEntry;
    }
    
    /**
     * Save an AS entry so the list is not empty.
     * @throws ActivityStreamServiceException 
     */
    protected void saveEntry() throws ActivityStreamServiceException{
    	if(activityStreamService == null){
    		activityStreamService = new ActivityStreamService();
    	}
    	String eventId = null;
    	Map<String, String> params = new HashMap<String, String>();
    	params.put("rollup", "true");
    	ActivityStreamEntityList list = activityStreamService.getStream("@me", "@all", "@all", params);
		for(int i = 0; i < list.size(); i++){
			eventId = list.get(i).getEventId();
			if(eventId != null){
				break;
			}
		}
		
		activityStreamService.saveEntry(eventId);
    }

    protected void createEntry(String userType, String groupType, String applicationType) {
    	createEntry(userType, groupType, applicationType, false, false);
    }    
    protected void createEntry(String userType, String groupType, String applicationType, boolean actionable, boolean saved) {
    	JsonJavaObject postPayload = new JsonJavaObject();
    	
		JsonJavaObject actor = new JsonJavaObject();
        actor.putString("id", "@me");

		JsonJavaObject object = new JsonJavaObject();
		object.putString("id", "objectid");
		object.putString("displayName", "Display Name for Misscrblogging sameple");
		postPayload.putObject("actor", actor);
		postPayload.putString("verb", "@invite");
		postPayload.putObject("object", object);
		if (actionable || saved) {
			JsonJavaObject connections = new JsonJavaObject();
			if (actionable) connections.put("actionable", "true");
			if (saved) connections.put("saved", "true");
			postPayload.put("connections", connections);
		}
		
		ActivityStreamService service = new ActivityStreamService();
		try {
			String entryID = service.postEntry(userType, groupType, applicationType, postPayload);
			System.out.println("Created Entry "+entryID);
			
		} catch (SBTServiceException e) {
			fail("SBTServiceException: " + e.getMessage());
		}
    }
    
    protected void createCommunityWithTags(String tags) {
    	CommunityService communityService = new CommunityService();
    	Community community = null;
    	try{
			community = new Community(communityService, "");
			community.setTitle(this.getClass().getName() + "#" + this.hashCode() + " Community - " + System.currentTimeMillis());
			community.setContent("Test Community created to test Activity Streams");
			community.setTags(tags);
			String communityId = communityService.createCommunity(community);
    	}catch(CommunityServiceException cse){
    		assertNull("CommunityServiceException in testing SearchByFilters");
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
			fail("SBTServiceException: " + e.getMessage());
		}
    }

}
