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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ibm.commons.util.io.json.JsonException;
import com.ibm.commons.util.io.json.JsonJavaObject;
import com.ibm.sbt.services.client.base.JsonEntity;
import com.ibm.sbt.services.client.base.datahandlers.EntityList;
import com.ibm.sbt.services.client.smartcloud.bss.AuthenticationService;
import com.ibm.sbt.services.client.smartcloud.bss.AuthorizationService;
import com.ibm.sbt.services.client.smartcloud.bss.BssException;
import com.ibm.sbt.services.client.smartcloud.bss.BssService.BillingFrequency;
import com.ibm.sbt.services.client.smartcloud.bss.CustomerJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.CustomerManagementService;
import com.ibm.sbt.services.client.smartcloud.bss.OrderJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.StateChangeListener;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberJsonBuilder;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriberManagementService;
import com.ibm.sbt.services.client.smartcloud.bss.SubscriptionManagementService;
import com.ibm.sbt.services.client.smartcloud.bss.UserCredentialJsonBuilder;

/**
 * @author mwallace
 *
 */
public class BaseBssApp extends BaseApp {

	private CustomerManagementService customerManagement;
	private SubscriberManagementService subscriberManagement;
	private SubscriptionManagementService subscriptionManagement;
	private AuthorizationService authorizationService;
	private AuthenticationService authenticationService;
	
	public BaseBssApp(String url, String user, String password) {
		super(url, user, password);
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
    
    public AuthorizationService getAuthorizationService() {
    	if (authorizationService == null) {
    		authorizationService = new AuthorizationService(getBasicEndpoint());
    	}
    	return authorizationService;
    }
    
    public AuthenticationService getAuthenticationService() {
    	if (authenticationService == null) {
    		authenticationService = new AuthenticationService(getBasicEndpoint());
    	}
    	return authenticationService;
    }
    
    public String registerCustomer(String email) throws BssException, JsonException, IOException {
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
    	        .setContactEmailAddress(email)
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
    	return String.valueOf(response.getAsLong("Long"));
    }
    
    public List<String> getCustomerIds() throws BssException {
    	CustomerManagementService customerManagement = getCustomerManagementService();
		EntityList<JsonEntity> customerList = customerManagement.getCustomers();
		List<String> customerIds = new ArrayList<String>();
		for (JsonEntity customer : customerList) {
			long id = customer.getAsLong("Id");
			customerIds.add(""+id);
		}
    	return customerIds;
    }
    
    public void unregisterCustomer(String customerId) throws BssException {
   		getCustomerManagementService().unregisterCustomer(customerId);
    }
    
    public String addSubscriber(String customerId, String email) throws BssException, JsonException, IOException {
    	return addSubscriber(customerId, email, SubscriberManagementService.Role.User);
    }
    
    public String addSubscriber(String customerId, String email, SubscriberManagementService.Role role) throws BssException, JsonException, IOException {
		SubscriberJsonBuilder subscriber = new SubscriberJsonBuilder();
		subscriber.setCustomerId(customerId)
				  .setRole(role)
				  .setFamilyName("Doe")
				  .setGivenName("Aaron")
				  .setEmailAddress(email)
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
		
    	JsonJavaObject response = getSubscriberManagementService().addSubscriber(subscriber);
    	return String.valueOf(response.getAsLong("Long"));
    }
    
    public String createSubscription(String customerId, int duration, String partNumber, int quantity) throws BssException, JsonException, IOException {
		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
		OrderJsonBuilder order = new OrderJsonBuilder();
		order.setCustomerId(customerId)
			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
		     .setDurationLength(duration)
		     .setPartNumber(partNumber)
		     .setPartQuantity(quantity)
		     .setBillingFrequency(BillingFrequency.ARC);
		EntityList<JsonEntity> subscriptionList = subscriptionManagement.createSubscription(order);
		for (JsonEntity subscription : subscriptionList) {
			System.out.println(subscription.toJsonString());
		}
		return String.valueOf(subscriptionList.get(0).getAsLong("SubscriptionId"));
    }
    
    public EntityList<JsonEntity> getSubscriptionsById(String customerId) throws BssException {
   		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
   		return subscriptionManagement.getSubscriptionsById(customerId);
    }
    
    public EntityList<JsonEntity> getSubscriptions() throws BssException {
   		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
   		return subscriptionManagement.getSubscriptions();
    }
    
    public JsonEntity getSubscriptionById(String subscriptionId) throws BssException {
   		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
   		return subscriptionManagement.getSubscriptionById(subscriptionId);
    }
    
	public String getLoginName(String subscriberId) throws BssException {
   		JsonEntity subscriber = getSubscriberById(subscriberId);
   		return subscriber.getAsString("Subscriber/Person/EmailAddress");
	}
    
	public void setOneTimePassword(String loginName, String password) throws BssException, JsonException, IOException {
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setNewPassword(password);
		
		AuthenticationService authenticationService = getAuthenticationService();
		authenticationService.setOneTimePassword(userCredential);
	}
    
    public void changePassword(String loginName, String oldPassword, String newPassword) throws BssException, JsonException, IOException {
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setOldPassword(oldPassword)
					  .setNewPassword(newPassword)
					  .setConfirmPassword(newPassword);
		
		AuthenticationService authenticationService = getAuthenticationService();
		authenticationService.changePassword(userCredential);
    }
    
    public void updateSubscriberEmail(String subscriberId, String emailAddress) throws BssException {
		SubscriberManagementService subscriberManagement = getSubscriberManagementService();
		
		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
		JsonJavaObject rootObject = jsonEntity.getJsonObject();
		
		JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
		JsonJavaObject personObject = subscriberObject.getAsObject("Person");
		personObject.putString("EmailAddress", emailAddress);
		
		subscriberManagement.updateSubscribeProfile(rootObject);
    }
    
    public void activateSubscriber(String subscriberId) throws BssException {
   		SubscriberManagementService subscriberManagement = getSubscriberManagementService();
		subscriberManagement.activateSubscriber(subscriberId);
    }
    
    public boolean entitleSubscriber(final String subscriberId, final String subscriptionId, final boolean acceptTOU) throws BssException {
		final SubscriberManagementService subscriberManagement = getSubscriberManagementService();

		StateChangeListener listener = new StateChangeListener() {
			@Override
			public void stateChanged(JsonEntity jsonEntity) {
				try {
					subscriberManagement.entitleSubscriber(subscriberId, subscriptionId, acceptTOU);
				} catch (BssException e) {
					System.err.println("Unable to entitle: "+subscriberId+" to: "+subscriptionId);
				}
			}
		};
		
		return getSubscriptionManagementService().waitSubscriptionState(subscriptionId, "ACTIVE", 5, 1000, listener);
    }
	
    public EntityList<JsonEntity> getSubscribersByEmail(String email) throws BssException {
    	EntityList<JsonEntity> jsonEntities = getSubscriberManagementService().getSubscribersByEmail(email);
   		return jsonEntities;
    }
    
    public JsonEntity getSubscriberById(String subscriberId) throws BssException {
    	JsonEntity jsonEntity = getSubscriberManagementService().getSubscriberById(subscriberId);
   		return jsonEntity;
    }
    
    public void deleteSubscriber(String subscriberId) throws BssException {
   		getSubscriberManagementService().deleteSubscriber(subscriberId);
    }
    
    public String getUniqueEmail(String domain) {
    	return "testuser_"+System.currentTimeMillis()+"@"+domain;
    }
    
    public void assignRole(String loginName, String role) throws BssException {
    	getAuthorizationService().assignRole(loginName, role);
    }
    
    public String[] getRoles(String loginName) throws BssException {
    	return getAuthorizationService().getRoles(loginName);
    }
		
}
