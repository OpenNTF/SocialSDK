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
<%@page	import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Member"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.MemberList"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT JAVA Sample - Get Community Details</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%
		try {
		
			CommunityService communityService = new CommunityService();
			Collection<Community> communities = communityService.getPublicCommunities();
			Community community = communities.iterator().next();
			CommunityList subCommunities = community.getSubCommunities(); 
			if(subCommunities.getTotalResults() > 0 ){
				out.println("<br>Listing sub communities of a Community <br>");
				for (Community subCommunity : subCommunities) {
					out.println("<b>Sub-Community Title : </b> " + subCommunity.getTitle());
					out.println("<br>");
				}
			}
			MemberList members = community.getMembers(); 
			out.println("<br>Listing members of a Community <br>");
			for (Member member : members) {
				out.println("<b>Member name : </b> " + member.getName());
				out.println("<br>");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while fetching community details: " + e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>