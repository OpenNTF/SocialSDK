/**
 * 
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
import com.ibm.sbt.automation.core.test.BaseTest.AuthType;
import com.ibm.sbt.automation.core.utils.Trace;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.Response;
import com.ibm.sbt.services.client.connections.forums.Forum;
import com.ibm.sbt.services.client.connections.forums.ForumList;
import com.ibm.sbt.services.client.connections.forums.ForumService;
import com.ibm.sbt.services.client.connections.forums.ForumServiceException;
import com.ibm.sbt.services.client.connections.forums.ForumTopic;
import com.ibm.sbt.services.client.connections.forums.TopicList;
import com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity;
import com.ibm.sbt.services.client.connections.forums.transformers.BaseForumTransformer;

/**
 * @author mwallace
 *
 */
public class BaseForumsTest extends BaseApiTest {
	
	protected boolean createForum = true;
    protected ForumService forumService;
    protected Forum forum;

    public BaseForumsTest() {
        setAuthType(AuthType.AUTO_DETECT);
    }
    
    @Before
    public void createForum() {
        createContext();
        if (createForum) {
        	String type = "public";
        	if (environment.isSmartCloud()) {
        		type = "private";
        	}
        	String name = createForumName();
        	//System.out.println(name);
            forum = createForum(name, type, name, "tag1,tag2,tag3");
        }
    }
    
    @After
    public void deleteForumAndQuit() {
    	//deleteForum(forum);
    	forum = null;
    	destroyContext();
    }
    
    protected String createForumName() {
    	return this.getClass().getName() + "#" + this.hashCode() + " Forum - " + System.currentTimeMillis();
    }
    
    protected ForumService getForumService() {
        if (forumService == null) {
            forumService = new ForumService(getEndpointName());
        }
        return forumService;
    }
    
    protected void assertForumValid(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(forum.getForumUuid(), json.getString("getForumUuid"));
        Assert.assertEquals(forum.getTitle(), json.getString("getTitle"));
        //Assert.assertEquals(forum.getSummary(), json.getString("getSummary"));
        Assert.assertEquals(forum.getContent(), json.getString("getContent"));
        Assert.assertEquals(forum.getForumUrl(), json.getString("getForumUrl"));
        Assert.assertEquals(forum.getAuthor().getName(), json.getJsonObject("getAuthor").getString("name"));
        Assert.assertEquals(forum.getAuthor().getEmail(), json.getJsonObject("getAuthor").getString("email"));
        Assert.assertEquals(forum.getAuthor().getUserid(), json.getJsonObject("getAuthor").getString("userid"));
        //Assert.assertEquals(forum.getContributor().getName(), json.getJsonObject("getContributor").getString("name"));
        //Assert.assertEquals(forum.getContributor().getEmail(), json.getJsonObject("getContributor").getString("email"));
        //Assert.assertEquals(forum.getContributor().getUserid(), json.getJsonObject("getContributor").getString("userid"));
    }
    
    protected void assertForumGetters(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertNotNull(json.getString("getForumUuid"));
        Assert.assertNotNull(json.getString("getTitle"));
        //Assert.assertNotNull(json.getString("getSummary"));
        Assert.assertNotNull(json.getString("getContent"));
        Assert.assertNotNull(json.getString("getForumUrl"));
        Assert.assertNotNull(json.getJsonObject("getAuthor").getString("name"));
        Assert.assertNotNull(json.getJsonObject("getAuthor").getString("email"));
        Assert.assertNotNull(json.getJsonObject("getAuthor").getString("userid"));
        //Assert.assertNotNull(json.getJsonObject("getContributor").getString("name"));
        //Assert.assertNotNull(json.getJsonObject("getContributor").getString("email"));
        //Assert.assertNotNull(json.getJsonObject("getContributor").getString("userid"));
    }
    
    protected void assertForumProperties(JsonJavaObject json) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertNotNull(json.getString("uid"));
        Assert.assertNotNull(json.getString("title"));
        Assert.assertNotNull(json.getString("updated"));
        Assert.assertNotNull(json.getString("published"));
        Assert.assertNotNull(json.getString("authorName"));
        Assert.assertNotNull(json.getString("authorEmail"));
        Assert.assertNotNull(json.getString("authorUserid"));
        Assert.assertNotNull(json.getString("authorUserState"));
        Assert.assertNotNull(json.getString("content"));
        Assert.assertNotNull(json.getString("categoryTerm"));
        Assert.assertNotNull(json.getString("editUrl"));
        Assert.assertNotNull(json.getString("selfUrl"));
        Assert.assertNotNull(json.getString("alternateUrl"));
        Assert.assertNotNull(json.getString("forumUuid"));
        Assert.assertNotNull(json.getString("moderation"));
        Assert.assertNotNull(json.getString("threadCount"));
        Assert.assertNotNull(json.getString("forumUrl"));
    }
    
    protected void assertMemberValid(JsonJavaObject json, String forumUuid, String name, String userid, String email, String role) {
        Assert.assertNull("Unexpected error detected on page", json.getString("code"));
        Assert.assertEquals(forumUuid, json.getString("getForumUuid"));
        Assert.assertEquals(name, json.getString("getName"));
        Assert.assertEquals(userid, json.getString("getUserid"));
        if (!environment.isSmartCloud()) {
        	Assert.assertTrue("Expect match "+email+" <> "+json.getString("getEmail"), email.equalsIgnoreCase(json.getString("getEmail")));
        }
        Assert.assertEquals(role, json.getString("getRole"));
    }
    
    protected Forum getLastCreatedForum() {
        Forum forum = null;
        try {
            loginConnections();
            
            ForumService forumService = getForumService();
            ForumList forums = forumService.getMyForums();
            forum = (Forum)forums.iterator().next();
            Trace.log("Last created forum: "+forum.getForumUuid());
            Trace.log("Last created forum: "+forum.getPublished());
            Iterator<BaseForumEntity> i = forums.iterator();
            while (i.hasNext()) {
            	BaseForumEntity c= i.next();
            	Trace.log("Last created forum: "+((Forum)c).getForumUuid());
            	Trace.log("Last created forum: "+c.getTitle());
            	Trace.log("Last created forum: "+c.getPublished());
            }
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (ForumServiceException cse) {
            fail("Error getting last created forum", cse);
        } 
        
        return forum;
    }
    
    protected Forum getForum(String forumUuid) {
        return getForum(forumUuid, true);
    }
    
    protected Forum getForum(String forumUuid, boolean failOnCse) {
        Forum forum = null;
        try {
            loginConnections();
            
            ForumService forumService = getForumService();
            forum = forumService.getForum(forumUuid);
            Trace.log("Got forum: "+forum.getForumUuid());
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (ForumServiceException cse) {
        	if (failOnCse) {
        		fail("Error retrieving forum", cse);
        	}
        } 
        return forum;
    }
    
    protected Forum createForum(String title, String type, String content, String tags) {
    	return createForum(title, type, content, tags, true);
    }
    
    protected Forum createForum(String title, String type, String content, String tags, boolean retry) {
        Forum forum = null;
        try {
            loginConnections();
            ForumService forumService = getForumService();
            
        	long start = System.currentTimeMillis();
            forum = new Forum(forumService, "");
            forum.setTitle(title);
            forum.setContent(content);
            forum.setTags(tags);
            forum = forumService.createForum(forum);
            forum = forumService.getForum(forum.getForumUuid());
            
            long duration = System.currentTimeMillis() - start;
            Trace.log("Created test forum: "+forum.getForumUuid() + " took "+duration+"(ms)");
        } catch (AuthenticationException pe) {
        	if (pe.getCause() != null) {
        		pe.getCause().printStackTrace();
        	}
            Assert.fail("Error authenicating: " + pe.getMessage());
        } catch (ForumServiceException cse) {
        	// TODO remove this when we upgrade the QSI
        	Throwable t = cse.getCause();
        	if (t instanceof ClientServicesException) {
        		ClientServicesException csex = (ClientServicesException)t;
        		int statusCode = csex.getResponseStatusCode();
        		if (statusCode == 500 && retry) {
        			return createForum(title + " (retry)", type, content, tags, false);
        		}
        	}
            fail("Error creating test forum with title: '"+title+"'", cse);
        } 
        
        return forum;
    }

    protected void deleteForum(Forum forum) {
        if (forum != null) {
            try {
            	loginConnections();
                ForumService forumService = getForumService();
                // TODO should be deleteForum
                forumService.removeForum(forum.getForumUuid());
            } catch (AuthenticationException pe) {
            	if (pe.getCause() != null) {
            		pe.getCause().printStackTrace();
            	}
                Assert.fail("Error authenicating: " + pe.getMessage());
            } catch (ForumServiceException cse) {
                forum = null;
            	// check if forum delete failed because
            	// forum was already deleted
            	Throwable t = cse.getCause();
            	if (t instanceof ClientServicesException) {
            		ClientServicesException csex = (ClientServicesException)t;
            		int statusCode = csex.getResponseStatusCode();
            		if (statusCode == 404) {
            			Trace.log(this.getClass().getName() + " attempt to delete already deleted Forum: " + csex.getLocalizedMessage());
            			return;
            		}
            	}
                fail("Error deleting forum "+forum, cse);
            }
        }
    }
    
    protected void deleteForum(String forumId) {
        if (forumId != null) {
            try {
            	loginConnections();
                ForumService forumService = getForumService();
                forumService.removeForum(forumId);
            } catch (AuthenticationException pe) {
            	if (pe.getCause() != null) {
            		pe.getCause().printStackTrace();
            	}
                Assert.fail("Error authenicating: " + pe.getMessage());
            } catch (ForumServiceException cse) {
                fail("Error deleting forum "+forumId, cse);
            }
        }
    }
    
	protected ForumTopic createForumTopic(Forum forum, ForumTopic topic) throws ForumServiceException {
		if (null == topic){
			throw new ForumServiceException(null,"Topic object passed was null");
		}
		Response result = null;
		try {
			ForumService forumService = getForumService();
			TopicList topicList = forumService.getForumTopics(forum.getForumUuid());
						
			String forumUuid = "";
			
			BaseForumTransformer transformer = new BaseForumTransformer(topic);
			Object 	payload = transformer.transform(topic.getFieldsMap());
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("forumUuid", forum.getForumUuid());
			
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
        
    protected void fail(String message, ForumServiceException cse) {
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
