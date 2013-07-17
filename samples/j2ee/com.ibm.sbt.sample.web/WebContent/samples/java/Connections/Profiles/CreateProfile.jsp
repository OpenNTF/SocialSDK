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
		Profile profile = service.newProfile();
		long random = System.currentTimeMillis();
		profile.setAsString("guid",	"testUser"+ random);
		profile.setAsString("email","testUser"+ random+"@renovations.com");
		profile.setAsString("uid", "testUser"+ random);
		profile.setAsString("distinguishedName","CN=testUser"+random+"def,o=renovations");
		profile.setAsString("displayName", "testUser"+ random);
		profile.setAsString("givenNames", "testUser"+ random);
		profile.setAsString("surname", "testUser"+ random);
		profile.setAsString("userState", "active");
		service.createProfile(profile);
		profile = service.getProfile("testUser"+ random+"@renovations.com");
		out.println("<b> Profile Created :with display name "+ profile.getDisplayName());
	} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</body>
</html>