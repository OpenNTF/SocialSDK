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
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="java.util.Collection"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Create Community</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<h4>Create Community</h4>
	<div id="content">
	<%
	try {
		CommunityService communityService = new CommunityService();
		Community community =  new Community(communityService, "");
		community.setTitle("Test Community 1ab" + System.currentTimeMillis()); 
		community.setContent("");
		community.setCommunityType("public");
		List<String> tags = new ArrayList<String>();
		tags.add("tag1"); 
		tags.add("tag2"); 
		community.setTags(tags);
		community = community.save(); 
		out.println("Community created with Id : " + community.getTitle());
		out.println("Community Tags:");
		out.println("<br>");
		tags = community.getTags();
		for (int i = 0; i < tags.size(); i++) {
			out.println(tags.get(i));
			out.println("<br>");
		}
	} catch (Exception e) {
		out.println("<pre>");
		out.println(e.getMessage());
		out.println("</pre>");
	}
	%>
	</div>
</body>
</html>