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
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumTopic"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumService"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumList"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.Forum"%>
<%@page import="java.util.Collection"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Create Community Forum Topic</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<div id="content">
	<%
	try {
		
		CommunityService comService = new CommunityService();
		CommunityList comList = comService.getMyCommunities();
		if(comList.size() > 0 ){
		String communityId = comList.get(0).getCommunityUuid();
		ForumTopic topic = new ForumTopic(new ForumService(), "");
		topic.setTitle("Dummy Community based Forum Topic" + System.currentTimeMillis());
		topic.setContent("Dummy Community based Forum Content");
		ForumTopic createdTopic = comService.createForumTopic(topic, communityId);
		out.println("Topic created with Id : " + createdTopic.getTopicUuid() + "for Community with Id :"+communityId +"<br>");
		out.println("Topic Title : " + createdTopic.getTitle()+"<br>");
		out.println("Topic Content : " + createdTopic.getContent()+"<br>");
		}
		else{
			out.println("No Community exist to create forum topic");
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