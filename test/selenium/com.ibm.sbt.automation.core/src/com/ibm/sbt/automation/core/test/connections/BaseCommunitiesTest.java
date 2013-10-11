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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.automation.core.test.BaseApiTest;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.communities.Member;
import com.ibm.sbt.services.client.connections.communities.MemberList;
import com.ibm.sbt.services.client.connections.forums.ForumServiceException;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;
import com.ibm.sbt.services.client.connections.forums.ForumType;
import com.ibm.sbt.services.client.connections.forums.TopicList;
import com.ibm.sbt.services.client.connections.forums.feedhandler.TopicsFeedHandler;
import com.ibm.sbt.services.client.connections.forums.transformers.BaseForumTransformer;



/**
 * @author mwallace
 *  
 * @date 13 Mar 2013
 */
public class BaseCommunitiesTest extends BaseApiTest {
    
    protected boolean createCommunity = true;
    protected CommunityService communityService;
    protected Community community;

    public BaseCommunitiesTest() {
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
        	//System.out.println(name);
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
    
    protected String createCommunityName() {
    	return this.getClass().getName() + "#" + this.hashCode() + " Community - " + System.currentTimeMillis();
    }
    
    protected CommunityService getCommunityService() {
        if (communityService == null) {
            communityService = new CommunityService(getEndpointName());
        }
        return communityService;
    }
    
    protected void assertCommunityValid(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(community.getCommunityUuid(), json.getString("getCommunityUuid"));
        Assert.assertEquals(community.getTitle(), json.getString("getTitle"));
        Assert.assertEquals(community.getSummary(), json.getString("getSummary"));
        Assert.assertEquals(community.getContent(), json.getString("getContent"));
        Assert.assertEquals(community.getCommunityUrl(), json.getString("getCommunityUrl"));
        Assert.assertEquals(community.getLogoUrl(), json.getString("getLogoUrl"));
        Assert.assertEquals(community.getMemberCount(), json.getInt("getMemberCount"));
        Assert.assertEquals(community.getCommunityType(), json.getString("getCommunityType"));
        Assert.assertEquals(community.getAuthor().getName(), json.getJsonObject("getAuthor").getString("authorName"));
        Assert.assertEquals(community.getAuthor().getEmail(), json.getJsonObject("getAuthor").getString("authorEmail"));
        Assert.assertEquals(community.getAuthor().getUserid(), json.getJsonObject("getAuthor").getString("authorUserid"));
        Assert.assertEquals(community.getContributor().getName(), json.getJsonObject("getContributor").getString("contributorName"));
        Assert.assertEquals(community.getContributor().getEmail(), json.getJsonObject("getContributor").getString("contributorEmail"));
        Assert.assertEquals(community.getContributor().getUserid(), json.getJsonObject("getContributor").getString("contributorUserid"));
    }
    
    protected void assertMemberValid(JsonJavaObject json, String communityUuid, String name, String userid, String email, String role) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(communityUuid, json.getString("getCommunityUuid"));
        Assert.assertEquals(name, json.getString("getName"));
        Assert.assertEquals(userid, json.getString("getUserid"));
        if (!environment.isSmartCloud()) {
        	Assert.assertTrue("Expect match "+email+" <> "+json.getString("getEmail"), email.equalsIgnoreCase(json.getString("getEmail")));
        }
        Assert.assertEquals(role, json.getString("getRole"));
    }
    
    protected Community getLastCreatedCommunity() {
        Community community = null;
        try {
            loginConnections();
            
            CommunityService communityService = getCommunityService();
            Collection<Community> communities = communityService.getMyCommunities();
            community = communities.iterator().next();
            Trace.log("Last created community: "+community.getCommunityUuid());
            Trace.log("Last created community: "+community.getPublished());
            Iterator<Community> i = communities.iterator();
            while (i.hasNext()) {
            	Community c= i.next();
            	Trace.log("Last created community: "+c.getCommunityUuid());
            	Trace.log("Last created community: "+c.getTitle());
            	Trace.log("Last created community: "+c.getPublished());
            }
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (CommunityServiceException cse) {
            fail("Error getting last created community", cse);
        } 
        
        return community;
    }
    
    protected Community getCommunity(String communityUuid) {
        return getCommunity(communityUuid, true);
    }
    
    protected Community getCommunity(String communityUuid, boolean failOnCse) {
        Community community = null;
        try {
            loginConnections();
            
            CommunityService communityService = getCommunityService();
            community = communityService.getCommunity(communityUuid);
            Trace.log("Got community: "+community.getCommunityUuid());
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (CommunityServiceException cse) {
        	if (failOnCse) {
        		fail("Error retrieving community", cse);
        	}
        } 
        return community;
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
    
    protected boolean addMember(Community community, String id, String role) {
        try {
            CommunityService communityService = getCommunityService();
            Member member = new Member(communityService, id);
            member.setRole(role);
            boolean added = communityService.addMember(community.getCommunityUuid(), member);
            Assert.assertTrue("Unable to add member: "+id, added);
            Trace.log("Added member: "+id);
            return added;
        } catch (CommunityServiceException cse) {
            fail("Error adding member", cse);
        } 
        return false;
    }

    protected boolean hasMember(Community community, String id) {
        try {
            CommunityService communityService = getCommunityService();
            MemberList memberList = communityService.getMembers(community.getCommunityUuid());
            for (int i=0; i<memberList.size(); i++) {
            	Member member = (Member)memberList.get(i);
                if (id.equals(member.getEmail()) || id.equals(member.getUserid())) {
                    return true;
                }
            }
        } catch (CommunityServiceException cse) {
            fail("Error getting members", cse);
        } 
        return false;
    }
    
	protected ForumTopic createForumTopic(Community community, ForumTopic topic) throws ForumServiceException {
		if (null == topic){
			throw new ForumServiceException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			CommunityService communityService = getCommunityService();
			TopicList topicList = communityService.getForumTopics(community.getCommunityUuid());
						
			String forumUuid = "";
			
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("communityUuid", community.getCommunityUuid());
			
			Map<String, String> headers = new HashMap<String, String>();
			headers.put("Content-Type", "application/atom+xml");
			
			//String url = resolveUrl(ForumType.TOPICS,null,params);
			//result = createData(url, null, headers,payload);
			//topic = (ForumTopic) new TopicsFeedHandler(this).createEntity(result);

		} catch (Exception e) {
			throw new ForumServiceException(e, "error creating forum");
		}

        return topic;
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