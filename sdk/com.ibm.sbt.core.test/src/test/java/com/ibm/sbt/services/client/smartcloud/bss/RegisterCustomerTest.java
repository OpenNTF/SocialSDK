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

import com.ibm.commons.util.io.json.JsonJavaObject;

/**
 * @author mwallace
 *
 */
public class RegisterCustomerTest extends BaseBssTest {

    @Test
    public void testRegisterCustomerBadRequest() {
    	try {
    		CustomerJsonBuilder customer = new CustomerJsonBuilder();
        	customer.setOrgName("Test Enterprise")
        	        .setPhone("999-999-9999");
        	
        	getCustomerManagementService().registerCustomer(customer);

    		Assert.fail("Expected bad request response");
    		
    	} catch (BssException be) {
    		// expecting this exception
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		Assert.assertNotNull("Expected JSON response", jsonObject);
    		Assert.assertEquals("400", be.getResponseCode());
    		Assert.assertEquals("The [LocationName] field requires a string value.", be.getResponseMessage());
    		Assert.assertEquals("Error", be.getSeverity());
    		Assert.assertEquals("Provide a valid string value for the [LocationName] field.", be.getUserAction());
    		Assert.assertEquals("BZSAP1033E", be.getMessageId());
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error registering customer caused by: "+e.getMessage());    		
    	}
    }
	
    @Test
    public void testRegisterCustomer() {
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
        	long customerId = response.getAsLong("Long");
        	Assert.assertNotNull("Invalid customer id", customerId);
        	System.out.println(customerId);
    	} catch (BssException be) {
    		JsonJavaObject jsonObject = be.getResponseJson();
    		System.err.println(jsonObject);
    		//be.printStackTrace();
    		Assert.fail("Error registering customer because: "+jsonObject);
    	} catch (Exception e) {
    		e.printStackTrace();
    		Assert.fail("Error registering customer caused by: "+e.getMessage());    		
    	}
    }
	
}
