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

<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ASGroup"%>
<%@page import="java.util.Date"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ASVerb"%>
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ASDataPopulator"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.model.Reply"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntity"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamEntityList"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.ActivityStreamService"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<html>

<head>
<title>SBT JAVA Sample - AS</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Posting Status Update to Activity Stream</h4>
	<div id="content">
	<%
		try {
			JsonJavaObject postPayload = new JsonJavaObject();
			String statusMsg = "Dummy Status Update from Java "+new Date();
			postPayload.put("content", statusMsg);

			ActivityStreamService _service = new ActivityStreamService();
		String entryId = _service.postMicroblogEntry(Context.get().getProperty("sample.userId1"),ASGroup.ALL.getGroupType(),"",postPayload);
			out.println("Created entry:" + entryId);
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>