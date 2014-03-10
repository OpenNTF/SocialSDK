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
package com.ibm.sbt.sample.bss;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import com.ibm.commons.runtime.Context;
import com.ibm.commons.util.StringUtil;
import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;
import com.ibm.sbt.services.client.smartcloud.bss.BssService.BillingFrequency;
import com.ibm.sbt.services.client.smartcloud.bss.CustomerJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.CustomerManagementService;
import com.ibm.sbt.services.client.smartcloud.bss.OrderJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberManagementService;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriptionManagementService;

/**
 * Some utility methods to make the BSS samples easier to read
 * 
 * @author mwallace
 *
 */
public class BssUtil {
	
	static private String DOMAIN = "mailinator.com";
	
	static public String getEndpoint(HttpServletRequest request) {
		String endpoint = request.getParameter("endpoint");
		if (StringUtil.isEmpty(endpoint)) {
			endpoint = Context.get().getProperty("bss.endpoint");
		}
		return StringUtil.isEmpty(endpoint) ? "smartcloudC1" : endpoint;
	}
	
	static public String getCustomerId(HttpServletRequest request) throws BssException, JsonException, IOException {
		String customerId = request.getParameter("customerId");
		if (StringUtil.isEmpty(customerId)) {
			customerId = Context.get().getProperty("bss.customerId");
		}
		if (StringUtil.isEmpty(customerId)) {
			// You must be a vendor for this to work
			customerId = registerCustomer(getEndpoint(request));
		}
		return customerId;
	}
	
	static public String getDomain(HttpServletRequest request) {
		String domain = request.getParameter("domain");
		if (StringUtil.isEmpty(domain)) {
			domain = Context.get().getProperty("bss.domain");
		}
		return StringUtil.isEmpty(domain) ? DOMAIN : domain;
	}
	
	static public String getFirstCustomerId(String endpoint) throws BssException {
    	CustomerManagementService customerManagement = new CustomerManagementService(endpoint);
    	EntityList<JsonEntity> customerList = customerManagement.getCustomers();
    	if (!customerList.isEmpty()) {
    		JsonEntity firstCustomer = customerList.get(0);
    		long id = firstCustomer.getAsLong("Id");
    		return "" + id;
    	}
    	return null;
	}

	static public String registerCustomer(String endpoint) throws BssException, JsonException, IOException {
		CustomerJsonBuilder customer = new CustomerJsonBuilder();
    	customer.setOrgName("Abe Industrial")
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
    	
    	CustomerManagementService customerManagement = new CustomerManagementService(endpoint);
    	JsonJavaObject response = customerManagement.registerCustomer(customer);
    	return String.valueOf(response.getAsLong("Long"));
    }
    
    static public void unregisterCustomer(String endpoint, String customerId) throws BssException {
    	CustomerManagementService customerManagement = new CustomerManagementService(endpoint);
    	customerManagement.unregisterCustomer(customerId);
    }

    static public String addSubscriber(String endpoint, String customerId) throws BssException, JsonException, IOException {
    	return addSubscriber(endpoint, customerId, DOMAIN);
    }
    
    static public String addSubscriber(String endpoint, String customerId, String domain) throws BssException, JsonException, IOException {
		SubscriberJsonBuilder subscriber = new SubscriberJsonBuilder();
		subscriber.setCustomerId(customerId)
				  .setRole(SubscriberManagementService.Role.User)
				  .setFamilyName("Doe")
				  .setGivenName("John")
				  .setEmailAddress(getUniqueEmail(domain))
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
		
    	SubscriberManagementService subscriberManagement = new SubscriberManagementService(endpoint);
    	JsonJavaObject response = subscriberManagement.addSubscriber(subscriber);
    	return String.valueOf(response.getAsLong("Long"));
    }
    
    static public String createSubscription(String endpoint, String customerId, int duration, String partNumber, int quantity) throws BssException, JsonException, IOException {
		SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService(endpoint);
		OrderJsonBuilder order = new OrderJsonBuilder();
		order.setCustomerId(customerId)
			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
		     .setDurationLength(duration)
		     .setPartNumber(partNumber)
		     .setPartQuantity(quantity)
		     .setBillingFrequency(BillingFrequency.ARC);
		EntityList<JsonEntity> subscriptionList = subscriptionManagement.createSubscription(order);
		return String.valueOf(subscriptionList.get(0).getAsLong("SubscriptionId"));
    }
    
    static public EntityList<JsonEntity> getSubscriptionsById(String endpoint, String customerId) throws BssException {
		SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService(endpoint);
   		return subscriptionManagement.getSubscriptionsById(customerId);
    }

    static public void activateSubscriber(String endpoint, String subscriberId) throws BssException {
   		SubscriberManagementService subscriberManagement = new SubscriberManagementService(endpoint);
		subscriberManagement.activateSubscriber(subscriberId);
    }
        
    static public String getUniqueEmail() {
    	return getUniqueEmail(DOMAIN);
    }
	
    static public String getUniqueEmail(String domain) {
    	return "ibmsbt_"+System.currentTimeMillis()+"@" + domain;
    }
	
}
