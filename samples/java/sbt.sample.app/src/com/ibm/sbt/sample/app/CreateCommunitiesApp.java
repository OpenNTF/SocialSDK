/*
 *  Copyright IBM Corp. 2012
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
package com.ibm.sbt.sample.app;

import java.util.Collection;
import java.util.Iterator;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.base.BaseService;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.communities.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace, Francis
 * @date 11 April 2013
 */
public class CreateCommunitiesApp {

    private RuntimeFactory runtimeFactory;
    private Context context;
    private Application application;
    private BasicEndpoint endpoint;
    private CommunityService communityService;
    
    /**
     * Default constructor. Initialises the Context, the CommunityService, and the default CommunityService endpoint.
     * 
     * Be sure to call the destroy() method in this class if you don't intend to keep the initialised Context around.
     */
    public CreateCommunitiesApp(){
        this(BaseService.DEFAULT_ENDPOINT_NAME, true);
    }
    
    /**
     * 
     * @param endpointName The name of the endpoint to use.
     * @param initEnvironment - True if you want a Context initialised, false if there is one already. destroy() should be called when finished using this class if a context is initialised here. 
     */
    public CreateCommunitiesApp(String endpointName, boolean initEnvironment) {
        if(initEnvironment)
            this.initEnvironment();
        
        this.communityService = new CommunityService();
        this.setEndpoint((BasicEndpoint)EndpointFactory.getEndpoint(endpointName));
    }
    
    /**
     * 
     * @return The endpoint used in this class.
     */
    public BasicEndpoint getEndpoint(){
        return this.endpoint;
    }
    
    /**
     * 
     * @param endpoint The endpoint you want this class to use.
     */
    public void setEndpoint(BasicEndpoint endpoint){
        this.endpoint = endpoint;
        this.communityService.setEndpoint(this.endpoint);
        
        try {
			endpoint.login("admin", "passw0rd");
		} catch (AuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /**
     * Initialise the Context, needed for Services and Endpoints.
     */
    public void initEnvironment() {
        runtimeFactory = new RuntimeFactoryStandalone();
        application = runtimeFactory.initApplication(null);
        context = Context.init(application, null, null);
    }
    
    /**
     * Destroy the Context.
     */
    public void destroy(){
        if (context != null)
            Context.destroy(context);
        if (application != null)
            Application.destroy(application);
    }
    
    /**
     * Create a community.
     * @return id of the community
     * @throws CommunityServiceException
     */
    public String createCommunity(Community community) throws CommunityServiceException{
    	return communityService.createCommunity(community);
    }
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	    CreateCommunitiesApp app = new CreateCommunitiesApp();
		
	    try {
            for (int i=0; i<10; i++) {
            	long start = System.currentTimeMillis();
            	Community community = new Community(app.communityService, "");
            	community.setTitle("CreateCommunity-"+System.currentTimeMillis()+"-"+i);
            	community.setCommunityType("public");
            	community.setContent("");
            	app.createCommunity(community);
            	long duration = System.currentTimeMillis() - start;
            	System.out.println("Create community took "+duration+"(ms)");
            }
        } catch (CommunityServiceException e) {
            e.printStackTrace();
        } finally {
            app.destroy();
        }
	}
}
