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
<%@page import="com.ibm.sbt.services.client.smartcloud.communities.Community"%> 
<%@page import="com.ibm.sbt.services.client.smartcloud.communities.CommunityService"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="org.w3c.dom.Node"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Get Community by ID</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<%
		String communityUuid = Context.get().getProperty("sample.smartcloud.communityId1");
		CommunityService svc = new CommunityService();
		Community<Node> community = svc.getCommunityEntry(communityUuid,true);
		out.println("<b>Community Details</b>");
		out.println("<br>");
		out.println("<b>Title: </b>"+community.get("title"));
		out.println("<br>");
		out.println("<b>Summary: </b>"+community.get("summary"));
		out.println("<br>");
		out.println("<b>Community owner: </b>"+community.get("author"));
	%>
	 <br>
</body>
</html>