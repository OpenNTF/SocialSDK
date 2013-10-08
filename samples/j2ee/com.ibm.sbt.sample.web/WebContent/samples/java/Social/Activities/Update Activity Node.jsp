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
<%@page import="com.ibm.sbt.services.client.connections.activity.model.ActivityNodeContentType"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.model.ActivityNodeType"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.DateField"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.FieldList"%>
<%@page import="com.ibm.sbt.services.client.connections.activity.TextField"%>
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
		ActivityList activities = activityService.getMyActivities();
		
		Activity activity = activities.get(0);
		
		ActivityNode actNode = new ActivityNode(activityService, activity.getActivityId());
		actNode.setEntryType(ActivityNodeType.Entry.getActivityNodeType());
		actNode.setTitle("ActivityNode with Fields" + System.currentTimeMillis());
		actNode.setContent("ActivityNodeContent");
		actNode.setContentType(ActivityNodeContentType.Html.getActivityNodeContentType());
		Field textField1 = new TextField("Summary JSP 1");
		textField1.setFieldName("MyTextField1");
		Field textField2 = new TextField("Summary JSP 2");
		textField2.setFieldName("MyTextField2");
		List<Field> fieldList = new ArrayList<Field>();
		fieldList.add(textField1);
		fieldList.add(textField2);
		actNode.setFields(fieldList);
		List<String> tagList = new ArrayList<String>();
		tagList.add("ABCTag");
		actNode.setTags(tagList);
		actNode = activityService.createActivityNode(actNode);
		out.println("<br>Activity Node Before Updation : " + actNode.getTitle() + " , Type : " + actNode.getEntryType());

		// updating now
		actNode.setTitle(actNode.getTitle() +"Updated");
		FieldList list = actNode.getTextFields();
		if(list != null && !list.isEmpty()) {
			list.get(0).setFieldName(list.get(0).getName()+"Updated");
		}
		Field dateField = new DateField(new Date());
		dateField.setFieldName("MyDateFieldUP");
		List<Field> updatedList = new ArrayList<Field>();
		updatedList.add(dateField);
		updatedList.add(list.get(0));
		actNode.setFields(updatedList);
		activityService.updateActivityNode(actNode);
		actNode = activityService.getActivityNode(actNode.getActivityId());
		out.println("<br>Activity Node After Updation : " + actNode.getTitle() + " , Type : " + actNode.getEntryType());
		
	} catch (Throwable e) {
		out.println("<pre>");
		out.println(e.getMessage());
		out.println("</pre>");	
	}					
	%>
	</div>
</body>
</html>