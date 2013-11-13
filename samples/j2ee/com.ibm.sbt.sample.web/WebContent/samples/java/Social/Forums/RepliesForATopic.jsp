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
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumTopic"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumService"%>
<%@page import="com.ibm.sbt.services.client.connections.common.Person"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.Forum"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumList"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.TopicList"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ReplyList"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumReply"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT JAVA Sample - RepliesForATopic</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Replies for a Topic</h4>
	<div id="content">
	<%
		try {
			
		 	ForumService svc = new ForumService();
			TopicList topics = svc.getMyForumTopics();
			if(topics.size()>0){
				ForumTopic topic = (ForumTopic)topics.iterator().next();
				ReplyList replies = svc.getForumTopicReplies(topic.getTopicUuid());
				if(replies.size()>0){
					for (BaseForumEntity reply : replies) {
						out.println("reply title : "+reply.getTitle()+"<br>");
						out.println("replied on Post with Id : "+((ForumReply)reply).getReplyToPostUuid()+"<br>");
						out.println("<br><br>");
					}
				} 
				else
					out.println("no reply found in topic");
			}else
				out.println("no topic found in forum");
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while fetching topic replies: " + e.getMessage());
			out.println("</pre>");
			
		}
	%>
	</div>
</body>
</html>