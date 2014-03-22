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

import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberManagementService;

/**
 * @author mwallace
 *
 */
public class CustomerProvisioning extends BaseBssApp {

	/**
	 * @param url
	 * @param user
	 * @param password
	 */
	public CustomerProvisioning(String url, String user, String password) {
		super(url, user, password);
	}

	/**
	 * Demonstrate a Customer Provisioning workflow.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Usage: java com.ibm.sbt.bss.app.CustomerProvisioning <url> <user> <password>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		
		CustomerProvisioning cp = null;
		try {
			cp = new CustomerProvisioning(url, user, password);

			String email = cp.getUniqueEmail("mailinator.com");
			System.out.println("Email: " + email);
			String customerId = cp.registerCustomer("Some Company", email);
			System.out.println("Customer Id: " + customerId);
			
			/*
			EntityList<JsonEntity> subscriptions = cp.getSubscriptionsById(customerId);
			System.out.println("Subscriptions: "+subscriptions.size());
			String subscriptionId = null;
			for (JsonEntity subscription : subscriptions) {
				long oid = subscription.getAsLong("Oid");
				String partNumber = subscription.getAsString("PartNumber");
				System.out.println(partNumber);
				// part number for IBM SmartCloud Connections
				if ("D0NWLLL".equalsIgnoreCase(partNumber)) {
					subscriptionId = "" + oid;
					break;
				}
			}
			if (StringUtil.isEmpty(subscriptionId)) {
	    		subscriptionId = cp.createSubscription(customerId, 3, "D0NWLLL", 5);
			}
			*/
			String subscriptionId = cp.createSubscription(customerId, 3, "D0NWLLL", 5);
			System.out.println("Subscription Id: " + subscriptionId);
			
			SubscriberManagementService subscriberManagement = cp.getSubscriberManagementService();
			//EntityList<JsonEntity> subscribers = subscriberManagement.getSubscribersByEmail(email);
			//JsonEntity subscriber = subscribers.get(0);
			//String subscriberId = String.valueOf(subscriber.getAsLong("Oid"));
			String subscriberId = cp.addSubscriber(customerId, cp.getUniqueEmail("mailinator.com"), SubscriberManagementService.Role.CustomerAdministrator);
			System.out.println("Subscriber Id: " + subscriberId);
			JsonEntity subscriber = cp.getSubscriberById(subscriberId);
			System.out.println("Subscriber: " + subscriber.toJsonString(false));
	   		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
	   		System.out.println("Login Name: " + loginName);
			String subscriberState = subscriber.getAsString("Subscriber/SubscriberState");
			System.out.println("Subscriber State: " + subscriberState);
			
			if (!"ACTIVE".equals(subscriberState)) {
				subscriberManagement.activateSubscriber(subscriberId);
				System.out.println("Activated subscriber: " + subscriberId);
			}
			
			cp.setOneTimePassword(loginName, "one-time-123");
			cp.changePassword(loginName, "one-time-123", "password1");
			System.out.println("Changed password: " + loginName);
			
			cp.entitleSubscriber(subscriberId, subscriptionId, true);
			System.out.println("Entitled subscriber: " + subscriptionId);
			
			subscriber = subscriberManagement.getSubscriberById(subscriberId);
			System.out.println(subscriber.toJsonString(false));	
			
			System.out.println("Customer provisioning successful");
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
}
