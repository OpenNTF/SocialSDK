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
<%@page import="com.ibm.sbt.services.client.connections.profiles.ColleagueConnection"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ColleagueConnectionList"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Check Colleague</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%
	try {
	
		String sourceUserId = Context.get().getProperty("sample.id1");
		String targetUserId;
		ProfileService connProfSvc = new ProfileService();
		ColleagueConnectionList colleagues = connProfSvc.getColleagueConnections(sourceUserId);
		if(colleagues != null && ! colleagues.isEmpty()) {
			ColleagueConnection colleague = colleagues.iterator().next();
			targetUserId = colleague.getContributorUserId();
			ColleagueConnection connection = connProfSvc.checkColleague(sourceUserId, targetUserId);
			out.println("connection Id : "+connection.getConnectionId());
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
