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

import java.util.List;

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
	
	private String customerId;
	
	private BasicEndpoint basicEndpoint;
	private CustomerManagementService customerManagement;
	private SubscriberManagementService subscriberManagement;
	private SubscriptionManagementService subscriptionManagement;
	private AuthorizationService authorizationService;
	private AuthenticationService authenticationService;
	
    @After
    public void cleanup() {
    	if (customerManagement != null) {
	    	// unregister customer that was created by the test
	    	if (customerId != null) {
	    		unregisterCustomer(customerId);
	    	}
	    	customerManagement = null;
    	}
    	subscriberManagement = null;
    	subscriptionManagement = null;
    	authenticationService = null;
    	authorizationService = null;
    }
    
    /**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
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
	    	
	    	if (url == null || user == null || password == null) {
	    		throw new IllegalStateException("Please set VM arguments for: -DServerUrl=https://apps.na.collabservtest.lotus.com -DUserName=<VSR account user> -DPassword=<VSR account password>");
	    	}
	    	
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
    
    public AuthenticationService getAuthenticationService(String user, String password) {
    	String url = System.getProperty("ServerUrl");
    	BasicEndpoint basicEndpoint = new BasicEndpoint();
		basicEndpoint.setUrl(url);
		basicEndpoint.setForceTrustSSLCertificate(true);
		basicEndpoint.setUser(user);
		basicEndpoint.setPassword(password);

    	return new AuthenticationService(basicEndpoint);
    }
    
    public String registerCustomer() {
    	try {
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
        	
        	JsonJavaObject response = getCustomerManagementService().registerCustomer(customer);
        	customerId = String.valueOf(response.getAsLong("Long"));
        	Assert.assertNotNull("Invalid customer id", customerId);
        	return customerId;
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error registering customer because: "+jsonObject);
    	} catch (Exception e) {
    		Assert.fail("Error registering customer caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
    public void unregisterCustomer(String customerId) {
    	try {
    		getCustomerManagementService().unregisterCustomer(customerId);
    	} catch (Exception e) {
    		Assert.fail("Error unregistering customer caused by: "+e.getMessage());
    	}
    }
    
    public String addSubscriber() {
   		if (customerId == null) {
   			customerId = registerCustomer();
   		}
   		
   		return addSubscriber(customerId);
    }
    
    public String addAdministrator(String customerId) {
    	String subscriberId = addSubscriber(customerId);
    	
    	addAdministratorRole(subscriberId);
    	
    	return subscriberId;
    }
    		
    public void addAdministratorRole(String subscriberId) {
    	try {
			JsonEntity subscriber = getSubscriberById(subscriberId);
			String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
			System.out.println(loginName);
			
			AuthorizationService authorizationService = getAuthorizationService();
			String[] roles = authorizationService.getRoles(loginName);
			for (String role : roles) {
				System.out.println(role);
			}
	
			authorizationService.assignRole(loginName, "CustomerAdministrator");
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error adding administrator role because: "+jsonObject);
    	} catch (Exception e) {
    		Assert.fail("Error adding administrator role caused by: "+e.getMessage());    		
    	}
    }
    
    public String addSubscriber(String customerId) {
       	try {
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
    		
        	JsonJavaObject response = getSubscriberManagementService().addSubscriber(subscriber);
        	String subscriberId = String.valueOf(response.getAsLong("Long"));
        	Assert.assertNotNull("Invalid subscriber id", subscriberId);
        	return subscriberId;
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error registering customer because: "+jsonObject);
    	} catch (Exception e) {
    		Assert.fail("Error adding subscriber caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
    public String createSubscription(String customerId, int duration, String partNumber, int quantity) {
    	try {
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
    		
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error creating subscription because: "+jsonObject);
    	} catch (Exception e) {
    		Assert.fail("Error creating subscription caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
    public JsonEntity getSubscriptionById(String subscriptionId) {
    	try {
    		SubscriptionManagementService subscriptionManagement = getSubscriptionManagementService();
    		return subscriptionManagement.getSubscriptionById(subscriptionId);
    		
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error retrieving subscription because: "+jsonObject);
    	} catch (Exception e) {
    		Assert.fail("Error retrieving subscription caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
	public String getLoginName(String subscriberId) {
    	try {
    		JsonEntity subscriber = getSubscriberById(subscriberId);
    		return subscriber.getAsString("Subscriber/Person/EmailAddress");
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error getting login name caused by: "+e.getMessage());    		
    	}
    	return null;
	}
    
	public void setOneTimePassword(String loginName, String password) {
    	try {
    		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
    		userCredential.setLoginName(loginName)
    					  .setNewPassword(password);
    		
    		AuthenticationService authenticationService = getAuthenticationService();
    		authenticationService.setOneTimePassword(userCredential);
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error setting one time password because: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error setting one time password caused by: "+e.getMessage());    		
    	}
	}
    
    public void changePassword(String loginName, String oldPassword, String newPassword) {
    	try {
    		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
    		userCredential.setLoginName(loginName)
    					  .setOldPassword(oldPassword)
    					  .setNewPassword(newPassword)
    					  .setConfirmPassword(newPassword);
    		
    		authenticationService.changePassword(userCredential);
    		
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.fail("Error changing password because: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error changing password caused by: "+e.getMessage());    		
    	}
    }
    
    public void updateSubscriberEmail(String subscriberId, String emailAddress) {
    	try {
    		SubscriberManagementService subscriberManagement = getSubscriberManagementService();
    		
    		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
			JsonJavaObject rootObject = jsonEntity.getJsonObject();
			
			JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
			JsonJavaObject personObject = subscriberObject.getAsObject("Person");
			personObject.putString("EmailAddress", emailAddress);
			
			subscriberManagement.updateSubscribeProfile(rootObject);
				
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error updating subscriber profile caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error updating subscriber profile caused by: "+e.getMessage());    		
    	}
    }
    
    public void activateSubscriber(String subscriberId) {
    	try {
    		SubscriberManagementService subscriberManagement = getSubscriberManagementService();
			subscriberManagement.activateSubscriber(subscriberId);
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.out.println(jsonObject);
    		Assert.fail("Error activating subscriber caused by: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error activating subscriber caused by: "+e.getMessage());    		
    	}
    }
    
    public void entitleSubscriber(final String subscriberId, final String subscriptionId, final boolean acceptTOU) {
    	try {
    		final SubscriberManagementService subscriberManagement = getSubscriberManagementService();

    		StateChangeListener listener = new StateChangeListener() {
				@Override
				public void stateChanged(JsonEntity jsonEntity) {
					try {
						JsonEntity entitlement = subscriberManagement.entitleSubscriber(subscriberId, subscriptionId, acceptTOU);
						System.out.println(entitlement.toJsonString());
					} catch (BssException be) {
			    		JsonJavaObject jsonObject = be.getResponseJson();
			    		System.out.println(jsonObject);
			    		Assert.fail("Error entitling subscriber caused by: "+jsonObject);
			    	} 
				}
			};
    		if (!getSubscriptionManagementService().waitSubscriptionState(subscriptionId, "ACTIVE", 500, 1000, listener)) {
    			Assert.fail("Timeout waiting for subscription to activate");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error entitling subscriber caused by: "+e.getMessage());    		
    	}
    }
	
    public JsonEntity getSubscriberById(String subscriberId) {
    	try {
    		JsonEntity jsonEntity = getSubscriberManagementService().getSubscriberById(subscriberId);
    		System.out.println(jsonEntity.toJsonString());
    		return jsonEntity;
    	} catch (Exception e) {
    		Assert.fail("Error retrieving subscriber caused by: "+e.getMessage());    		
    	}
    	return null;
    }
    
    public void deleteSubscriber(String subscriberId) {
    	try {
    		getSubscriberManagementService().deleteSubscriber(subscriberId);
    	} catch (Exception e) {
    		Assert.fail("Error deleting subscriber caused by: "+e.getMessage());    		
    	}
    }
    
    public String createSubscription() {
    	try {
    		String customerId = registerCustomer();
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
    		String subscriptionId = String.valueOf(subscription.getAsLong("SubscriptionId"));
        	Assert.assertNotNull("Invalid subscription id", subscriptionId);
        	return subscriptionId;
    	} catch (Exception e) {
    		Assert.fail("Error creating subscription caused by: "+e.getMessage());
    	}
    	return null;
    }
    
    protected JsonJavaObject findSeat(String subscriberId, String subscriptionId) throws BssException {
    	JsonEntity jsonEntity = getSubscriberManagementService().getSubscriberById(subscriberId);
		JsonJavaObject rootObject = jsonEntity.getJsonObject();
		JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
		List<Object> seatSet = subscriberObject.getAsList("SeatSet");
		for (Object seat : seatSet) {
			String nextId = String.valueOf(((JsonJavaObject)seat).getAsLong("SubscriptionId"));
			if (nextId.equals(subscriptionId)) {
				JsonJavaObject seatJson = new JsonJavaObject();
				seatJson.put("Seat", (JsonJavaObject)seat);
				return seatJson;
			}
		}
		return null;
    }
    
    protected JsonEntity getSeat(String subscriptionId, String seatId) throws BssException {
    	return getSubscriptionManagementService().getSeat(subscriptionId, seatId);
    }
        
    protected String getUniqueEmail() {
    	return "ibmsbt_"+System.currentTimeMillis()+"@mailinator.com";
    }
	
	protected boolean arrayContains(String value, String[] expectedValues) {
		for (String nextValue : expectedValues) {
			if (value.equalsIgnoreCase(nextValue)) {
				return true;
			}
		}
		return false;
	}
	
    
}
