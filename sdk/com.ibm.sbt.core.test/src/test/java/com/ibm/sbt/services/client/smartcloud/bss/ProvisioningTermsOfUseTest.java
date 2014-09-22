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
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;

/**
 * @author mwallace
 *
 */
public class ProvisioningTermsOfUseTest extends BaseBssTest {

	final static public String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	final static public DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Test
    public void testProvisionTOU() {
    	try {
    		// Create customer
    		String customerId = registerCustomer();
    		
    		this.assertFail = false;
    		
    		// Create "IBM SmartCloud Connections" Subscription
    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 1000);
    		//System.out.println(engageSubscriptionId);
    		System.out.println("D0NWLLL : " + getSubscriptionById(engageSubscriptionId).toJsonString());

    		long begin = System.currentTimeMillis();
    		for (int i=0; i<1000; i++) {
    			try {
    	    		// Add Subscriber
    	    		String subscriberId = addSubscriber(customerId);
    	    		JsonEntity subscriber = getSubscriberById(subscriberId);
    	    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    	
    	    		// Activate the subscriber
    	    		activateSubscriber(subscriberId);    	    		
    	    		waitSubscriberActive(subscriberId);
    	    		
    	    		// Set password
    	    		setPassword(loginName, "onet1me!", "passw0rd");
    	    		
    	    		// Create Unify Pool Subscription
    	    		String poolSubscriptionId = createSubscription(customerId, 3, "D0NWLLL-UNIFYPOOL", 1);

    	    		// Entitle pool
    	    		entitleSubscriber(subscriberId, poolSubscriptionId, true);
    	    		
    	    		JsonJavaObject seatJson = findSeat(subscriberId, poolSubscriptionId);
    	    		//System.out.println(seatJson.toString());
    	    		String seatId = String.valueOf(seatJson.getAsObject("Seat").getAsLong("Id"));
    	    		boolean acceptedTOU = seatJson.getAsObject("Seat").getAsBoolean("HasAcceptedTermsOfUse");
    	    		if (!acceptedTOU) {
    	    			System.err.println("D0NWLLL-UNIFYPOOL");
    	    			System.err.println(dateFormat.format(new Date()));
    	    			System.err.println(seatJson);
    	    			System.err.println("Customer.Id: "+customerId);
    	    			System.err.println("Subscriber.Id: "+subscriberId);
    	    			System.err.println("Subscriber.Person.EmailAddress: "+loginName);
    	    			System.err.println("Seat.Id: "+customerId);
    	    			System.err.println("HasAcceptedTermsOfUse: "+acceptedTOU);
    	    			this.customerId = null;
    	    			Assert.fail("Terms of use are not accepted");
    	    		}

    	    		// Entitle engage
    	    		entitleSubscriber(subscriberId, engageSubscriptionId, true);

    	    		// HasAcceptedTermsOfUse false here
    	    		seatJson = findSeat(subscriberId, engageSubscriptionId);
    	    		//System.out.println(seatJson.toString());
    	    		seatId = String.valueOf(seatJson.getAsObject("Seat").getAsLong("Id"));
    	    		acceptedTOU = seatJson.getAsObject("Seat").getAsBoolean("HasAcceptedTermsOfUse");
    	    		if (!acceptedTOU) {
    	    			System.err.println("D0NWLLL");
    	    			System.err.println(dateFormat.format(new Date()));
    	    			System.err.println(seatJson);
    	    			System.err.println("Customer.Id: "+customerId);
    	    			System.err.println("Subscriber.Id: "+subscriberId);
    	    			System.err.println("Subscriber.Person.EmailAddress: "+loginName);
    	    			System.err.println("Seat.Id: "+customerId);
    	    			System.err.println("HasAcceptedTermsOfUse: "+acceptedTOU);
    	    			this.customerId = null;
    	    			Assert.fail("Terms of use are not accepted");
    	    		}
    	    		    	    		
					// Revoke pool
					revokeSubscriber(subscriberId, seatId, true);

					// HasAcceptedTermsOfUse intermittent here
					
					// Cancel pool subscription
					cancelSubscription(poolSubscriptionId);

					// HasAcceptedTermsOfUse intermittent here
					
    	    		System.out.print("["+i+"]");
    	    		
    			} catch (BssException be) {
    	    		JsonJavaObject jsonObject = be.getResponseJson();
    	    		System.err.println(jsonObject);
    	    	} catch (Exception e) {
    				e.printStackTrace();
    			}
    		}
    		long totalDuration = System.currentTimeMillis() - begin;
    		System.out.println("\nTotal duration: "+totalDuration+"(ms)");
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error provisioning 1000 users caused by: "+e.getMessage());    		
    	}
    }
	
}
