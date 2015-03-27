/*
 * © Copyright IBM Corp. 2014
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
package com.ibm.sbt.services.client.connections.activities;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.base.Version;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class BaseActivityServiceTest extends BaseUnitTest {

	private BasicEndpoint basicEndpoint;
	private BasicEndpoint altEndpoint;
	protected ActivityService activityService;
	protected Activity activity;
	protected List<Activity> activities;
	
	static protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	
	@Before 
    public void override(){
        try {
            if(Class.forName("javax.mail.BodyPart") != null){
                System.out.println("javax.mail.BodyPart exists");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Short Circuiting Tests");
            Assume.assumeTrue("Short Circuit", true);
        }
    }
	
	@Before
	public void initActivityServiceTest() {
		
		
		
		if (activityService==null) {
			BasicEndpoint endpoint = getBasicEndpoint();
			activityService = (endpoint == null) ? new ActivityService() : new ActivityService(endpoint);
		}
	}
	
	@After
	public void termActivityServiceTest() {
		if (activity != null) {
			try {
				activityService.deleteActivity(activity);
			} catch (Exception e) {
			}
		}
		if (activities != null) {
			try {
				deleteActivities(activities);
			} catch (Exception e) {
			}
		}
	}
	
	protected boolean isV5OrHigher() {
		Version version = activityService.getApiVersion();
		return version.isAtLeast(5);
	}
	
    /**
	 * @return the basicEndpoint
	 */
	protected BasicEndpoint getBasicEndpoint() {
    	if (basicEndpoint == null && System.getProperty("ServerUrl") != null) {
	    	String url = System.getProperty("ServerUrl");
	    	String user = System.getProperty("UserName");
	    	String password = System.getProperty("Password");
	    	
			basicEndpoint = new BasicEndpoint();
			basicEndpoint.setUrl(url);
			basicEndpoint.setForceTrustSSLCertificate(true);
			basicEndpoint.setUser(user);
			basicEndpoint.setPassword(password);
			
			// enable to reuse cookies
			//basicEndpoint.setClientServiceClass("com.ibm.sbt.services.client.CookieStoreClientService");
			
			// enable to use a proxy
			//basicEndpoint.setHttpProxy("localhost:8888");
			//basicEndpoint.setUseProxy(true);			
    	}
    	return basicEndpoint;
	}
	
	protected BasicEndpoint getAltEndpoint() {
    	if (altEndpoint == null && System.getProperty("ServerUrl") != null) {
	    	String url = System.getProperty("ServerUrl");
	    	String user = System.getProperty("UserNameAlt");
	    	String password = System.getProperty("PasswordAlt");
	    	
	    	altEndpoint = new BasicEndpoint();
	    	altEndpoint.setUrl(url);
	    	altEndpoint.setForceTrustSSLCertificate(true);
	    	altEndpoint.setUser(user);
	    	altEndpoint.setPassword(password);
    	}
    	return altEndpoint;
	}

	protected String getMemberId() {
		String memberId = System.getProperty("MemberId");
		if (StringUtil.isEmpty(memberId)) {
			memberId = TestEnvironment.getSecondaryUserUuid();
		}
		return memberId;
	}
	
	protected String getMemberEmail() {
		String memberId = System.getProperty("MemberEmail");
		if (StringUtil.isEmpty(memberId)) {
			memberId = TestEnvironment.getSecondaryUserEmail();
		}
		return memberId;
	}
	
	protected String getMemberName() {
		String memberId = System.getProperty("MemberName");
		if (StringUtil.isEmpty(memberId)) {
			memberId = TestEnvironment.getSecondaryUserDisplayName();
		}
		return memberId;
	}
	
	protected Node readXml(String fileName) throws IOException, XMLException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("com/ibm/sbt/services/client/connections/activities/"+fileName);
		return DOMUtil.createDocument(inputStream);
	}
		
	protected InputStream readFile(String fileName) throws IOException, XMLException {
		return this.getClass().getClassLoader().getResourceAsStream("com/ibm/sbt/services/client/connections/activities/"+fileName);
	}
		
    protected String createActivityTitle() {
    	return createTitle("Activity");
    }
    
    protected String createActivityNodeTitle() {
    	return createTitle("ActivityNode");
    }
    
    protected String createTitle(String type) {
    	return this.getClass().getName() + "_" + type + "-" + System.currentTimeMillis();
    }
    
    protected Activity createActivity() throws ClientServicesException {
		return createActivity(createActivityTitle());
    }
    
    protected Activity createActivity(String title) throws ClientServicesException {
		return createActivity(title, Activity.PRIORITY_NORMAL);
    }
        	
    protected Activity createActivity(String title, int priority) throws ClientServicesException {
		return createActivity(title, priority, null);
    }
        	
    protected Activity createActivity(String title, int priority, Field[] fields) throws ClientServicesException {
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	
		activity = new Activity();
		activity.setTitle(title);
		activity.setTags(tags);
		activity.setPriority(priority);
		activity.setSummary("Goal for " + title);
		
		if (fields != null) {
			for (Field field : fields) {
				activity.addField(field);
			}
		}
		
		return activityService.createActivity(activity);
    }
        	
    protected Activity createActivity(String title, String type) throws ClientServicesException {
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	
		activity = new Activity();
		activity.setTitle(title);
		activity.setTags(tags);
		activity.setPriority(Activity.PRIORITY_NORMAL);
		activity.setType(type);
		activity.setSummary("Goal for " + title);
		
		return activityService.createActivity(activity);
    }
        	
    protected Activity createActivity(String title, List<String> tags) throws ClientServicesException {
		activity = new Activity();
		activity.setTitle(title);
		activity.setTags(tags);
		activity.setPriority(Activity.PRIORITY_NORMAL);
		activity.setSummary("Goal for " + title);
		
		return activityService.createActivity(activity);
    }
        	
    protected Activity createActivity(String title, List<String> tags, boolean completed) throws ClientServicesException {
		activity = new Activity();
		activity.setTitle(title);
		activity.setTags(tags);
		activity.setPriority(Activity.PRIORITY_NORMAL);
		activity.setSummary("Goal for " + title);
		activity.setCompleted(completed);
		
		return activityService.createActivity(activity);
    }
        	
    protected ActivityNode createActivityNode() throws ClientServicesException {
		return createActivityNode(createActivityNodeTitle());
    }
        	
    protected ActivityNode createActivityNode(String title) throws ClientServicesException {
		activity = new Activity();
		activity.setTitle(createActivityTitle());
		activity = activityService.createActivity(activity);
		
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activity.getActivityUuid());
		activityNode.setTitle(title);
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		return activityService.createActivityNode(activityNode);
    }
        	
    protected ActivityNode createActivityNode(String activityUuid, String title) throws ClientServicesException {
    	List<String> tags = new ArrayList<String>();
    	tags.add("personal");
    	tags.add("unit_test");
    	tags.add("ibmsbt");
    	
		return createActivityNode(activityUuid, title, tags);
    }
        	
    protected ActivityNode createActivityNode(String activityUuid, String title, List<String> tags) throws ClientServicesException {
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activityUuid);
		activityNode.setTitle(title);
		activityNode.setTags(tags);
		activityNode.setType(ActivityNode.TYPE_ENTRY);
		return activityService.createActivityNode(activityNode);
    }
    
    protected ActivityNode createActivityNode(String activityUuid, String title, String type, List<String> tags) throws ClientServicesException {
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activityUuid);
		activityNode.setTitle(title);
		activityNode.setType(type);
		activityNode.setTags(tags);
		return activityService.createActivityNode(activityNode);
    }
    
    protected ActivityNode createActivityNode(String activityUuid, String title, String type) throws ClientServicesException {
		ActivityNode activityNode = new ActivityNode();
		activityNode.setActivityUuid(activityUuid);
		activityNode.setTitle(title);
		activityNode.setType(type);
		return activityService.createActivityNode(activityNode);
    }
    
    protected List<ActivityNode> createActivityDescendants(String activityUuid, int count, long interval) throws ClientServicesException {
    	List<ActivityNode> activityNodes = new ArrayList<ActivityNode>();
    	
    	for (int i=0; i<count; i++) {
    		long now = System.currentTimeMillis();
    		String title = i + ". Descendant-" + now;
    		activityNodes.add(createActivityNode(activityUuid, title));
    		
    		try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
			}
    	}
    	
    	return activityNodes;
    }
       
	protected List<ActivityNode> touchActivityNodes(List<ActivityNode> nodes, boolean reverse, long interval) throws ClientServicesException {
		List<ActivityNode> touchedNodes = new ArrayList<ActivityNode>();
		Date now = new Date();
		if (reverse) {
			for (int i=nodes.size(); i>0; i--) {
				ActivityNode node = nodes.get(i-1);
				node.setContent("Updated at "+now);
				activityService.updateActivityNode(node);
				
				touchedNodes.add(activityService.getActivityNode(node.getActivityNodeUuid()));
	    		
	    		try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}
			}
		} else {
			for (int i=0; i<nodes.size(); i++) {
				ActivityNode node = nodes.get(i);
				node.setContent("Updated at "+now);
				activityService.updateActivityNode(node);
				
				touchedNodes.add(activityService.getActivityNode(node.getActivityNodeUuid()));
	    		
	    		try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}
			}
		}
		return touchedNodes;
	}
    
    protected List<Activity> createActivities(String titlePrefix, int count, long interval) throws ClientServicesException {
    	activities = new ArrayList<Activity>();
    	
    	for (int i=0; i<count; i++) {
    		long now = System.currentTimeMillis();
    		String title = titlePrefix + "-" + now;
    		activities.add(createActivity(title));
    		
    		try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
			}
    	}
    	
		System.out.println("         TITLE                    CREATE                  PUBLISHED                 UPDATED");
		for (Activity activity : activities) {
			String title = activity.getTitle();
			Date create = new Date(Long.parseLong(title.substring(title.indexOf('-')+1)));
			System.out.println(activity.getTitle() + "  " + dateFormat.format(create) + "  " + dateFormat.format(activity.getPublished()) + "  " + dateFormat.format(activity.getUpdated()));
		}
    	
    	return activities;
    }
    
    protected void deleteActivities(List<Activity> activities) {
    	for (Activity activity : activities) {
			try {
				//System.out.println("Deleting "+activity.getTitle());
				activityService.deleteActivity(activity);
			} catch (Exception e) {
			}
    	}
    }
        
	protected List<ActivityNode> createActivityDescendants(Activity activity) throws ClientServicesException {
		String activityUuid = activity.getActivityUuid();
		
		List<ActivityNode> createdNodes = createActivityDescendants(activityUuid, 5, 1000);
		
		List<ActivityNode> touchedNodes = touchActivityNodes(createdNodes, true, 1000);
		
		dumpNodes(touchedNodes);

		return touchedNodes;
	}
	
	protected void dumpNodes(List<ActivityNode> nodes) {
		System.out.println("            TITLE                 PUBLISHED            UPDATED");
		for (ActivityNode node : nodes) {
			String title = node.getTitle();
			Date create = new Date(Long.parseLong(title.substring(title.indexOf('-')+1)));
			System.out.println(node.getTitle() + "  " + dateFormat.format(node.getPublished()) + "  " + dateFormat.format(node.getUpdated()));
		}
	}
	
	protected Member getMember(Activity activity, String userid) throws ClientServicesException, XMLException {
		EntityList<Member> members = activity.getMembers();
		for (Member member : members) {
			if (userid.equals(member.getContributor().getUserid())) {
				return member;
			}
		}
		return null;
	}

	protected boolean containsActivity(EntityList<Activity> activities, Activity activity) throws ClientServicesException, XMLException {
		for (Activity nextActivity : activities) {
			if (activity.getId().equals(nextActivity.getId())) {
				return true;
			}
		}
		return false;
	}
	
    
}
