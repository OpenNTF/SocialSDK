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
package com.ibm.sbt.services.client.smartcloud.bss;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author mwallace
 *
 */
public class CreateSubscriptionsTest extends BaseBssTest {

    @Test
    public void testCreateSubscriptions() {
    	Thread[] threads = new Thread[50];
    	for (int i=0; i<threads.length; i++) {
    		final String base = "org"+i;
    		threads[i] = new Thread() {
    			public void run() {
    				String customerId = createSubscriptions(base);
    				addSubscribers(customerId);
    			};
    		};
    	}
    	for (int i=0; i<threads.length; i++) {
    		threads[i].start();
    	}
    	for (int i=0; i<threads.length; i++) {
    		try {
				threads[i].join(120000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    }
    
    private void addSubscribers(String customerId) {
    	for (int i=0; i<20; i++) {
    		addSubscriber(customerId);
    	}
    }
    
    private String createSubscriptions(String base) {
    	try {
    		String customerId = registerCustomer(getUniqueEmail(base));
    		System.out.println("CustomerId:"+customerId);
    		
    		this.customerId = null;

    		createSubscriptions(customerId, "D0NWLLL");
    		createSubscriptions(customerId, "D0NWLLL-UNIFYPOOL");
    		createSubscriptions(customerId, "D0NWLLL-UNIFYTRL");
    		
    		return customerId;
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error creating subscriptions caused by: "+e.getMessage());    		
    	}
    	return null;
    }

    private void createSubscriptions(String customerId, String partNumber) {
		for (int i=0; i<20; i++) {
    		String subscriptionId = createSubscription(customerId, 3, partNumber, 5);
			long start = System.currentTimeMillis();
	    	for (int j=0; j<100; j++) {
	    		JsonEntity subscription = getSubscriptionById(subscriptionId);
	    		if (subscription != null) {
	    			System.out.println(subscription.toJsonString());
    	    		String currentState = subscription.getAsString("Subscription/SubscriptionState");
	        		System.out.println("SubscriptionState:"+currentState);
	        		if ("ACTIVE".equalsIgnoreCase(currentState)) {
	        			break;
	        		}
	    		}	    		
	    		// wait the specified interval
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ie) {}
	    	}
	    	long duration = System.currentTimeMillis() - start;
	    	System.out.println(customerId + " " + i + ". Wait for subscription " + partNumber + " took: " + duration);
	    	
    		// wait the specified interval
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {}
		}
    }
}
