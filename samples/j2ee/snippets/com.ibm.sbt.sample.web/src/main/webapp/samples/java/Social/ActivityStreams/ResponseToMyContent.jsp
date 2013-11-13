<!-- 
/*
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
 */ -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.model.Reply"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntity"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntityList"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamService"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>

<head>
<title>SBT JAVA Sample - AS</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Response to my content</h4>
	<div id="content">
	<%
		try {
			ActivityStreamService _service = new ActivityStreamService();
			ActivityStreamEntityList _entries = _service.getResponsesToMyContent(null);

			if (_entries.size() <= 0)
				out.println("No updates to be displayed");

			for (ActivityStreamEntity entry : _entries) {
				out.print("<br><br>");
				if(entry.isContainAttachment()){
					if(entry.getAttachment().isImage()){
						out.println("<img src=\""+entry.getAttachment().getImage().getUrl()+"\" >");
											out.println("<br>"+entry.getAttachment().getAuthorName());
					out.println("<br>"+entry.getAttachment().getUrl());
					}
				}
				out.println("<br>"+entry.getEventTitle());
			
				if(entry.getNumComments()>0){
					out.println("Number of comments was "+entry.getNumComments());
					List<Reply> replies = entry.getReplies();
					for (Reply reply : replies) {
						out.println("<br>"+reply.getContent());
					}
				}
				
			}
		} catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>