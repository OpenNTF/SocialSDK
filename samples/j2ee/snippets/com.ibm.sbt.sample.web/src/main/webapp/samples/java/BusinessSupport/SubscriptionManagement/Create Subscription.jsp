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
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page import="com.ibm.sbt.services.client.base.JsonEntity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.*"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.BssService.BillingFrequency"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>Create Subscription</title>
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
			
		OrderJsonBuilder order = new OrderJsonBuilder();
		order.setCustomerId(customerId)
			 .setDurationUnits(SubscriptionManagementService.DurationUnits.YEARS)
		     .setDurationLength(3)
		     .setPartNumber("D0NWLLL")
		     .setPartQuantity(5)
		     .setBillingFrequency(BillingFrequency.ARC);
		
		SubscriptionManagementService subscriptionManagement = new SubscriptionManagementService("smartcloud");
		EntityList<JsonEntity> subscriptionList = subscriptionManagement.createSubscription(order);
		for (JsonEntity subscription : subscriptionList) {
			if((null!=subscription.getAsString("SubscriptionId"))
					&&("D0NWLLL".equals(subscription.getAsString("PartNumber")))){
				out.println("<pre>" + subscription.toJsonString(false) + "<pre/>");
			}
		}	
	}
	
	catch (BssException be) {
		out.println("<pre>");
		out.println("Error creating subscription caused by: "+ be.getResponseJson());
		out.println("</pre>");	
	}
	
	catch (Exception e) {
		out.println("<pre>");
		out.println("Error creating subscription caused by: "+e.getMessage());
		out.println("</pre>");	
	}
	%>
	</div>
</body>

</html>