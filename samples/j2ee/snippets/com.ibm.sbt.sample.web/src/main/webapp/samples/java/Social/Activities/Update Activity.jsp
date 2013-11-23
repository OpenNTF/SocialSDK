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
	<title>Update Activity</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Update Activity</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		ActivityList activities = activityService.getMyActivities();
		
		if(activities != null && !activities.isEmpty()) {
			Activity activity = activities.get(0);

			out.println("Activity before updation : " + activity.getTitle());
			activity.setTitle("UpdatedBySample" + System.currentTimeMillis());
			activity.setGoal("GoalOfActivity updated - " + System.currentTimeMillis());
			
			List<String> tagList = new ArrayList<String>();
			tagList.add("tag123");
			tagList.add("tagabc");
			activity.setTags(tagList);
			activity.setDueDate(new Date());
			
			activityService.updateActivity(activity);
			out.println("Activity after updation : " + activityService.getActivity(activity.getActivityId()).getTitle() );
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