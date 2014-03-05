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
public class GetSubscriptionsApp extends BaseBssApp {
	
	public GetSubscriptionsApp(String url, String user, String password) {
		super(url, user, password);
	}
	
	/**
	 * Demo.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 4) {
			System.out.println("Usage: java com.ibm.sbt.bss.app.GetSubscriptionsApp <url> <user> <password> <customerId>");
			return;
		}
		
		String url = args[0];
		String user = args[1];
		String password = args[2];
		String customerId = args[3];
		
		GetSubscriptionsApp sa = null;
		try {
			sa = new GetSubscriptionsApp(url, user, password);
			
			EntityList<JsonEntity> subscriptions = sa.getSubscriptionsById(customerId);
			for (JsonEntity subscription : subscriptions) {
				System.out.println(subscription.toJsonString(false));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
