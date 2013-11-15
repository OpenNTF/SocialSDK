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
<%@page	import="com.ibm.sbt.services.client.connections.forums.ForumTopic"%>
<%@page	import="com.ibm.sbt.services.client.connections.forums.ForumService"%>
<%@page	import="com.ibm.sbt.services.client.connections.forums.TopicList"%>
<%@page	import="com.ibm.sbt.services.client.connections.forums.Recommendation"%>
<%@page	import="com.ibm.sbt.services.client.connections.forums.RecommendationList"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.util.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>SBT JAVA Sample - Topic by ID</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Topic by ID</h4>
	<div id="content">
		<%
			try {

				ForumService svc = new ForumService();
				TopicList forums = svc.getMyForumTopics();
				ForumTopic topic = (ForumTopic) forums.iterator().next();
				ForumTopic retrievedtopic = svc.getForumTopic(topic
						.getTopicUuid());
				String title = retrievedtopic.getTitle();
				out.println("Topic Title : " + title + "<br>");
				out.println("Topic is pinned : " + topic.isPinned() + "<br>");
				out.println("Topic is locked : " + topic.isLocked() + "<br>");
				out.println("Topic is question : " + topic.isQuestion()	+ "<br>");

				RecommendationList recommendations = retrievedtopic.getRecommendations();
				if (recommendations.size() > 0) {
					out.println("<b>List of users who recommended this thread</b>"+ topic.getRecommendationCount());
					for (Recommendation recommendation : recommendations)
						out.println("<b>Name: </b>" + recommendation.getName()+ "<br>");
				} else {
					out.println("There are no recommendations for this topic");
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