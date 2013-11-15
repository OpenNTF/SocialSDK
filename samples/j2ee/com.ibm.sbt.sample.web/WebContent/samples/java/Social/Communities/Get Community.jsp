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
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT JAVA Sample - Get Community</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%
		try {
			CommunityService svc = new CommunityService();
			CommunityList communities = svc.getPublicCommunities();
			Community community = communities.iterator().next();
		
			community = svc.getCommunity(community.getCommunityUuid());
			String title = community.getTitle();
			out.println("<b>Title is: </b>" + title);
			out.println("<b>Type : </b>" + community.getCommunityType()+"<br>");
			out.println("<b>Url : </b>" + community.getCommunityUrl()+"<br>");
			out.println("<b>uuid : </b>" + community.getCommunityUuid()+"<br>");
			out.println("<b>content : </b>" + community.getContent()+"<br>");
			out.println("<b>community url : </b>" + community.getCommunityUrl()+"<br>");
			out.println("<b>logo url : </b>" + community.getLogoUrl()+"<br>");
			out.println("<b>member count : </b>" + community.getMemberCount()+"<br>");
			out.println("<b>member url : </b>" + community.getMembersUrl()+"<br>");
			out.println("<b>summary : </b>" + community.getSummary()+"<br>");
			
			
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while fetching a community: " + e.getMessage());
			
		}
	%>
	</div>
</body>
</html>