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
package bss.changepassword.app;

import java.io.IOException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.sbt.services.client.smartcloud.bss.AuthenticationService;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberManagementService;
import com.ibm.sbt.services.client.smartcloud.bss.UserCredentialJsonBuilder;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author mwallace
 *
 */
public class SetUserPassword {

	private String url;
	private String user;
	private String password;
	
	private BasicEndpoint basicEndpoint;

	private AuthenticationService authenticationService;
	private SubscriberManagementService subscriberManagementService;
	
	public SetUserPassword(String url, String user, String password) {
		this.url = url;
		this.user = user;
		this.password = password;
	}

	/**
	 * @return the basicEndpoint
	 */
	public BasicEndpoint getBasicEndpoint() {
    	if (basicEndpoint == null) {
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
    	
    public AuthenticationService getAuthenticationService() {
    	if (authenticationService == null) {
    		authenticationService = new AuthenticationService(getBasicEndpoint());
    	}
    	return authenticationService;
    }
    
    public SubscriberManagementService getSubscriberManagementService() {
    	if (subscriberManagementService == null) {
    		subscriberManagementService = new SubscriberManagementService(getBasicEndpoint());
    	}
    	return subscriberManagementService;
    }
        
    public void setUserPassword(String loginName, String newPassword, boolean bypassPolicy) throws BssException, JsonException, IOException {
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setNewPassword(newPassword);
		
		System.out.println("Set password: " + userCredential.toJson());
		
		AuthenticationService authenticationService = getAuthenticationService();
		authenticationService.setUserPassword(userCredential, bypassPolicy);
    }
    	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 5) {
			System.out.println("Usage: java bss.changepassword.app.SetUserPassword <url> <user> <password> <loginName> <new_password> [bypass_policy]");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String loginName = args[3];
		String newPassword = args[4];
		
		boolean bypassPolicy = (args.length > 5 && "false".equalsIgnoreCase(args[5])) ? false : true;
		
		SetUserPassword cp = null;
		try {
			cp = new SetUserPassword(url, user, password);
			
			cp.setUserPassword(loginName, newPassword, bypassPolicy);
			
			System.out.println("Successfully set password for: "+loginName);
		} catch (BssException be) {
			System.err.println("Error setting password for: "+loginName);
			if (be.getResponseJson() != null) {
				System.err.println(be.getResponseJson());
			} 
			be.printStackTrace();
		} catch (Exception e) {
			System.err.println("Error setting password for: "+loginName);
			e.printStackTrace();
		}

	}
	
}
