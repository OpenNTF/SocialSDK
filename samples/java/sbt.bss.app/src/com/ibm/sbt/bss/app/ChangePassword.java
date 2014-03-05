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
package com.ibm.sbt.bss.app;

import java.io.IOException;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.sbt.services.client.smartcloud.bss.AuthenticationService;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;
import com.ibm.sbt.services.client.smartcloud.bss.UserCredentialJsonBuilder;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author mwallace
 *
 */
public class ChangePassword {

	private String url;
	private String user;
	private String password;
	
	private BasicEndpoint basicEndpoint;

	private AuthenticationService authenticationService;
	
	public ChangePassword(String url, String user, String password) {
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
    	}
    	return basicEndpoint;
	}
    	
    public AuthenticationService getAuthenticationService() {
    	if (authenticationService == null) {
    		authenticationService = new AuthenticationService(getBasicEndpoint());
    	}
    	return authenticationService;
    }
    
	public void setOneTimePassword(String loginName, String password) throws BssException, JsonException, IOException {
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setNewPassword(password);
		
		System.out.println(userCredential.toJson());
		
		AuthenticationService authenticationService = getAuthenticationService();
		authenticationService.setOneTimePassword(userCredential);
	}
    
    public void changePassword(String loginName, String oldPassword, String newPassword) throws BssException, JsonException, IOException {
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setOldPassword(oldPassword)
					  .setNewPassword(newPassword)
					  .setConfirmPassword(newPassword);
		
		System.out.println(userCredential.toJson());
		
		AuthenticationService authenticationService = getAuthenticationService();
		authenticationService.changePassword(userCredential);
    }
    	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 6) {
			System.out.println("Usage: java com.ibm.sbt.bss.app.ChangePassword <url> <user> <password> <loginName> <one_time_password> <new_password>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String loginName = args[3];
		String oneTimePassword = args[4];
		String newPassword = args[5];
		
		ChangePassword cp = null;
		try {
			cp = new ChangePassword(url, user, password);
			
			cp.setOneTimePassword(loginName, oneTimePassword);
				
			cp.changePassword(loginName, oneTimePassword, newPassword);
			
			System.out.println("Successfully changed password for: "+loginName);
		} catch (BssException be) {
			System.err.println("Error changing password for: "+loginName);
			System.err.println(be.getResponseJson());
		} catch (Exception e) {
			System.err.println("Error changing password for: "+loginName);
			e.printStackTrace();
		}

	}
	
}
