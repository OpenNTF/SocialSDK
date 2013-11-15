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
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.MemberList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Member"%>

<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT JAVA Sample - Get Members</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
		try {
			CommunityService communityService = new CommunityService();
			CommunityList communities = communityService.getPublicCommunities();
			Community community = communities.iterator().next();
		
			MemberList members = communityService.getMembers(community.getCommunityUuid());
			
			out.println("<br>Listing Members of a Community <br>");
			for (Member member : members) {
				out.println("<b>member Name : </b> " + member.getName()+"<br>");
				out.println("<b> Member Email: </b>"+ member.getEmail()+"<br>");
				out.println("<b> Member Role: </b>"+ member.getRole()+"<br>");
				out.println("<br>");
			}
			
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while fetching members of a community: " + e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>