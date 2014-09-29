/*
 * � Copyright IBM Corp. 2014
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;

/**
 * @author mwallace
 *
 */
public class ProvisionTestOrg extends BaseBssTest {

	final static public String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	final static public DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	
	@Test
    public void testProvisionUsers() {
		provisionSubscribers(5);
		
		this.customerId = null;
    }
	

	private void provisionSubscribers(int count) {
    	try {
    		// Create customer
    		String customerId = registerCustomer();
    		
    		// Provision subscribers
    		provisionSubscribers(customerId, count);
    		
    	} catch (Exception e) {
    		e.printStackTrace();   		
    	}
    }
    		

	private void provisionSubscribers(String customerId, int count) {
    	try {
    		// Create "IBM SmartCloud Connections" Subscription
    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 100);

    		System.out.println("Customer Id:" + customerId);
    		for (int i=0; i<count; i++) {
    			try {
    	    		// Add Subscriber
    	    		long start1 = System.currentTimeMillis();
    	    		String loginName = "lorenzo_user"+i+"@yopmail.com";
    	    		String subscriberId = addSubscriber(customerId, loginName);
    	    		long duration1 = System.currentTimeMillis() - start1;

    	    		JsonEntity subscriber = getSubscriberById(subscriberId);
    	    		loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    	    		System.out.println(loginName);
    	    		
    	    		// Activate the subscriber
    	    		long start2 = System.currentTimeMillis();
    	    		activateSubscriber(subscriberId);
    	    		long duration2 = System.currentTimeMillis() - start2;
    	    		
    	    		// Set password
    	    		long start3 = System.currentTimeMillis();
    	    		setPassword(loginName, "onet1me!", "passw0rd");
    	    		long duration3 = System.currentTimeMillis() - start3;
    	    		
    	    		// Entitle subscriber
    	    		long start6 = System.currentTimeMillis();
    	    		long duration6 = System.currentTimeMillis() - start6;
    	    		long start7 = System.currentTimeMillis();
    	    		entitleSubscriber(subscriberId, engageSubscriptionId, true);
    	    		long duration7 = System.currentTimeMillis() - start7;
    			} catch (Exception e) {
    			}
    		}
    		System.out.println("");
    	} catch (Exception e) {
    		e.printStackTrace();   		
    	}
    }

}
