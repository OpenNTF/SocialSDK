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
	<title>Move Entry to Section</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Move Entry to Section</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		EntityList<Activity> activities = activityService.getMyActivities();
		
		if(activities != null && !activities.isEmpty()) {
			ActivityNode node = new ActivityNode(activityService, activities.get(0).getActivityUuid());
			node.setType(ActivityNode.TYPE_ENTRY);
			node.setTitle("Entry under Section.." + System.currentTimeMillis());
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag2");
			node.setTags(tagList);
			node.setContent("Entry moved under Section Programmatically");
			node = activityService.createActivityNode(node);
			
			ActivityNode sectionNode = new ActivityNode(activityService, activities.get(0).getActivityUuid());
			sectionNode.setType(ActivityNode.TYPE_SECTION);
			sectionNode.setTitle("sectionNode from JSP " + System.currentTimeMillis());
			sectionNode.setContent("SectionNode Content " + System.currentTimeMillis());
			sectionNode.setPosition(2000);	
			sectionNode = activityService.createActivityNode(sectionNode);
			
			node.setInReplyTo(new InReplyTo(sectionNode));
			activityService.updateActivityNode(node);
			
			out.println("Entry : " + node.getTitle() + " moved to Section : " + sectionNode.getTitle());
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