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
<%@page import="com.ibm.sbt.services.client.connections.activity.model.ActivityNodeType"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.FieldList"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.Field"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.ActivityNode"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.Activity"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.ActivityList"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.ActivityService"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>  
 
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Activity Service API</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		Activity activity = activityService.getMyActivities().get(0);
		
		ActivityNode todoNode = new ActivityNode(activityService, activity.getActivityId());
		todoNode.setEntryType(ActivityNodeType.ToDo.getActivityNodeType());
		todoNode.setContent("Todo Node Content " + System.currentTimeMillis());
		todoNode.setTitle("todoNode from JSP " + System.currentTimeMillis());
		List<String> tagList = new ArrayList<String>();
		tagList.add("todoNodeTag");
		todoNode.setTags(tagList);
		todoNode.setPosition(1000);
		todoNode.setDueDate(new Date()); 
		
		todoNode.setAssignedTo("Frank Adams", "0EE5A7FA-3434-9A59-4825-7A7000278DAA");
		//todoNode.setIcon();

		todoNode.setInReplyTo("urn:lsid:ibm.com:oa:2b87fab8-4526-4636-9ec0-39aff6902ed5", "https://qs.renovations.com:444/activities/service/atom2/forms/activitynode?activityNodeUuid=2b87fab8-4526-4636-9ec0-39aff6902ed5");		
		todoNode = activityService.createActivityNode(todoNode);
		out.println("Todo Node Created : " + todoNode.getId());
	} catch (Throwable e) {
		out.println("<pre>");
		out.println(e.getMessage());
		out.println("</pre>");	
	}					
	%>
	</div>
</body>
</html>