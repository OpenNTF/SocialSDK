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
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.sample.bss.BssUtil"%>
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page import="com.ibm.sbt.services.client.base.JsonEntity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.*"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>Add Subscriber</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" title="Style" href="/sbt.bootstrap211/bootstrap/css/bootstrap.css"></link>
<link rel="stylesheet" type="text/css" title="Style" href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css"></link>
</head>

<body>
	<p class="text-info">
	The API caller must have one of the following roles to run the API:
	<ul>
		<li>CustomerAdministrator</li>
    	<li>VSR</li>
    </ul>
    </p>
	<div id="content">
	<table class="table table-condensed">
	<%
	try {
		String endpoint = BssUtil.getEndpoint(request);
		String customerId = BssUtil.getCustomerId(request);
		String domain = BssUtil.getDomain(request);
		out.println("<tr><td>Customer Id</td><td>" + customerId + "</td></tr>");
			
		SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService(endpoint);
   		SubscriberManagementService subscriberManagement = new SubscriberManagementService(endpoint);
   		AuthenticationService authenticationService = new AuthenticationService(endpoint);
		
		EntityList<JsonEntity> subscriptions = subscriptionManagement.getSubscriptionsById(customerId);
		String subscriptionId = null;
		for (JsonEntity subscription : subscriptions) {
			long oid = subscription.getAsLong("Oid");
			String partNumber = subscription.getAsString("PartNumber");
			// part number for IBM SmartCloud Connections or IBM SmartCloud Engage for Enterprise Deployment - ASL
			if ("D0NWLLL".equalsIgnoreCase(partNumber) || "D0NWKLL".equalsIgnoreCase(partNumber)) {
				subscriptionId = "" + oid;
				break;
			}
		}
		
		SubscriberJsonBuilder subscriberBuilder = new SubscriberJsonBuilder();
		subscriberBuilder.setCustomerId(customerId)
				  .setRole(SubscriberManagementService.Role.User)
				  .setFamilyName("Doe")
				  .setGivenName("Aaron")
				  .setEmailAddress(BssUtil.getUniqueEmail(domain))
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
		
    	JsonJavaObject responseJson = subscriberManagement.addSubscriber(subscriberBuilder);
    	String subscriberId = String.valueOf(responseJson.getAsLong("Long"));
		out.println("<tr><td>Subscriber Id</td><td>" + subscriberId + "</td></tr>");
		
		subscriberManagement.activateSubscriber(subscriberId);
		
   		JsonEntity subscriber = subscriberManagement.getSubscriberById(subscriberId);
   		String loginName = subscriber.getAsString("Subscriber/Person/EmailAddress");
		out.println("<tr><td>Login Name</td><td>" + loginName + "</td></tr>");
		
		UserCredentialJsonBuilder userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setNewPassword("one-time-123");
		authenticationService.setOneTimePassword(userCredential);

		userCredential = new UserCredentialJsonBuilder();
		userCredential.setLoginName(loginName)
					  .setOldPassword("one-time-123")
					  .setNewPassword("password1")
					  .setConfirmPassword("password1");
		
		authenticationService.changePassword(userCredential);
		
		subscriberManagement.entitleSubscriber(subscriberId, subscriptionId, true);
		
		subscriber = subscriberManagement.getSubscriberById(subscriberId);
		out.println("<tr><td colspan='2'><pre>" + subscriber.toJsonString(false) + "<pre/></td></tr>");
		
	} catch (BssException be) {
		out.println("<tr><td colspan='2'>Error provisioning user caused by: <pre>" + be.getResponseJson() + "</pre></td></tr>");
	} catch (Exception e) {
		out.println("<tr><td colspan='2'>Error provisioning user caused by: " + e.getMessage() + "</td></tr>");
	}
	%>
	</table>
	</div>
</body>

</html>
