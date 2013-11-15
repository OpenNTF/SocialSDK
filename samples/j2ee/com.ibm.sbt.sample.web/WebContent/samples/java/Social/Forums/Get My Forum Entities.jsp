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
	import="com.ibm.sbt.services.client.connections.forums.ForumTopic"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.ForumService"%>
<%@page
	import="com.ibm.sbt.services.client.connections.common.Person"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.model.BaseForumEntity"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.Forum"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.ForumList"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.TopicList"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.ReplyList"%>
<%@page
	import="com.ibm.sbt.services.client.connections.forums.ForumReply"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="java.util.*"%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>SBT JAVA Sample - Replies For A Topic</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%!
		String addForumReply(ForumService svc, String postUuid) throws Exception {
			ReplyList replyReplies = svc.getForumReplyReplies(postUuid);
			StringBuilder tree = new StringBuilder();
			tree.append("<ul>");
			if (replyReplies.size() > 0) {
				for (BaseForumEntity replyOnAReply : replyReplies) {
					if (StringUtil.equalsIgnoreCase(((ForumReply) replyOnAReply).getReplyToPostUuid(),postUuid)) {
						tree.append("<ul><li style='list-style-type: circle'>"+ replyOnAReply.getTitle());
						if (!(((ForumReply) replyOnAReply).isDeleted())) {
							tree.append(addForumReply(svc, replyOnAReply.getUid()));
						}
						tree.append("</li></ul>");
					}
				}
			}
			tree.append("</ul>");
			return tree.toString();
		}

		String addTopicReply(ForumService svc, String postUuid) throws Exception {
			ReplyList topicReplies = svc.getForumTopicReplies(postUuid);
			StringBuilder tree = new StringBuilder();
			tree.append("<ul>");
			if (topicReplies.size() > 0) {
				for (BaseForumEntity replyOnATopic : topicReplies) {
					if (StringUtil.equalsIgnoreCase(((ForumReply) replyOnATopic).getReplyToPostUuid(),postUuid)) {
							tree.append("<ul><li style='list-style-type: square'>"+ replyOnATopic.getTitle());
							if (!(((ForumReply) replyOnATopic).isDeleted())) {
								tree.append(addForumReply(svc, replyOnATopic.getUid()));
							}
					}
					tree.append("</li></ul>");
				}
			}
			tree.append("</ul>");
			return tree.toString();
	}%>
	<%
	try {

		ForumService svc = new ForumService();
		String myForumId = svc.getMyForums().get(0).getUid();
		TopicList topics = svc.getForumTopics(myForumId);
		if (topics.size() > 0) {
			out.println("Total No. of Topics in Forum :"+topics.size()+"<br><br>");
			for (Iterator iterator = topics.iterator(); iterator.hasNext();) {
				ForumTopic topic = (ForumTopic)iterator.next();
				out.println("<b>Topic Title:"+topic.getTitle()+"</b><br>");
				StringBuilder heiararchy = new StringBuilder();
				heiararchy.append(addTopicReply(svc,topic.getUid()));
				out.println(heiararchy);
			}
		} else {
			out.println("no topic found");
		}
	} catch (Throwable e) {
		out.println("<pre>");
		out.println("Problem Occurred while fetching topic replies: "+ e.getMessage());
		out.println("</pre>");
	}
	%>
	</div>
</body>
</html>