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
<%@page import="com.ibm.sbt.services.client.connections.activities.InReplyTo"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.Field"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.ActivityNode"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.Activity"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.ActivityService"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>  
 
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>Create Reply Node</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Create Reply Node</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		EntityList<Activity> activities = activityService.getMyActivities();
		
		if(activities != null && !activities.isEmpty()) {
			ActivityNode chatNode = new ActivityNode(activityService, activities.get(0).getActivityUuid());
			chatNode.setType(ActivityNode.TYPE_CHAT);
			chatNode.setTitle("Chatting .." + System.currentTimeMillis());
			chatNode.setContent("Jsp Content");
			chatNode = activityService.createActivityNode(chatNode);
			
			ActivityNode replyNode = new ActivityNode(activityService, activities.get(0).getActivityUuid());
			replyNode.setType(ActivityNode.TYPE_REPLY);
			replyNode.setTitle("reply to chat.." + System.currentTimeMillis());
			
			replyNode.setContent("Hi! Jsp" + System.currentTimeMillis());
			replyNode.setInReplyTo(new InReplyTo(chatNode));	
	
			replyNode = activityService.createActivityNode(replyNode);
			out.println("Reply Node Created : " + replyNode.getId());
		} else {
			out.println("No Activites Found");
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