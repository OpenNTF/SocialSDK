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
import com.ibm.sbt.services.client.connections.communities.Community;
import com.ibm.sbt.services.client.connections.communities.CommunityService;
import com.ibm.sbt.services.client.connections.communities.Member;
import com.ibm.sbt.services.endpoints.BasicEndpoint;
import com.ibm.sbt.services.endpoints.EndpointFactory;

/**
 * @author mwallace
 * @date 11 April 2013
 */
public class GetAllCommunitiesApp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RuntimeFactory runtimeFactory = new RuntimeFactoryStandalone();
		Application application = runtimeFactory.initApplication(null);
		Context context = Context.init(application, null, null);

		try {
			BasicEndpoint endpoint = (BasicEndpoint)EndpointFactory.getEndpoint(CommunityService.DEFAULT_ENDPOINT_NAME);
	        endpoint.login("admin", "passw0rd", true);
			
	        CommunityService communityService = new CommunityService();
            Collection<Community> communities = communityService.getPublicCommunities();
            for (Iterator<Community> iter = communities.iterator(); iter.hasNext(); ) {
                Community community = iter.next();
                Member[] members = communityService.getMembers(community);
                System.out.println(community.getTitle());
                for (int i=0; i<members.length; i++) {
                    System.out.println("    " + members[i].getEmail());
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}

		Context.destroy(context);
		Application.destroy(application);
	}
}
