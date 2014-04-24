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
<title>Unassign Role</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
	try {
    	final SubscriberManagementService subscriberManagement = new SubscriberManagementService("smartcloud");
    	final AuthorizationService authorizationService = new AuthorizationService("smartcloud");
		EntityList<JsonEntity> subscriberList = subscriberManagement.getSubscribers();
		if (!subscriberList.isEmpty()) {
			JsonEntity subscriber = subscriberList.get(1);
			final String loginName = subscriber.getAsString("Person/EmailAddress");
			final String[] roles = authorizationService.getRoles(loginName);
			
			try{
				authorizationService.unassignRole(loginName, "CustomerAdministrator");
			}catch (BssException be){
				JsonJavaObject jsonObject = be.getResponseJson();
    			out.println("Error unassigning roles caused by: <pre>" + JsonGenerator.toJson(JsonJavaFactory.instanceEx, jsonObject, false) + "</pre>");
			}
			
			final JspWriter fout = out;
			StateChangeListener stateChangeListener = new StateChangeListener() {
				@Override
				public void stateChanged(JsonEntity jsonEntity) {
					try {
						String[] finalRoles = authorizationService.getRoles(loginName);
						fout.println("Unassigned role(s) as requested <br/>");
						fout.println("List of current roles for: "+ loginName +" <br/>");
						fout.println("<ul>");
						for (String role : finalRoles) {
							fout.println("<li>" + role + "</li>");
						}
						fout.println("</ul>");
					} catch (Exception e) {
						try {
							fout.println(e.getMessage());
						} catch (Exception ex) {
						}
					}
				}
			};
			
			if (!authorizationService.waitRoleSetState(loginName,
					roles, 5, 1000, stateChangeListener)) {
				out.println("Timeout waiting for roles to be unassigned");
			}
			
		}
	} catch (Exception e) {
		e.printStackTrace();
		out.println("Error unassigning roles caused by: "+e.getMessage());    		
	}
	
	%>
	</div>
</body>

</html>