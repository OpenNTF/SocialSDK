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
<%@page import="com.ibm.sbt.services.client.connections.activities.DateField"%>
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
	<title>Update Activity Node</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Update Activity Node</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		EntityList<Activity> activities = activityService.getMyActivities();
		
		if(activities != null && !activities.isEmpty()) {
			Activity activity = activities.get(0);
			
			ActivityNode actNode = new ActivityNode(activityService, activity.getActivityUuid());
			actNode.setType(ActivityNode.TYPE_ENTRY);
			actNode.setTitle("ActivityNode with Fields" + System.currentTimeMillis());
			actNode.setContent("ActivityNodeContent");
			TextField textField1 = new TextField();
			textField1.setName("MyTextField1");
			textField1.setSummary("Summary JSP 1");
			TextField textField2 = new TextField();
			textField2.setName("MyTextField2");
			textField2.setSummary("Summary JSP 2");
			List<Field> fieldList = new ArrayList<Field>();
			fieldList.add(textField1);
			fieldList.add(textField2);
			actNode.setFields(fieldList);
			List<String> tagList = new ArrayList<String>();
			tagList.add("ABCTag");
			actNode.setTags(tagList);
			actNode = activityService.createActivityNode(actNode);
			out.println("<br>Activity Node Before Updation : " + actNode.getTitle() + " , Type : " + actNode.getType());
	
			// updating now
			actNode.setTitle(actNode.getTitle() +"Updated");
			Field[] fields = actNode.getFields();
			if(fields != null && fields.length > 0) {
				fields[0].setName(fields[0].getName()+"Updated");
			}
			DateField dateField = new DateField();
			dateField.setName("MyDateFieldUP");
			dateField.setDate(new Date());
			actNode.addField(dateField);
			activityService.updateActivityNode(actNode);
			actNode = activityService.getActivityNode(actNode.getActivityUuid());
			out.println("<br>Activity Node After Updation : " + actNode.getTitle() + " , Type : " + actNode.getType());
		}  else {
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