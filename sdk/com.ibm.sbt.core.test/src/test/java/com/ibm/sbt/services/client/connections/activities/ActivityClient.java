/*
 * � Copyright IBM Corp. 2014
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

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.ibm.sbt.services.client.ClientServicesException;
import com.ibm.sbt.services.client.connections.common.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.Endpoint;

/**
 * @author mwallace
 *
 */
public class ActivityClient {

	private String user;
	private String password;
	private String id;
	private Endpoint endpoint;
	private ActivityService activityService;
	
	static final String sourceClass = ActivityClient.class.getName();
	static final Logger logger = Logger.getLogger(sourceClass);
	
	static public ActivityClient[] createClients(String url, String[][] users) {
		ActivityClient[] clients = new ActivityClient[users.length];
		for (int i=0; i<users.length; i++) {
			clients[i] = new ActivityClient(url, users[i][0], users[i][1], users[i][2]);
		}
		return clients;
	}

	ActivityClient(String url, String user, String password, String id) {
		this.user = user;
		this.password = password;
		this.id = id;
		
		this.endpoint = createEndpoint(url, user, password);
		this.activityService = new ActivityService(this.endpoint);
	}
	
	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	public String getId() {
		return id;
	}
	
	/**
	 * @return the endpoint
	 */
	public Endpoint getEndpoint() {
		return endpoint;
	}
	
	/**
	 * @return the activityService
	 */
	public ActivityService getActivityService() {
		return activityService;
	}
	
	/**
	 * 
	 */
	public Endpoint createEndpoint(String url, String user, String password) {
		BasicEndpoint basicEndpoint = null;
		basicEndpoint = new BasicEndpoint();    			
		basicEndpoint.setUrl(url);
		basicEndpoint.setForceTrustSSLCertificate(true);
		basicEndpoint.setUser(user);
		basicEndpoint.setPassword(password);
		
		// proxy support
		//basicEndpoint.setHttpProxy("127.0.0.1:8888");
		//basicEndpoint.setUseProxy(true);
		
		// cookie handling
		basicEndpoint.setClientServiceClass("com.ibm.sbt.services.client.CookieStoreClientService");
				
		return basicEndpoint;
	}
	
    public Activity getActivity(String activityUuid) throws ClientServicesException {
    	ActivityService activityService = getActivityService();
    	return activityService.getActivity(activityUuid);
    }
    
    public Activity createActivity(ActivityClient[] participants) throws ClientServicesException {
    	long start = System.currentTimeMillis();
    	
    	String user = getUser();
    	String title = "Activity["+user+"-"+System.currentTimeMillis()+"]";
    	
    	ActivityService activityService = getActivityService();
    	Activity activity = new Activity(activityService);
    	activity.setTitle(title);

    	Map<String, String> params = new HashMap<String, String>();
    	params.put("nodescap", "unlimited");

    	activity = activityService.createActivity(activity, params);

		long duration = System.currentTimeMillis() - start;
		logger.fine("Created activity: "+duration+"(ms)");
		
		System.out.println(user+" created activity "+activity.getActivityUuid()+" duration:"+duration+"(ms)");
    	
    	for (ActivityClient participant : participants) {
    		if (!participant.getUser().equals(user)) {
    			activity.addMember(Member.TYPE_PERSON, participant.getId(), Member.ROLE_OWNER);

    			System.out.println("Added member "+participant.getUser());
    		}
    	}
		
		return activity;
    }
    
	public Thread createActivityNodes(final Activity activity, final int count) throws InterruptedException {
		Thread thread = new Thread() {
			@Override
			public void run() {
				for (int i=0; i<count; i++) {
					try {
				    	long start = System.currentTimeMillis();

				    	ActivityService activityService = getActivityService();
						
						String title = "ActivityNode["+user+"-"+System.currentTimeMillis()+"]";
				    	ActivityNode activityNode = new ActivityNode(activityService);
				    	activityNode.setActivityUuid(activity.getActivityUuid());
				    	activityNode.setTitle(title);
						
				    	activityService.createActivityNode(activityNode);

						long duration = System.currentTimeMillis() - start;
						logger.fine("Created activity node: "+duration+"(ms)");
				    	
						System.out.println(user+" created activity node "+activityNode.getActivityNodeUuid()+" duration:"+duration+"(ms)");
						
					} catch (Exception e) {
						System.err.println("Error creating activity node for: "+user+" caused by: "+e.getMessage());
						//e.printStackTrace();
					}
				}
			}
		};

		thread.start();
		
		return thread;
	}
	
}
