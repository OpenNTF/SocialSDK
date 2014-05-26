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
package com.ibm.sbt.services.client.connections.activities;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ibm.commons.util.StringUtil;

/**
 * @author mwallace
 * 
 * Set the following system properties:
 * -DServerUrl=https://apps.na.collabservtest.lotus.com
 * -DUsers=<login1>:<password1>:<userid1>,<login2>:<password2>:<userid2>
 *
 */
public class ActivityConcurrentUpdates {
	
	@Test
	public void testConcurrentUpdates() {
		
		String url = System.getProperty("ServerUrl");
		String userstr = System.getProperty("Users");
		
		String[] userstrs = StringUtil.splitString(userstr, ',');
		String[][] users = new String[userstrs.length][2];
		for (int i=0; i<userstrs.length; i++) {
			users[i] = StringUtil.splitString(userstrs[i], ':');
		}
		
		ActivityClient[] clients = ActivityClient.createClients(url, users);
		
		try {
			long start = System.currentTimeMillis();
			
			Activity activity = clients[0].createActivity(clients);
			
			List<Thread> threads = new ArrayList<Thread>();
			
			for (ActivityClient client : clients) {
				Thread thread = client.createActivityNodes(activity, 20);
				threads.add(thread);
			}
			
			for (Thread thread : threads) {
				thread.join();
			}
			
			long duration = System.currentTimeMillis() - start;
			System.out.println("Completed test: "+duration+"(ms)");
		} catch (Exception e) {
			System.err.println("Error creating activity entries: "+e.getMessage());
			e.printStackTrace();
		}
		
	}

	@Test
	public void testConcurrentFileUploads() {
		
		String url = System.getProperty("ServerUrl");
		String userstr = System.getProperty("Users");
		
		String[] userstrs = StringUtil.splitString(userstr, ',');
		String[][] users = new String[userstrs.length][2];
		for (int i=0; i<userstrs.length; i++) {
			users[i] = StringUtil.splitString(userstrs[i], ':');
		}
		
		ActivityClient[] clients = ActivityClient.createClients(url, users);
		
		try {
			long start = System.currentTimeMillis();
			
			Activity activity = clients[0].createActivity(clients);
			
			List<Thread> threads = new ArrayList<Thread>();
			
			for (ActivityClient client : clients) {
				Thread thread = client.uploadFiles(activity, 20);
				threads.add(thread);
			}
			
			for (Thread thread : threads) {
				thread.join();
			}
			
			long duration = System.currentTimeMillis() - start;
			System.out.println("Completed test: "+duration+"(ms)");
		} catch (Exception e) {
			System.err.println("Error uploading activity files: "+e.getMessage());
			e.printStackTrace();
		}
		
	}

}
