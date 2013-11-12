<!-- /*
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
 */-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Collection"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumList"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Get Forums</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
		try {
			CommunityService communityService = new CommunityService();
			Collection<Community> communities = communityService.getPublicCommunities();
			Community community = communities.iterator().next();
			
			ForumList forums = communityService.getForums(community.getCommunityUuid());
			if(forums.getTotalResults() > 0 ){
				out.println("<br>Listing forums of a Community <br>");
				for (BaseForumEntity forum : forums) {
					out.println("<b>Forum Title : </b> " + forum.getTitle()+"<br>");
					out.println("<b>Forum Uuid : </b> " + forum.getUid()+"<br>");
					out.println("<b>Forum updated : </b> " + forum.getUpdated()+"<br>");
					out.println("<b>Forum published : </b> " + forum.getPublished()+"<br><br>");
				}
			}
			else
				out.println("No Forums found for this Community");
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>