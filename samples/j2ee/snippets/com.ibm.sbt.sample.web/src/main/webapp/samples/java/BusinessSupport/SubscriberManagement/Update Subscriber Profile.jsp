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
<title>Update Subscriber Profile</title>
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
					
		SubscriberManagementService subscriberManagement = new SubscriberManagementService("smartcloud");
		JsonEntity jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
		
		JsonJavaObject rootObject = jsonEntity.getJsonObject();
		
		JsonJavaObject subscriberObject = rootObject.getAsObject("Subscriber");
		JsonJavaObject personObject = subscriberObject.getAsObject("Person");
		personObject.putString("GivenName", "Fred");
		personObject.putString("WorkPhone", "800-666-1234");
		
		subscriberManagement.updateSubscribeProfile(rootObject);
		
		jsonEntity = subscriberManagement.getSubscriberById(subscriberId);
		out.println("<pre>" + jsonEntity.toJsonString(false) + "<pre/>");

	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error updating subscriber profile caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>
