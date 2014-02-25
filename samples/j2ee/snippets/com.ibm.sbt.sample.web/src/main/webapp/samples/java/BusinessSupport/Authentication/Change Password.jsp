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
<title>Register Customer</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
    	CustomerManagementService customerManagement = new CustomerManagementService("smartcloudC1");
    	SubscriberManagementService subscriberManagement = new SubscriberManagementService("smartcloudC1");
		AuthenticationService authenticationService = new AuthenticationService("smartcloudC1");

		EntityList<JsonEntity> customerList = customerManagement.getCustomers();
		if (!customerList.isEmpty()) {
			JsonEntity customer = customerList.get(0);
			String customerId = "" + customer.getAsLong("Id");

    		SubscriberJsonBuilder jsonBuilder = new SubscriberJsonBuilder();
    		jsonBuilder.setCustomerId(customerId)
    				  .setRole(SubscriberManagementService.Role.User)
    				  .setFamilyName("Doe")
    				  .setGivenName("John")
    				  .setEmailAddress("ibmsbt_"+System.currentTimeMillis()+"@mailinator.com")
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
    		
        	JsonJavaObject responseJson = subscriberManagement.addSubsciber(jsonBuilder);
        	String subscriberId = String.valueOf(responseJson.getAsLong("Long"));
        	out.println("Added new subscriber: "+subscriberId+"<br/>");
        	
        	JsonEntity subscriber = subscriberManagement.getSubscriberById(subscriberId);
    		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
        	out.println("Retrieved subscriber: "+loginName+"<br/>");
        	
    		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
    		userCredential.setLoginName(loginName)
    					  .setNewPassword("one_time_***REMOVED***");
    		
    		authenticationService.setOneTimePassword(userCredential);
    		
    		userCredential = new UserCredentialJsonBuilder();
    		userCredential.setLoginName(loginName)
    					  .setOldPassword("one_time_***REMOVED***")
    					  .setNewPassword("new_***REMOVED***")
    					  .setConfirmPassword("new_***REMOVED***");
    		
    		authenticationService.changePassword(userCredential);
			
			out.println("Changed password for: "+loginName+"<br/>");
		}

	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error changing password caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>