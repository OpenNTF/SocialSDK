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
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ProfileAdminService"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.Profile"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<title>Add Profile</title>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	</head>
	<body>
	<%
	try {					
		ProfileAdminService service = new ProfileAdminService(); //If no endpoint is specified, it uses default end point i.e connections
		Profile profile = service.getProfile("testUser61@renovations.com", false);
		String email = "testUser61@renovations.com";

		profile.set("guid", "testUserD9A04-F2E1-1222-4825-7A700026E92C");
		profile.set("email", email );
		profile.set("uid", "testUser");
		profile.set("distinguishedName", "CN=testUser61 def,o=renovations");
		profile.set("displayName", "testUser61");
		profile.set("givenNames", "testUser");
		profile.set("surname", "testUser");
		profile.set("userState", "active");

		boolean success = service.createProfile(profile);
		if(success){
			profile = service.getProfile(email);
			out.println("User Profile created with Name : "+ profile.getDisplayName());
		}
		else{
			out.println("error while trying to create Profile");
		}
	} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</body>
</html>