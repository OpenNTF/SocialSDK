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

import com.ibm.sbt.services.client.smartcloud.bss.CustomerManagementService;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author mwallace
 *
 */
public class CustomerProvisioning {

	/**
	 * Demonstrate a Customer Provisioning workflow.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.bss.app.CustomerProvisioning <url> <user> <password> <verb> <output>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		
		try {
			BasicEndpoint basicEndpoint = new BasicEndpoint();
			basicEndpoint.setUrl(url);
			basicEndpoint.setForceTrustSSLCertificate(true);
			basicEndpoint.setUser(user);
			basicEndpoint.setPassword(password);

			CustomerManagementService customerManagement = new CustomerManagementService(basicEndpoint);

			// Step 1. 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
