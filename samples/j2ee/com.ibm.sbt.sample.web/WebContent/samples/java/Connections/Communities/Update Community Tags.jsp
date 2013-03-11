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
<%@page import="java.util.Collection"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%> 
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="java.util.HashMap"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Update Community Tags</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<h4>Update Community Tags</h4>
	<div id="content">
	<%
	try {
		CommunityService communityService = new CommunityService();
		Collection<Community> communities = communityService.getPublicCommunities();
		Community community = communities.iterator().next();
		community.setTitle("Test Community" + System.currentTimeMillis());
		community.setContent("Test Community updated by Update Community Java sample");
		community.setAddedTags("NewTag");
		communityService.updateCommunity(community);
		
		community = communityService.getCommunity(community.getCommunityUuid());
		 
		out.println("Community Tags:");
		out.println("<br>");
		String[] tags = community.getTags();
		for (int i = 0; i < tags.length; i++) {
			out.println(tags[i]);
			out.println("<br>");
		}
	} catch (Throwable e) {
		out.println("<pre>");
		e.printStackTrace(new PrintWriter(out));
		out.println("</pre>");
	}
	%>
	</div>
</body>
</html>