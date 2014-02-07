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

import java.math.BigInteger;

import org.junit.After;
import org.junit.Assert;

import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.BssService.BillingFrequency;
import com.ibm.sbt.services.endpoints.BasicEndpoint;

/**
 * @author mwallace
 *
 */
public class BaseBssTest {
	
	private BigInteger customerId;
	
	private BasicEndpoint basicEndpoint;
	private CustomerManagementService customerManagement;
	private SubscriberManagementService subscriberManagement;
	private SubscriptionManagementService subscriptionManagement;
	
    @After
    public void cleanup() {
    	if (customerManagement != null) {
	    	// unregister customer that was created by the test
	    	if (customerId != null) {
	    		unregisterCustomer(customerId);
	    	}
	    	
	    	customerManagement = null;
    	}
    }
    
    /**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(BigInteger customerId) {
		this.customerId = customerId;
	}
    
    /**
	 * @return the basicEndpoint
	 */
	public BasicEndpoint getBasicEndpoint() {
    	if (basicEndpoint == null) {
	    	String url = System.getProperty("ServerUrl");
	    	String user = System.getProperty("UserName");
	    	String password = System.getProperty("Password");
	    	
			basicEndpoint = new BasicEndpoint();
			basicEndpoint.setUrl(url);
			basicEndpoint.setForceTrustSSLCertificate(true);
			basicEndpoint.setUser(user);
			basicEndpoint.setPassword(password);
    	}
    	return basicEndpoint;
	}
    
    public CustomerManagementService getCustomerManagementService() {
    	if (customerManagement == null) {
			customerManagement = new CustomerManagementService(getBasicEndpoint());
    	}
    	return customerManagement;
    }
    
    public SubscriberManagementService getSubscriberManagementService() {
    	if (subscriberManagement == null) {
			subscriberManagement = new SubscriberManagementService(getBasicEndpoint());
    	}
    	return subscriberManagement;
    }
    
    public SubscriptionManagementService getSubscriptionManagementService() {
    	if (subscriptionManagement == null) {
    		subscriptionManagement = new SubscriptionManagementService(getBasicEndpoint());
    	}
    	return subscriptionManagement;
    }
    
    public BigInteger registerCustomer() {
    	try {
    		CustomerJsonBuilder customer = new CustomerJsonBuilder();
        	customer.setOrgName("Acme Industrial")
        	        .setPhone("999-999-9999")
        	        .setOrganizationAddressLine1("5 Technology Park Drive")
        	        .setOrganizationAddressLine2("")
        	        .setOrganizationAddressType(CustomerManagementService.AddressType.MAILING)
        	        .setOrganizationCity("Westford")
        	        .setOrganizationCountry("United States")
        	        .setOrganizationPostalCode("01866")
        	        .setOrganizationState("Massachusetts")
        	        .setContactFamilyName("Ninty")
        	        .setContactGivenName("Joe")
        	        .setContactEmailAddress(getUniqueEmail())
        	        .setContactNamePrefix("Mr")
        	        .setContactEmployeeNumber("6A77777")
        	        .setContactLanguagePreference("EN_US")
        	        .setContactWorkPhone("800-555-1234")
        	        .setContactMobilePhone("800-555-2345")
        	        .setContactHomePhone("800-555-3456")
        	        .setContactFax("800-555-4567")
        	        .setContactJobTitle("Director")
        	        .setContactWebSiteAddress("joeninty.example.com")
        	        .setContactTimeZone("America/Central")
        	        .setContactPhoto("")
        	        .setCustomerAccountNumber("0000123457")
        	        .setCustomerAccountLocationName("Westford Lab")
        	        .setCustomerAccountPaymentMethodType(CustomerManagementService.PaymentMethodType.INVOICE)
        	        .setCustomerAccountCurrencyType("USD")
        	        .setCustomerIdentifierType(CustomerManagementService.CustomerIdType.IBM_CUSTOMER_NUMBER)
        	        .setCustomerIdentifierValue("9999999999");
        	
        	JsonJavaObject response = getCustomerManagementService().registerCustomer(customer);
        	customerId = BigInteger.valueOf(response.getAsLong("Long"));
        	Assert.assertNotNull("Invalid customer id", customerId);
        	return customerId;
    	} catch (Exception e) {
    		Assert.fail("Error registering customer caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
    public void unregisterCustomer(BigInteger customerId) {
    	try {
    		getCustomerManagementService().unregisterCustomer(customerId);
    	} catch (Exception e) {
    		Assert.fail("Error unregistering customer caused by: "+e.getMessage());
    	}
    }
    
    public BigInteger addSubscriber() {
    	try {
    		if (customerId == null) {
    			customerId = registerCustomer();
    		}
    		
    		SubscriberJsonBuilder subscriber = new SubscriberJsonBuilder();
    		subscriber.setCustomerId(customerId)
    				  .setRole(SubscriberManagementService.Role.User)
    				  .setFamilyName("Doe")
    				  .setGivenName("John")
    				  .setEmailAddress(getUniqueEmail())
    				  .setNamePrefix("Mr")
    				  .setNameSuffix("")
    				  .setEmployeeNumber("6A7777B")
    				  .setLanguagePreference("EN_US")
    				  .setWorkPhone("111-111-1111")
    				  .setMobilePhone("111-111-1112")
    				  .setHomePhone("111-111-1113")
    				  .setFax("111-111-1114")
    				  .setJobTitle("Director")
    				  .setWebSiteAddress("www.example.com")
    				  .setTimeZone("America/Central")
    				  .setPhoto("");
        	System.out.println(subscriber.toJson());
    		
        	JsonJavaObject response = getSubscriberManagementService().addSubsciber(subscriber);
        	BigInteger subscriberId = BigInteger.valueOf(response.getAsLong("Long"));
        	Assert.assertNotNull("Invalid subscriber id", subscriberId);
        	return subscriberId;
    	} catch (Exception e) {
    		Assert.fail("Error adding subscriber caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
    public void deleteSubscriber(BigInteger subscriberId) {
    	try {
    		getSubscriberManagementService().deleteSubsciber(subscriberId);
    	} catch (Exception e) {
    		Assert.fail("Error deleting subscriber caused by: "+e.getMessage());    		
    	}
    }
    
    public BigInteger createSubscription() {
    	try {
    		BigInteger customerId = registerCustomer();
    		OrderJsonBuilder order = new OrderJsonBuilder();
    		order.setCustomerId(customerId)
    			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
    		     .setDurationLength(3)
    		     .setPartNumber("D0NWLLL")
    		     .setPartQuantity(5)
    		     .setBillingFrequency(BillingFrequency.ARC);
    		System.out.println(order.toJson());
        	
    		EntityList<JsonEntity> subscriptionList = getSubscriptionManagementService().createSubscription(order);

    		JsonEntity subscription = subscriptionList.get(0);
    		BigInteger subscriptionId = BigInteger.valueOf(subscription.getAsLong("SubscriptionId"));
        	Assert.assertNotNull("Invalid subscription id", subscriptionId);
        	return subscriptionId;
    	} catch (Exception e) {
    		Assert.fail("Error creating subscription caused by: "+e.getMessage());
    	}
    	return null;
    }
    
    protected String getUniqueEmail() {
    	return "ibmsbt_"+System.currentTimeMillis()+"@mailinator.com";
    }
	
}
