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

import org.junit.After;
import org.junit.Before;

import junit.framework.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
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
    protected boolean createCommunity = true;
    protected CommunityService communityService;
    protected Community community;

    public BaseActivityStreamsTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Before
    public void createCommunity() {
        createContext();
        if (createCommunity) {
        	String type = "public";
        	if (environment.isSmartCloud()) {
        		type = "private";
        	}
        	String name = createCommunityName();
            community = createCommunity(name, type, name, "tag1,tag2,tag3");
        }
    }
    
    @After
    public void deleteCommunityAndQuit() {
    	deleteCommunity(community);
    	community = null;
    	destroyContext();
    	
    	if (environment.isDebugTransport()) {
    		saveTestDataAndResults();
    	}
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
    
    protected String createCommunityName() {
    	return this.getClass().getName() + "#" + this.hashCode() + " Community - " + System.currentTimeMillis();
    }
    
    protected Community createCommunity(String title, String type, String content, String tags) {
    	return createCommunity(title, type, content, tags, true);
    }
    
    protected Community createCommunity(String title, String type, String content, String tags, boolean retry) {
        Community community = null;
        try {
            loginConnections();
            CommunityService communityService = getCommunityService();
            
        	long start = System.currentTimeMillis();
            community = new Community(communityService, "");
            community.setTitle(title);
            community.setCommunityType(type);
            community.setContent(content);
            community.setTags(tags);
            String communityUuid = communityService.createCommunity(community);
            community = communityService.getCommunity(communityUuid);
            
            long duration = System.currentTimeMillis() - start;
            Trace.log("Created test community: "+communityUuid + " took "+duration+"(ms)");
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (CommunityServiceException cse) {
        	// TODO remove this when we upgrade the QSI
        	Throwable t = cse.getCause();
        	if (t instanceof ClientServicesException) {
        		ClientServicesException csex = (ClientServicesException)t;
        		int statusCode = csex.getResponseStatusCode();
        		if (statusCode == 500 && retry) {
        			return createCommunity(title + " (retry)", type, content, tags, false);
        		}
        	}
            fail("Error creating test community with title: '"+title+"'", cse);
        } 
        
        return community;
    }

    protected void deleteCommunity(Community community) {
        if (community != null) {
            try {
            	loginConnections();
                CommunityService communityService = getCommunityService();
                communityService.deleteCommunity(community.getCommunityUuid());
            } catch (AuthenticationException pe) {
            	if (pe.getCause() != null) {
            		pe.getCause().printStackTrace();
            	}
                Assert.fail("Error authenicating: " + pe.getMessage());
            } catch (CommunityServiceException cse) {
                community = null;
            	// check if community delete failed because
            	// community was already deleted
            	Throwable t = cse.getCause();
            	if (t instanceof ClientServicesException) {
            		ClientServicesException csex = (ClientServicesException)t;
            		int statusCode = csex.getResponseStatusCode();
            		if (statusCode == 404) {
            			Trace.log(this.getClass().getName() + " attempt to delete already deleted Community: " + csex.getLocalizedMessage());
            			return;
            		}
            	}
                fail("Error deleting community "+community, cse);
            }
        }
    }
    
    protected void deleteCommunity(String communityId) {
        if (communityId != null) {
            try {
            	loginConnections();
                CommunityService communityService = getCommunityService();
                communityService.deleteCommunity(communityId);
            } catch (AuthenticationException pe) {
            	if (pe.getCause() != null) {
            		pe.getCause().printStackTrace();
            	}
                Assert.fail("Error authenicating: " + pe.getMessage());
            } catch (CommunityServiceException cse) {
                fail("Error deleting community "+communityId, cse);
            }
        }
    }
    
    protected CommunityService getCommunityService() {
        if (communityService == null) {
            communityService = new CommunityService(getEndpointName());
        }
        return communityService;
    }
    
    protected void fail(String message, CommunityServiceException cse) {
    	String failure = message;
    	
    	Throwable cause = cse.getCause();
    	if (cause != null) {
    		cause.printStackTrace();
    		failure += ", " + cause.getMessage();
    	} else {
    		cse.printStackTrace();
    		failure += ", " + cse.getMessage();
    	}
    	
    	Assert.fail(failure);
    }


}
