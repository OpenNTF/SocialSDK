<!-- /*
 * � Copyright IBM Corp. 2012
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
<%@page import="com.ibm.sbt.services.client.smartcloud.profiles.Profile"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.profiles.ProfileService"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SmartCloud ProfileService Java </title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	
	
	<%
		try {		
				ProfileService service = new ProfileService("smartcloud"); //If no endpoint is specified, it uses default end point i.e connections
				Profile profile = service.getMyProfile();
				if(profile != null) 
				{
					out.println("<B>Profile </B> ");
					out.println("<br>");
					out.println("<br>");
					out.println("<B> Name : 	</B>" + profile.getDisplayName());
					out.println("<br>");
					out.println("<B> Email : 	</B>" + profile.getEmail());
					out.println("<br>");
					out.println("<B> ThumbnailUrl : 	</B>" + profile.getThumbnailUrl());
					out.println("<br>");
					out.println("<B> Id : 	</B>" + profile.getId());
					out.println("<br>");
					out.println("<B> About Me : 	</B>" + profile.getAbout());
					out.println("<br>");
					out.println("<B> Address : 	</B>" + profile.getAddress());
					out.println("<br>");
					out.println("<B> Country : 	</B>" + profile.getCountry());
					out.println("<br>");
					out.println("<B> Department : 	</B>" + profile.getDepartment());
					out.println("<br>");
					out.println("<B> Phone Number : 	</B>" + profile.getTelephoneNumber());
					out.println("<br>");
					out.println("<B> Profile Url : 	</B>" + profile.getProfileUrl());
					out.println("<br>");
					out.println("<B> Title : 	</B>" + profile.getJobTitle());
					out.println("<br>");
				}
				else
				{
					out.println("No Results");
				}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");	
		}	
		%>
	 <br>
</body>
</html>