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
import java.util.HashMap;
import java.util.Iterator;

import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.CommunityServiceException;
import com.ibm.sbt.services.client.connections.communities.MemberList;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.ConnectionsBasicEndpoint;

/**
 * @author mwallace, Francis
 * @date 11 April 2013
 */
public class CommunityServiceApp {

    private CommunityService communityService;
    
    /**
     * Default constructor
     */
    public CommunityServiceApp(String url, String user, String password) {
        this.communityService = new CommunityService();
        this.communityService.setEndpoint(createEndpoint(url, user, password));
    }
    
    private BasicEndpoint createEndpoint(String url, String user, String password) {
    	BasicEndpoint endpoint = new ConnectionsBasicEndpoint();
    	endpoint.setUrl(url);
    	endpoint.setUser(user);
    	endpoint.setPassword(password);
    	endpoint.setForceTrustSSLCertificate(true);
    	return endpoint;
    }
    
    /**
	 * @return the communityService
	 */
	public CommunityService getCommunityService() {
		return communityService;
	}
    
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.CommunityServiceApp url user password");
			return;
		}
		String url = args[0];
		String user = args[1];
		String password = args[2];
	    CommunityServiceApp app = new CommunityServiceApp(url, user, password);
		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put("ps", "100"); //100 members will be retrieved at a time. Max is 10,000.
	    try {
	    	CommunityService communityService = app.getCommunityService();
            Collection<Community> communities = communityService.getPublicCommunities();
            for (Iterator<Community> iter = communities.iterator(); iter.hasNext(); ) {
                Community community = iter.next();
                boolean moreMembers = true;
                int page = 1;
                
                System.out.println(community.getTitle());
                do{
                    parameters.put("page", Integer.toString(page));
                    MemberList members = communityService.getMembers(community.getCommunityUuid(), parameters);
                    for (int i=0; i<members.size(); i++) {
                        System.out.println("    " + members.get(i).getEmail());
                    }
                    if((members.getStartIndex() + members.getItemsPerPage()) <= members.getTotalResults()){
                        moreMembers = true;
                        page++;
                    }else{
                        moreMembers=false;
                    }
                }while(moreMembers);
            }
        } catch (CommunityServiceException e) {
            e.printStackTrace();
        }
	    
	}
}
