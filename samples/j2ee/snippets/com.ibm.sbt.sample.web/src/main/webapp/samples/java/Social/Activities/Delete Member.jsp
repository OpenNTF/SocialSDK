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
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.services.client.connections.common.Member"%>
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
	<title>Delete Member</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Delete Member</h4>
	<div id="content">
	<%
	try {		
		ActivityService activityService = new ActivityService();
		EntityList<Activity> activities = activityService.getMyActivities();
		if(activities != null && !activities.isEmpty()) {
			Activity activity = activities.get(0);
			EntityList<Member> members = activityService.getMembers(activity.getActivityUuid());
			Member memberToBeDeleted = null;
			for(Member member : members) {
				if(!StringUtil.equalsIgnoreCase(member.getRole(), "owner")) {
					memberToBeDeleted = member;
					break;
				}
			}
			
			if(memberToBeDeleted == null) { 
				out.println("No Member to be Deleted.");
			} else {
				activityService.deleteMember(activity.getActivityUuid(), memberToBeDeleted);
				out.println("Member deleted : " + memberToBeDeleted);
			}
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