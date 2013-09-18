<!-- 
/*
 * © Copyright IBM Corp. 2013
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
 */ -->
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ProfileService"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.Profile"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ConnectionEntry"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ConnectionEntryList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<html>
<head>
	<title>SBT JAVA Sample - Check Colleague</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%
	try{
		String sourceUserId = Context.get().getProperty("sample.id1");
		String targetUserId;
		ProfileService connProfSvc = new ProfileService();
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("outputType", "connection");
		
		ConnectionEntryList colleagues = (ConnectionEntryList)connProfSvc.getColleagues(sourceUserId, parameters);
		
		if(colleagues != null && ! colleagues.isEmpty()) {
			targetUserId = colleagues.get(0).getContributorUserId();
			ConnectionEntry connection = connProfSvc.checkColleague(sourceUserId, targetUserId);
			out.println("User's are colleagues with id's"+sourceUserId+" and "+ targetUserId+" the connection Id is : "+connection.getConnectionId());
		}
		else
			out.println("no colleagues exist for the user");
	}  catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
</body>
</html>
