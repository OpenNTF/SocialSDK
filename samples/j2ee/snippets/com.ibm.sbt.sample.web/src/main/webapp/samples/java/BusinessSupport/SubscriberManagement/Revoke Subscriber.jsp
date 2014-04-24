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
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.util.io.json.*"%>
<%@page import="com.ibm.sbt.services.client.base.JsonEntity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.*"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>Revoke Subscriber</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
		String customerId = Context.get().getProperty("bss.customerId");
		out.println("Customer Id: " + customerId + "<br/>");
		if (StringUtil.isEmpty(customerId)) {
			out.println("Please provide a valid customer id in the sbt.properties.");
			return;
		}
			
		final String subscriberId = Context.get().getProperty("bss.subscriberId");
		out.println("Subscriber Id: " + subscriberId + "<br/>");
		if (StringUtil.isEmpty(subscriberId)) {
			out.println("Please provide a valid subscriber id in the sbt.properties.");
			return;
		}		
		
		SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService("smartcloud");		
		EntityList<JsonEntity> subscriptions = subscriptionManagement.getSubscriptionsById(customerId);
		long oid = 0L;
		for (JsonEntity subscription : subscriptions) {
			oid = subscription.getAsLong("Oid");
			String partNumber = subscription.getAsString("PartNumber");
			// part number for IBM SmartCloud Connections or IBM SmartCloud Engage for Enterprise Deployment - ASL
			if ("D0NWLLL".equalsIgnoreCase(partNumber) ||
				"D0NPULL".equalsIgnoreCase(partNumber) ||
				"D0NWKLL".equalsIgnoreCase(partNumber)) {
				break;
			}
		}
		if (oid == 0) {
			out.println("Unable to find a base subscription.");
			return;
		}
		final String subscriptionId = "" + oid;
		final SubscriberManagementService subscriberManagement = new SubscriberManagementService("smartcloud");
		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
		JsonJavaObject rootObject = jsonEntity.getJsonObject();
		JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
		List<Object> seatSet = subscriberObject.getAsList("SeatSet");
		
		JsonJavaObject seat = null;
		String seatId = "";
		if(!seatSet.isEmpty()){
			for (Object seatObj : seatSet) {
    			String nextSubscriptionId = "" + ((JsonJavaObject)seatObj).getAsLong("SubscriptionId");
    			if (subscriptionId.equals(nextSubscriptionId)) {
    				seat = (JsonJavaObject)seatObj;
    			}
    		}
		
			long seatIdLong = 0L;
			seatIdLong = seat.getAsLong("Oid");
			out.println("Subscription ID: " + subscriptionId + "<br/>");
			seatId = "" + seatIdLong;
		
			try{
				subscriberManagement.revokeSubscriber(subscriberId, seatId, false);
				out.println("Revoked Seat: <br/>");
				out.println("<pre>" + seatId + "</pre>");
			
			} catch (BssException be){
				JsonJavaObject jsonObject = be.getResponseJson();
    			out.println("Error revoking subscriber caused by: <pre>" + JsonGenerator.toJson(JsonJavaFactory.instanceEx, jsonObject, false) + "</pre>");
			}
		} else{
			out.println("No seat assigned to subscriber.");
		}

	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error revoking subscriber list caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>