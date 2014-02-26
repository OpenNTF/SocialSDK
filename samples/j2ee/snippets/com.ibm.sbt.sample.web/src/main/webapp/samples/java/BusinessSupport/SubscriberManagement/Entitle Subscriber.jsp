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
<%@page import="java.io.IOException"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.sample.bss.BssUtil"%>
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
<title>Get Customers</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
		// Step 1. Create customer
		String customerId = BssUtil.registerCustomer("smartcloudC1");
		
		// Step 2. Add Subscriber
		final String subscriberId = BssUtil.addSubscriber("smartcloudC1", customerId);

		// Step 3. Activate the subscriber
		BssUtil.activateSubscriber("smartcloudC1", subscriberId);
		
		// Step 4. Create "IBM SmartCloud Connections" Subscription
		final String subscriptionId = BssUtil.createSubscription("smartcloudC1", customerId, 3, "D0NWLLL", 5);
		
		// Step 5. Entitle subscriber
		final JspWriter fout = out;
		final SubscriberManagementService subscriberManagement = new SubscriberManagementService("smartcloudC1");
		StateChangeListener listener = new StateChangeListener() {
			@Override
			public void stateChanged(JsonEntity jsonEntity) {
				try {
					JsonEntity entitlement = subscriberManagement.entitleSubscriber(subscriberId, subscriptionId, true);
					
					try {
						fout.println("Entitlement: <br/>");
						fout.println("<pre>" + entitlement.toJsonString(false) + "</pre>");
					} catch (IOException ioe) {}
				} catch (BssException be) {
		    		JsonJavaObject jsonObject = be.getResponseJson();
		    		
		    		try {
		    			fout.println("Error entitling subscriber caused by: <pre>" + JsonGenerator.toJson(JsonJavaFactory.instanceEx, jsonObject, false) + "</pre>");
		    		} catch (Exception e) {}
		    	} 
			}
		};
		SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService("smartcloudC1");
		if (!subscriptionManagement.waitSubscriptionState(subscriptionId, "ACTIVE", 5, 1000, listener)) {
			out.println("Timeout waiting for subscription to activate");
		}
		
		// Optional: Check seats
		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
		JsonJavaObject rootObject = jsonEntity.getJsonObject();
		JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
		List<Object> seatSet = subscriberObject.getAsList("SeatSet");
		out.println("Subscriber Seat Set: <br/>");
		out.println("<ul>");
		for (Object seat : seatSet) {
			out.println("<li><pre>" + seat + "</pre></li>");
		}		
		out.println("</ul>");
		
	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error entitling subscriber list caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>