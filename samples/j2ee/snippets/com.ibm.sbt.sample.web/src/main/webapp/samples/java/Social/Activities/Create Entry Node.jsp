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
<%@page import="com.ibm.sbt.services.client.connections.common.Link"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.LinkField"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.DateField"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.PersonField"%>
<%@page import="com.ibm.sbt.services.client.connections.activities.TextField"%>
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
	<title>Create Entry Node</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Create Entry Node</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		EntityList<Activity> activities = activityService.getMyActivities();
		
		if(activities != null && !activities.isEmpty()) {
			ActivityNode entryNode = new ActivityNode(activityService);
			entryNode.setActivityUuid(activities.get(0).getActivityUuid());
			entryNode.setType(ActivityNode.TYPE_ENTRY);
			entryNode.setTitle("EntryNode from JSP " + System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("entryNodeTag");
			entryNode.setTags(tagList);
			
			entryNode.setContent("Entry Node Content " + System.currentTimeMillis());
			entryNode.setPosition(1000);
			entryNode.setFlags("private");
			
			TextField textField = new TextField();
			textField.setName("MyTextField");
			textField.setSummary("Summary JSP");
			DateField dateField = new DateField();
			dateField.setName("MyDateField");
			dateField.setDate(new Date());
			LinkField linkField = new LinkField();
			linkField.setName("MyBookMarkField");
			linkField.setLink(new Link("BookGoogle", "www.google.com"));
			
	 		List<Field> fieldList = new ArrayList<Field>();
			fieldList.add(textField);
			fieldList.add(dateField);
			fieldList.add(linkField);
			
			entryNode.setFields(fieldList);		
			entryNode = activityService.createActivityNode(entryNode);// add act id here for consistency
			out.println("Entry Node Created : " + entryNode.getId());
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