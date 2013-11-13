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
<%@page
	import="com.ibm.sbt.services.client.connections.forums.ForumService"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.TopicList"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.Forum"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.model.Author"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.ForumList"%>
<%@page import="java.util.*"%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>SBT JAVA Sample - TopicsForAForum</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Topics for a particular Forum</h4>
	<div id="content">
		<%
			try {

				ForumService svc = new ForumService();
				ForumList forums = svc.getPublicForums();
				Forum forum = (Forum) forums.iterator().next();
				TopicList topics = svc.getForumTopics(forum.getForumUuid());
				if (topics.size() > 0) {
					for (BaseForumEntity entry : topics) {
						Author author = entry.getAuthor();
						out.println("uid of forum :" + entry.getUid() + "<br>");
						out.println("date published :" + entry.getPublished() + "<br>");
						out.println("date updated : " + entry.getUpdated() + "<br>");
						out.println("author name : " + author.getName() + "<br>");
						out.println("author state : " + author.getState()+ "<br>");
						out.println("author email : " + author.getEmail()+ "<br>");
						out.println("author uid : " + author.getUserid() + "<br>");
						out.println("forum title : " + entry.getTitle()	+ "<br>");
						out.println("<br><br>");
					}
				} else {
					out.println("No topics exist for this forum");
				}
			} catch (Throwable e) {
				out.println("<pre>");
				out.println("Problem Occurred while fetching forum: "+ e.getMessage());
				out.println("</pre>");
			}
		%>
	</div>
</body>
</html>