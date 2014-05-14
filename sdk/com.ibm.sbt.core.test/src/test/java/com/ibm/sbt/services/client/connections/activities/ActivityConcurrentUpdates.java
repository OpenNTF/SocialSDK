package com.ibm.sbt.services.client.connections.activities;

import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.StringUtil;

import org.junit.Assert;
import org.junit.Test;

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
				Thread thread = client.createActivityNodes(activity, 10);
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

}
