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
	
		ProfileAdminService service = new ProfileAdminService(); 
		long random = System.currentTimeMillis();
		Profile profile = new Profile(service, Context.get().getProperty("sample.createProfileUid")+random);
		profile.setAsString("guid",	Context.get().getProperty("sample.createProfileId")+random);
		profile.setAsString("uid", Context.get().getProperty("sample.createProfileUid")+random);
		profile.setAsString("distinguishedName",Context.get().getProperty("sample.createProfileDistinguishedName"));
		profile.setAsString("displayName", Context.get().getProperty("sample.createProfileDisplayName")+random);
		profile.setAsString("givenNames", Context.get().getProperty("sample.createProfileGivenNames"));
		profile.setAsString("surname", Context.get().getProperty("sample.createProfileSurName"));
		profile.setAsString("userState", Context.get().getProperty("sample.createProfileUserState"));
		service.createProfile(profile);
		profile = service.getProfile(Context.get().getProperty("sample.createProfileId")+random);
		out.println("Profile Created :with display name "+ profile.getName());
	} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</body>
</html>