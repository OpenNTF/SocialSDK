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
package com.ibm.sbt.sample.app;

import com.ibm.commons.util.StringUtil;
import com.ibm.sbt.services.client.connections.profiles.Profile;
import com.ibm.sbt.services.client.connections.profiles.ProfileAdminService;
import com.ibm.sbt.services.client.connections.profiles.ProfileServiceException;
import com.ibm.sbt.services.client.smartcloud.bss.CustomerManagementService;


/**
 * @author mwallace
 *
 */
public class ProfileAdminApp extends BaseApp {
	
	private ProfileAdminService profileAdminService;
	
	public ProfileAdminApp(String url, String user, String password) {
		super(url, user, password);
	}
	
    public ProfileAdminService getProfileAdminService() {
    	if (profileAdminService == null) {
    		profileAdminService = new ProfileAdminService(getBasicEndpoint());
    	}
    	return profileAdminService;
    }
    
    public Profile getProfile(String id) throws ProfileServiceException{
    	return getProfileAdminService().getProfile(id);
    }
	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: java com.ibm.sbt.sample.app.ProfileAdminApp <url> <user> <password> <id>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String id = args[3];
		
		ProfileAdminApp paa = null;
		try {
			paa = new ProfileAdminApp(url, user, password);
			
			Profile profile = paa.getProfile(id);
			
			System.out.println(profile.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
