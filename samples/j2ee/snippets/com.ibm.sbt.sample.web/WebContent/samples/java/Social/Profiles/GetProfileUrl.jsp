<!-- 
/*
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
 */ -->
 
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ProfileService"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.Profile"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Get Profile url</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%
	try {
			String userId = Context.get().getProperty("sample.id1");
			ProfileService connProfSvc = new ProfileService();
			Profile profile = connProfSvc.getProfile(userId);
			if(profile != null){
				if (StringUtil.isNotEmpty(profile.getProfileUrl())) {
					out.println(profile.getProfileUrl());
				}else {
						out.println("No information found");
					}
			}
			else{
					out.println("error encountered in getting required Profile information");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>
