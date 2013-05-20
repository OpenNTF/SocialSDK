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
<%@page import="java.util.Collection"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%> 
<%@page import="com.ibm.sbt.services.client.connections.communities.Member"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="java.util.HashMap"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Add Community Member</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<h4>Add & Remove a Community Member</h4>
	<div id="content">
	<%
	try {
		CommunityService communityService = new CommunityService();
		Collection<Community> communities = communityService.getPublicCommunities();
		Community community = communities.iterator().next();
		Member[] members = communityService.getMembers(community);
		if (members.length == 1) {
		    String email = Context.get().getProperty("sample.email2");
			Member member = new Member(communityService, email);
			boolean added = communityService.addMember(community, member);
			out.println("Added " + member.getId() + " : " + added);

			community = communityService.getCommunity(community.getCommunityUuid());
			out.println("<br/>");

			boolean removed = communityService.removeMember(community, member);
			out.println("Removed " + member.getId() + " result : " + removed);
		} else {
			Member member = members[members.length-1];
			boolean removed = communityService.removeMember(community, member);
			out.println("Removed " + member.getName() + " result:" + removed);

			community = communityService.getCommunity(community.getCommunityUuid());
			out.println("<br/>");

			boolean added = communityService.addMember(community, member);
			out.println("Added " + member.getName() + " : " + added);
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