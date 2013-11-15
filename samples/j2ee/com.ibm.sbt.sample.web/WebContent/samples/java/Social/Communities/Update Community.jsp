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
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Update Community</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<div id="content">
	<%
	try {
		CommunityService communityService = new CommunityService();
		CommunityList communities = communityService.getMyCommunities();
		if(communities!=null && !communities.isEmpty()){
			Community community = communities.iterator().next();
			community.setTitle("Test Community" + System.currentTimeMillis());
			community.setContent("Test Community updated by Update Community Java snippet");
			community.setCommunityType("public");
			List<String> tags = new ArrayList<String>();
			tags.add("demotag");
			community.setTags(tags);
			communityService.updateCommunity(community);
			community = communityService.getCommunity(community.getCommunityUuid());
			out.println("Community updated is: " + community.getCommunityUuid());
			out.println("Community Tags:");
			out.println("<br>");
			tags = community.getTags();
			for (int i = 0; i < tags.size(); i++) {
				out.println(tags.get(i));
				out.println("<br>");
			}
		}
		else
			out.println("No community exist to update");
	} catch (Exception e) {
		out.println("<pre>");
		out.println(e.getMessage());
		out.println("</pre>");
	}
	%>
	</div>
</body>
</html>