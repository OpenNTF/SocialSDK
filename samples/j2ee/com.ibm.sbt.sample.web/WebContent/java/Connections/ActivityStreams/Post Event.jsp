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
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page import="java.util.Date"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>


<html>

<head>
<title>SBT JAVA Sample - AS</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>


<body>

	<h4>Posting Event to Activity Stream</h4>


	<%
		try {
		JsonJavaObject postPayload = new JsonJavaObject();

		JsonJavaObject actor = new JsonJavaObject();
		actor.put("id", "@self");

		JsonJavaObject object = new JsonJavaObject();
		object.put("summary", "Posting Microblog Through API");
		object.put("objectType", "note");
		object.put("id", "randomobjectid");
		object.put("displayName", "Display Name for Microblogging sameple");
		object.put("url", "http://www.ibm.com");

		postPayload.put("actor", actor);
		postPayload.put("verb", ASVerb.POST.getVerbType());
		postPayload.put("title", "post");
		postPayload.put("content", "post");
		postPayload.put("updated", new Date().getTime());
		postPayload.put("object", object);
		Map<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");

		ActivityStreamService _service = new ActivityStreamService();
		_service.postEntry(postPayload, header);
		} catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>

</body>
</html>