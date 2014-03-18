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

import java.util.List;

import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberManagementService;


/**
 * @author mwallace
 *
 */
public class CreateVendorApp extends BaseBssApp {
	
	public CreateVendorApp(String url, String user, String password) {
		super(url, user, password);
	}
	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: java com.ibm.sbt.bss.app.AddSubscribersBlog <url> <user> <password> <domain>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String domain = args[3];
		
		CreateVendorApp cva = null;
		try {
			cva = new CreateVendorApp(url, user, password);
			
			EntityList<JsonEntity> subscribers = cva.getSubscribersByEmail(user);
			String customerId = "" + subscribers.get(0).getAsLong("CustomerId");
			System.out.println(customerId);

			String subscriberId = cva.addSubscriber(customerId, cva.getUniqueEmail(domain), SubscriberManagementService.Role.VSR);
				
			cva.activateSubscriber(subscriberId);
				
			String loginName = cva.getLoginName(subscriberId);
			System.out.println(loginName);
				
			cva.setOneTimePassword(loginName, "one-time-123");
				
			cva.changePassword(loginName, "one-time-123", "password1");
			
			cva.assignRole(loginName, "CUSTOMERADMINISTRATOR");
			//cva.assignRole(loginName, "VENDORADMINISTRATOR");
			//cva.assignRole(loginName, "VSR");
			
			String[] roles = cva.getRoles(loginName);
			for (String role : roles) {
				System.out.println(role);
			}
			
			CreateVendorApp cva2 = new CreateVendorApp(url, loginName, "password1");
			
			customerId = cva2.registerCustomer(cva2.getUniqueEmail(domain));
			System.out.println(customerId);
			
			List<String> customerIds = cva2.getCustomerIds();
			System.out.println("Sub VSR Customers: "+customerIds.size());
			for (String customerId1 : customerIds) {
				System.out.println(customerId1);
			}
			
			customerIds = cva.getCustomerIds();
			System.out.println("Master VSR Customers: "+customerIds.size());
			for (String customerId1 : customerIds) {
				System.out.println(customerId1);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
