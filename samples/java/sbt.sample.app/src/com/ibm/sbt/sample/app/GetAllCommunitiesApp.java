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
package com.ibm.sbt.sample.app;

import java.util.Collection;
import java.util.Iterator;

import com.ibm.commons.runtime.Application;
import com.ibm.commons.runtime.Context;
import com.ibm.commons.runtime.RuntimeFactory;
import com.ibm.commons.runtime.impl.app.RuntimeFactoryStandalone;
import com.ibm.sbt.security.authentication.AuthenticationException;
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.communities.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace
 * @date 11 April 2013
 */
public class GetAllCommunitiesApp {

    private RuntimeFactory runtimeFactory;
    private Context context;
    private Application application;
    private BasicEndpoint endpoint;
    private CommunityService communityService;
    
    public void init() throws AuthenticationException{
        runtimeFactory = new RuntimeFactoryStandalone();
        application = runtimeFactory.initApplication(null);
        context = Context.init(application, null, null);
        communityService = new CommunityService();
        endpoint = (BasicEndpoint)EndpointFactory.getEndpoint(CommunityService.DEFAULT_ENDPOINT_NAME);
        endpoint.login("admin", "passw0rd", true);
    }
    
    public void destroy(){
        if (context != null)
            Context.destroy(context);
        if (application != null)
            Application.destroy(application);
    }
    
    public Collection<Community> getPublicCommunities() throws CommunityServiceException{
        return communityService.getPublicCommunities();
    }
    
    public Member[] getCommunityMembers(Community community) throws CommunityServiceException{
        return communityService.getMembers(community);
    }
    
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	    GetAllCommunitiesApp app = new GetAllCommunitiesApp();
		
	    try {
            app.init();
            Collection<Community> communities = app.getPublicCommunities();
            for (Iterator<Community> iter = communities.iterator(); iter.hasNext(); ) {
                Community community = iter.next();
                Member[] members = app.getCommunityMembers(community);
                System.out.println(community.getTitle());
                for (int i=0; i<members.length; i++) {
                    System.out.println("    " + members[i].getEmail());
                }
            }
        } catch (AuthenticationException e) {
            e.printStackTrace();
        } catch (CommunityServiceException e) {
            e.printStackTrace();
        } finally {
            app.destroy();
        }
	    
	}
}
