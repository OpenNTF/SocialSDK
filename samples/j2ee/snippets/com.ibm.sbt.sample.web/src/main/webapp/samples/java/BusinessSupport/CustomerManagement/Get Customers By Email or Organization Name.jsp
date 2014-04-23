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
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page import="com.ibm.sbt.services.client.base.JsonEntity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.bss.*"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>Get Customers By Email or Organization Name</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
    	CustomerManagementService customerManagement = new CustomerManagementService("smartcloud");
    	SubscriberManagementService subscriberManagement = new SubscriberManagementService("smartcloud");
    	EntityList<JsonEntity> customerList = customerManagement.getCustomers();
    	EntityList<JsonEntity> subscriberList = subscriberManagement.getSubscribers();
    	
    	if(!customerList.isEmpty()){
    		JsonEntity customer = customerList.get(0);
    		String contactEmail = customer.getAsString("Organization/Contact/EmailAddress");
    		EntityList<JsonEntity> listByContactEmail = customerManagement.getCustomersByContactEmail(contactEmail);
			out.println("Id's of customers by Contact Email Address: " + contactEmail + "<br/>");
			out.println("<ul>");
			for (JsonEntity cust : listByContactEmail) {
				long id = cust.getAsLong("Id");
				out.println("<li>" + id + "</li>");
			}
			out.println("</ul>");
			
    		String orgName = customer.getAsString("Organization/Contact/OrgName");
    		EntityList<JsonEntity> listByOrgName = customerManagement.getCustomerByOrgName(orgName);
			out.println("Id's of customers by Organization Name : " + orgName + "<br/>");
			out.println("<ul>");
			for (JsonEntity org : listByOrgName) {
				long id = org.getAsLong("Id");
				out.println("<li>" + id + "</li>");
			}
			out.println("</ul>");
    	}
  
    	if(!subscriberList.isEmpty()){
    		JsonEntity subscriber = subscriberList.get(0);
    		String subscriberEmail = subscriber.getAsString("Person/EmailAddress");
    		EntityList<JsonEntity> listBySubscriberEmail = customerManagement.getCustomersBySubscriberEmail(subscriberEmail);
			out.println("Id's of customers by Subscriber Email Address: " + subscriberEmail + "<br/>");
			out.println("<ul>");
			for (JsonEntity subscr : listBySubscriberEmail) {
				long id = subscr.getAsLong("Id");
				out.println("<li>" + id + "</li>");
			}
			out.println("</ul>");
    	}
    	
	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error retrieving customer list caused by: "+e.getMessage());    		
	}
	%>
	</div>
</body>

</html>