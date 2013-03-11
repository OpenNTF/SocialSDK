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
<%@page
	import="com.ibm.sbt.services.client.activitystreams.ActivityStreamService"%>
<%@page
	import="com.ibm.sbt.services.client.activitystreams.model.ActivityStreamEntry"%>


<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@ page
	import="com.ibm.sbt.services.client.connections.ConnectionsService"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@ page import="com.ibm.sbt.services.client.activitystreams.*"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.commons.xml.DOMUtil"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.Node"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>

<head>
<title>SBT ActivityStream - Updates from a community</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Updates from a Community</h4>
	<div id="content">
	<%
		try {
			ActivityStreamService _service = new ActivityStreamService();
			List<ActivityStreamEntry> _entries = (List) _service.getUpdatesFromCommunity(Context.get()
					.getProperty("sample.communityId"));

			if (_entries.size() <= 0)
				out.println("No updates to be displayed");

			for (Iterator iterator = _entries.iterator(); iterator.hasNext();) {
				ActivityStreamEntry entry = (ActivityStreamEntry) iterator.next();
				out.println(entry.getActor().getName() + " : " + entry.getEventTitle());
				out.println("<br>");
				out.println(entry.getSummary());
				out.println("<br>");
				out.println(entry.getPublished());
				out.println("<br>");
				out.println("<br>");

				out.println("Number of Comments : " + entry.getNumComments());
				out.println("Number of Likes : " + entry.getNumLikes() + "<br>");

				if (entry.getAttachment().isImage()) {
					out.println("<img src=\"" + entry.getAttachment().getImage().getUrl() + "\">");
					out.println("<br>");
				}

				if (null != entry.getAttachment().getDisplayName()) {
					out.println("Attachment : <a href=\"" + entry.getAttachment().getUrl() + "\">"
							+ entry.getAttachment().getDisplayName() + "</a>");
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