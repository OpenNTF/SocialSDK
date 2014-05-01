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
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.w3c.dom.Node;

import com.ibm.commons.util.StringUtil;
import com.ibm.commons.xml.DOMUtil;
import com.ibm.commons.xml.XMLException;
import com.ibm.sbt.services.BaseUnitTest;
import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.test.lib.TestEnvironment;

/**
 * @author mwallace
 *
 */
public class BaseActivityServiceTest extends BaseUnitTest {

	private BasicEndpoint basicEndpoint;
	protected ActivityService activityService;
	protected Activity activity;
	
	@Before
	public void initActivityServiceTest() {
		if (activityService==null) {
			activityService = new ActivityService(getBasicEndpoint());
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
	}
	
    /**
	 * @return the basicEndpoint
	 */
	protected BasicEndpoint getBasicEndpoint() {
    	if (basicEndpoint == null) {
	    	String url = System.getProperty("ServerUrl");
	    	String user = System.getProperty("UserName");
	    	String password = System.getProperty("Password");
	    	
			basicEndpoint = new BasicEndpoint();
			basicEndpoint.setUrl(url);
			basicEndpoint.setForceTrustSSLCertificate(true);
			basicEndpoint.setUser(user);
			basicEndpoint.setPassword(password);
			
			// enable to use a proxy
			//basicEndpoint.setHttpProxy("localhost:8888");
			//basicEndpoint.setUseProxy(true);			
    	}
    	return basicEndpoint;
	}
	
	protected String getMemberId() {
		String memberId = System.getProperty("MemberId");
		if (StringUtil.isEmpty(memberId)) {
			memberId = TestEnvironment.getSecondaryUserUuid();
		}
		return memberId;
	}
	
	protected Node readXml(String fileName) throws IOException, XMLException {
		InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("com/ibm/sbt/services/client/connections/activities/"+fileName);
		return DOMUtil.createDocument(inputStream);
	}
		
    protected String createActivityTitle() {
    	return createTitle("Activity");
    }
    
    protected String createActivityNodeTitle() {
    	return createTitle("ActivityNode");
    }
    
    protected String createTitle(String type) {
    	return this.getClass().getName() + "#" + this.hashCode() + "_" + type + "-" + System.currentTimeMillis();
    }
    
    protected Activity createActivity() throws ClientServicesException {
		return createActivity(createActivityTitle());
    }
        	
    protected Activity createActivity(String title) throws ClientServicesException {
		return createActivity(title, Activity.NORMAL);
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
		return activityService.createActivityNode(activityNode);
    }
        	
}
