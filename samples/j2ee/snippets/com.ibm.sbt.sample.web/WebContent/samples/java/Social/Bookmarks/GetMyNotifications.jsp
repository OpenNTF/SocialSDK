<!-- /*
 * © Copyright IBM Corp. 2012
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
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.bookmarks.BookmarkService"%>
<%@page import="com.ibm.sbt.services.client.connections.bookmarks.Bookmark"%>
<%@page import="com.ibm.sbt.services.client.connections.bookmarks.BookmarkList"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT JAVA Sample - Get My Notifications</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Get My Notifications</h4>
	<div id="content">
	<%
		try {
			BookmarkService svc = new BookmarkService();
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("ps", "5");
			BookmarkList bookmarks = svc.getMyNotifications();
			out.println("<br>Listing my bookmarks , Total bookmarks found : "+bookmarks.getTotalResults());
			out.println("<br>");
			for (Bookmark bookmark : bookmarks) {
					out.println("<b>Name : </b> " + bookmark.getTitle());
					out.println("<br>");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while fetching my notifications: " + e.getMessage());
			
		}
	%>
	</div>
</body>
</html>