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
public class ProvisioningLoadTest extends BaseBssTest {

	final static public String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
	final static public DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
	static {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Test
    public void testProvisionAdditionalUsers() {
		try {
			EntityList<JsonEntity> customerList = getCustomerManagementService().getCustomers();
			for (JsonEntity customer : customerList) {
				String orgName = customer.getJsonObject().getJsonObject("Organization").getAsString("OrgName");
				if ("Abe Industrial".equals(orgName)) {
					String id = String.valueOf(customer.getJsonObject().getAsLong("Id"));
					String customerState = customer.getJsonObject().getAsString("CustomerState");
					try {
						if ("ACTIVE".equals(customerState)) {							
							provisionSubscribers(id, 10, true);
						}
					} catch (Exception e) {
						System.err.println("Provisioning additional users failed because: "+e.getMessage());
					}
				}
			}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Provisioning additional users failed because: "+e.getMessage());    		
    	}
	}
	
	@Test
    public void testProvisionUsersAlt() {
		provisionSubscribersAlt(100);
    }
	
	@Test
    public void testProvisionUsers() {
		provisionSubscribers(100, false);
    }
	
    @Test
    public void testProvisionUsersSingleSubscriptionLongRun() {
    	this.assertFail = false;
    	
    	for (int i=0; i<20; i++) {
    		try {
    			provisionSubscribers(500, true);
    		} catch (Exception e) {
    		}
    		
    		// wait the specified interval
			try {
				Thread.sleep(1800000);
			} catch (InterruptedException ie) {}
    	}
    }
	
    @Test
    public void testProvisionUsersSingleSubscription() {
    	try {
    		// Create customer
    		String customerId = registerCustomer();
    		System.out.println("Customer Id : "+customerId);
    		
    		this.customerId = null;
    		
    		long begin = System.currentTimeMillis();
    		for (int i=0; i<500; i++) {
    			try {
    	    		// Add Subscriber
    	    		long startAdd = System.currentTimeMillis();
    	    		String subscriberId = addSubscriber(customerId);
    	    		long durationAdd = System.currentTimeMillis() - startAdd;
    	
    	    		// Activate the subscriber
    	    		long startActivate = System.currentTimeMillis();
    	    		activateSubscriber(subscriberId);
    	    		long durationActivate = System.currentTimeMillis() - startActivate;
    	    		
    	    		// Create "IBM SmartCloud Connections" Subscription
    	    		long startCreateSub = System.currentTimeMillis();
    	    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 1);
    	    		long durationCreateSub = System.currentTimeMillis() - startCreateSub;

    	    		// Entitle subscriber
    	    		long startEntitleSub = System.currentTimeMillis();
    	    		entitleSubscriber(subscriberId, engageSubscriptionId, true);
    	    		long durationEntitleSub = System.currentTimeMillis() - startEntitleSub;
    	    		System.out.println(i+1 + ", " + subscriberId + ", " + durationAdd + ", " + durationActivate + ", " + durationCreateSub + ", " + durationEntitleSub);
    	    		//entitleSubscriber(subscriberId, storageSubscriptionId, true);
    			} catch (Exception e) {
    			}
    		}
    		long totalDuration = System.currentTimeMillis() - begin;
    		System.out.println("Total duration: "+totalDuration+"(ms)");
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error provisioning 100 users caused by: "+e.getMessage());    		
    	}
    }
	
    @Test
    public void testReprovisionUsers() {
    	try {
    		// Create customer
    		String customerId = registerCustomer();
    		
    		// Create "IBM SmartCloud Connections" Subscription
    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 100);
    		//System.out.println(engageSubscriptionId);
    		System.out.println("D0NWLLL : " + getSubscriptionById(engageSubscriptionId).toJsonString());

    		// Create Extra Storage Subscription
    		String storageSubscriptionId = createSubscription(customerId, 3, "D100PLL", 100);
    		//System.out.println(storageSubscriptionId);
    		System.out.println("D100PLL : " + getSubscriptionById(storageSubscriptionId).toJsonString());

    		String prefix = "ibmsbt" + System.currentTimeMillis() + "_";
    		List<String> subscriberIds = new ArrayList<String>();
    		for (int i=0; i<10; i++) {
	    		// Add Subscriber
    			String email = prefix + i + "@ivthouse.com";
	    		String subscriberId = addSubscriber(customerId, email);
	    		subscriberIds.add(subscriberId);
	    		
	    		// Activate the subscriber
	    		activateSubscriber(subscriberId);
	    		
	    		// Entitle subscriber
	    		long start = System.currentTimeMillis();
	    		entitleSubscriber(subscriberId, engageSubscriptionId, true);
	    		long duration = System.currentTimeMillis() - start;
	    		System.out.println(i+1 + ", " + subscriberId + ", " + duration);
	    		//entitleSubscriber(subscriberId, storageSubscriptionId, true);
    		}
    		
    		for (String subscriberId : subscriberIds) {
    			getSubscriberManagementService().deleteSubscriber(subscriberId);
    		}
    		
    		System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------");

    		for (int i=0; i<10; i++) {
	    		// Add Subscriber
    			String email = prefix + i + "@ivthouse.com";
	    		String subscriberId = addSubscriber(customerId, email);
	    		subscriberIds.add(subscriberId);
	    		
	    		// Activate the subscriber
	    		activateSubscriber(subscriberId);
	    		
	    		// Entitle subscriber
	    		long start = System.currentTimeMillis();
	    		entitleSubscriber(subscriberId, engageSubscriptionId, true);
	    		long duration = System.currentTimeMillis() - start;
	    		System.out.println(i+1 + ", " + subscriberId + ", " + duration);
	    		//entitleSubscriber(subscriberId, storageSubscriptionId, true);
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error provisioning 100 users caused by: "+e.getMessage());    		
    	}
    }

	@Test
	public void testProvisionUsersLongRun() {
		this.assertFail = false;
		
		for (int i=0; i<20; i++) {
			try {
				provisionSubscribers(100, false);
			} catch (Exception e) {
			}
			
			// wait the specified interval
			try {
				Thread.sleep(1800000);
			} catch (InterruptedException ie) {}
		}
	}

	@Test
	public void testProvisionUsersAltLongRun() {
		this.assertFail = false;
		
		for (int i=0; i<20; i++) {
			try {
				provisionSubscribersAlt(100);
			} catch (Exception e) {
			}
			
			// wait the specified interval
			try {
				Thread.sleep(1800000);
			} catch (InterruptedException ie) {}
		}
	}

	private void provisionSubscribers(int count, boolean singleSubscription) {
    	try {
    		// Create customer
    		String customerId = registerCustomer();
    		
    		// Provision subscribers
    		provisionSubscribers(customerId, count, singleSubscription);
    		
    	} catch (Exception e) {
    		e.printStackTrace();   		
    	}
    }
    		

	private void provisionSubscribers(String customerId, int count, boolean singleSubscription) {
    	try {
    		// Create "IBM SmartCloud Connections" Subscription
    		String engageSubscriptionId = null;
    		if (!singleSubscription) {
    			engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 100);
    		}

    		System.out.println("Customer Id:" + customerId);
    		System.out.println("Time, Subscriber Id, Add Subscriber, Activate Subscriber, Set password, Create Subscription, Entitle Subscriber, Entitle Subscriber, Revoke Subscription, Cancel Subscription");
    		for (int i=0; i<count; i++) {
    			try {
    	    		// Add Subscriber
    	    		long start1 = System.currentTimeMillis();
    	    		String subscriberId = addSubscriber(customerId);
    	    		long duration1 = System.currentTimeMillis() - start1;

    	    		JsonEntity subscriber = getSubscriberById(subscriberId);
    	    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    	    		
    	    		// Activate the subscriber
    	    		long start2 = System.currentTimeMillis();
    	    		activateSubscriber(subscriberId);
    	    		long duration2 = System.currentTimeMillis() - start2;
    	    		
    	    		// Set password
    	    		long start3 = System.currentTimeMillis();
    	    		setPassword(loginName, "onet1me!", "passw0rd");
    	    		long duration3 = System.currentTimeMillis() - start3;
    	    		
    	    		// Create temp
    	    		long start4 = System.currentTimeMillis();
    	    		String tempSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 1);
    	    		long duration4 = System.currentTimeMillis() - start4;

    	    		// Entitle temp
    	    		long start5 = System.currentTimeMillis();
    	    		entitleSubscriber(subscriberId, tempSubscriptionId, true);
    	    		long duration5 = System.currentTimeMillis() - start5;
    	    		
    	    		// Entitle subscriber
    	    		long start6 = System.currentTimeMillis();
    	    		if (singleSubscription) {
    	    			engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 1);
    	    		}
    	    		long duration6 = System.currentTimeMillis() - start6;
    	    		long start7 = System.currentTimeMillis();
    	    		entitleSubscriber(subscriberId, engageSubscriptionId, true);
    	    		long duration7 = System.currentTimeMillis() - start7;
    	    		
    	    		// Revoke temp
    	    		long start8 = System.currentTimeMillis();
    	    		JsonJavaObject seatJson = findSeat(subscriberId, tempSubscriptionId);
    	    		String seatId = String.valueOf(seatJson.getAsObject("Seat").getAsLong("Id"));
    	    		revokeSubscriber(subscriberId, seatId, true);
    	    		long duration8 = System.currentTimeMillis() - start8;
    	    		    	    		
    	    		// Cancel temp
    	    		long start9 = System.currentTimeMillis();
    	    		cancelSubscription(tempSubscriptionId);
    	    		long duration9 = System.currentTimeMillis() - start9;
    	    		    	    		
    	    		System.out.println(dateFormat.format(new Date(start1)) + 
    	    				", " + subscriberId + 
    	    				", " + duration1 + 
    	    				", " + duration2 + 
    	    				", " + duration3 + 
    	    				", " + duration4 + 
    	    				", " + duration5 + 
    	    				", " + duration6 + 
    	    				", " + duration7 + 
    	    				", " + duration8 + 
    	    				", " + duration9);
    			} catch (Exception e) {
    			}
    		}
    		System.out.println("");
    	} catch (Exception e) {
    		e.printStackTrace();   		
    	}
    }

	private void provisionSubscribersAlt(int count) {
    	try {
    		// Create customer
    		String customerId = registerCustomer();
    		
    		// Provision subscribers
    		provisionSubscribersAlt(customerId, count);
    		
    	} catch (Exception e) {
    		e.printStackTrace();   		
    	}
    }
    		
	private void provisionSubscribersAlt(String customerId, int count) {
    	try {
    		// Create "IBM SmartCloud Connections" Subscription
    		String engageSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 100);

    		List<UserProvisionJob> provisionJobs = new ArrayList<UserProvisionJob>();
    		for (int i=0; i<count; i++) {
    			try {
    	    		provisionJobs.add(new UserProvisionJob(engageSubscriptionId));
    	    		System.out.print(".");
    			} catch (Exception e) {
    			}
    		}
    		for (UserProvisionJob provisionJob : provisionJobs) {
    			try {
    				provisionJob.entitlePoolSubscription();
    	    		System.out.print(".");
    			} catch (Exception e) {
    			}
    		}
    		for (UserProvisionJob provisionJob : provisionJobs) {
    			try {
    				provisionJob.waitPoolSubscription();
    	    		System.out.print(".");
    			} catch (Exception e) {
    			}
    		}
    		for (UserProvisionJob provisionJob : provisionJobs) {
    			try {
    				provisionJob.entitleEngageSubscription();
    	    		System.out.print(".");
    			} catch (Exception e) {
    			}
    		}
    		for (UserProvisionJob provisionJob : provisionJobs) {
    			try {
    				provisionJob.waitEngageSubscription();
    	    		System.out.print(".");
    			} catch (Exception e) {
    			}
    		}
    		for (UserProvisionJob provisionJob : provisionJobs) {
    			try {
    				provisionJob.revokePoolSubscription();
    	    		System.out.print(".");
    			} catch (Exception e) {
    			}
    		}
    		System.out.println("Customer Id:" + customerId);
    		System.out.println("Time, Subscriber Id, Add Subscriber, Activate Subscriber, Set password, Create Subscription, Entitle Subscriber, Entitle Subscriber, Revoke Subscription, Cancel Subscription");
    		for (UserProvisionJob provisionJob : provisionJobs) {
    			try {
    				provisionJob.dumpDurations();
    			} catch (Exception e) {
    			}
    		}
    		System.out.println("");
    	} catch (Exception e) {
    		e.printStackTrace();   		
    	}
    }

	private class UserProvisionJob {
		
		private String engageSubscriptionId;
		private String subscriberId;
		private String loginName;
		private String tempSubscriptionId;
		
		private long start = System.currentTimeMillis();
		
		private long duration1;
		private long duration2;
		private long duration3;
		private long duration4;
		private long duration5;
		private long duration6;
		private long duration7;
		private long duration8;
		private long duration9;
		
		UserProvisionJob(String engageSubscriptionId) {
			this.engageSubscriptionId = engageSubscriptionId;
			
    		// Add Subscriber
    		long start = System.currentTimeMillis();
    		subscriberId = addSubscriber(customerId);
    		duration1 = System.currentTimeMillis() - start;
    		
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
    		
    		// Activate the subscriber
    		long start2 = System.currentTimeMillis();
    		activateSubscriber(subscriberId);
    		duration2 = System.currentTimeMillis() - start2;
    		
    		// Set password
    		long start3 = System.currentTimeMillis();
    		setPassword(loginName, "onet1me!", "passw0rd");
    		duration3 = System.currentTimeMillis() - start3;
    		
    		// Create temp
    		long start4 = System.currentTimeMillis();
    		tempSubscriptionId = createSubscription(customerId, 3, "D0NWLLL", 1);
    		duration4 = System.currentTimeMillis() - start4;
		}
		
		void entitlePoolSubscription() throws BssException {
    		// Entitle temp
    		long start5 = System.currentTimeMillis();
    		entitleSubscriber(subscriberId, tempSubscriptionId, true, false);
    		duration5 = System.currentTimeMillis() - start5;
		}
		
		void waitPoolSubscription() throws BssException {
			getSubscriberManagementService().waitSeatState(subscriberId, tempSubscriptionId, "ASSIGNED", 10, 2000, null);
		}
		
		void entitleEngageSubscription() throws BssException {
    		long start6 = System.currentTimeMillis();
    		entitleSubscriber(subscriberId, engageSubscriptionId, true, false);
    		duration6 = System.currentTimeMillis() - start6;
		}

		void waitEngageSubscription() throws BssException {
			getSubscriberManagementService().waitSeatState(subscriberId, engageSubscriptionId, "ASSIGNED", 10, 2000, null);
		}
		
		void revokePoolSubscription() throws BssException {
    		// Revoke temp
    		long start7 = System.currentTimeMillis();
    		JsonJavaObject seatJson = findSeat(subscriberId, tempSubscriptionId);
    		String seatId = String.valueOf(seatJson.getAsObject("Seat").getAsLong("Id"));
    		revokeSubscriber(subscriberId, seatId, true);
    		duration7 = System.currentTimeMillis() - start7;
    		    	    		
    		// Cancel temp
    		long start8 = System.currentTimeMillis();
    		cancelSubscription(tempSubscriptionId);
    		duration8 = System.currentTimeMillis() - start8;
		}
		
		void dumpDurations() {
    		System.out.println(dateFormat.format(new Date(start)) + 
    				", " + subscriberId + 
    				", " + duration1 + 
    				", " + duration2 + 
    				", " + duration3 + 
    				", " + duration4 + 
    				", " + duration5 + 
    				", " + duration6 + 
    				", " + duration7 + 
    				", " + duration8);
		}
		
	}

}
