<!-- /*
 * © Copyright IBM Corp. 2012
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
 */-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.util.io.json.*"%>
<%@page import="com.ibm.sbt.services.client.base.JsonEntity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.*"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>Unregister Customer</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
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
    	        .setContactEmailAddress("ibmsbt_"+System.currentTimeMillis()+"@mailinator.com")
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
    	
    	CustomerManagementService customerManagement = new CustomerManagementService("smartcloudC1");
    	JsonJavaObject responseJson = customerManagement.registerCustomer(customer);
    	String customerId = "" + responseJson.getAsLong("Long");
    	out.println("Registered customer Id: " + customerId + "<br/>");

		JsonEntity jsonEntity = customerManagement.getCustomerById(customerId);

		customerManagement.unregisterCustomer(customerId);
		
		try {
			jsonEntity = customerManagement.getCustomerById(customerId);			
		} catch (BssException be) {
			String responseCode = be.getResponseCode();
			if("404".equals(responseCode)) {
				out.println("Unregistered customer Id: " + customerId);
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error unregistering customer caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>