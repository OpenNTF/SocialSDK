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
import com.ibm.sbt.services.client.base.datahandlers.EntityList;


/**
 * @author mwallace
 *
 */
public class CreateSubscriptionsApp extends BaseBssApp {
	
	public CreateSubscriptionsApp(String url, String user, String password) {
		super(url, user, password);
	}
	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: java com.ibm.sbt.bss.app.CreateSubscriptionsApp <url> <user> <password> <customerId>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String customerId = args[3];
		
		CreateSubscriptionsApp cs = null;
		try {
			cs = new CreateSubscriptionsApp(url, user, password);
			
			EntityList<JsonEntity> subscriptions = cs.getSubscriptionsById(customerId);
			
			String[] partNumbers = { 
					"D0NWILL",	// IBM SmartCloud Meetings for Enterprise Deployment - ASL
					"D0NWJLL",	// IBM SmartCloud iNotes - Application Specific License
					"D0NWKLL",	// IBM SmartCloud Engage for Enterprise Deployment - ASL
					"D0NWLLL",	// IBM SmartCloud Connections
					"D0Y5ELL",	// IBM SmartCloud Docs
					"D100PLL",	// IBM SmartCloud Additional Storage Collaboration GB
					"D0PUILL",	// IBM SmartCloud Notes - ASL MTU AU(bill mth to mth)
					"D0PUJLL"	// IBM SmartCloud Notes Trav - ASL MTU AU(bill mth to mth)
			};
			
			boolean hasSubscription = false;
			for (String partNumber : partNumbers) {
				hasSubscription = false;
				for (JsonEntity subscription : subscriptions) {
					String custPartNumber = subscription.getAsString("PartNumber");
					if (custPartNumber.equalsIgnoreCase(partNumber)) {
						hasSubscription = true;
					}
				}
				if (!hasSubscription) {
					cs.createSubscription(customerId, 3, partNumber, 5);
				}
			}
			
			subscriptions = cs.getSubscriptionsById(customerId);
			for (JsonEntity subscription : subscriptions) {
				System.out.println(subscription.toJsonString(false));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
