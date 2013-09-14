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
+<%@page import="com.ibm.commons.runtime.Context"%> 
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
<title>SBTX JAVA Sample - AS</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Posting Event to Activity Stream</h4>
	<div id="content">
	<%
		try {
			
			ASDataPopulator populator = new ASDataPopulator();
			populator.setTargetConnectionState("active");
			populator.setTargetId("testid");
			populator.setTargetName("testdata");
			populator.setTargetSummary("testdatasummary");
			populator.setTargetType("testdatatype");
			populator.setTargetUrl("testdataurl");
			
			populator.setProviderId("providertestdataid");
			populator.setProviderDisplayName("pname");
			populator.setProviderUrl("url");
			
			
			populator.setGeneratorId("testdatageneratorId");
			populator.setGeneratorImageUrl("testdatageneratorImageUrl");
			populator.setGeneratorName("testdatageneratorName");
			populator.setGeneratorUrl("testdatageneratorUrl");
			
			populator.setActorConnectionsState("active");
			//populator.setActorId("urn:lsid:lconn.ibm.com:profiles.person:0EE5A7FA-3434-9A59-4825-7A7000278DAA");
			String userid = Context.get().getProperty("sample.userId1");
		    String actorId = "urn:lsid:lconn.ibm.com:profiles.person:"+userid; 
		    populator.setActorId(actorId); 
			populator.setActorName("Frank Adams");
			populator.setActorObjectType("person");
			
			populator.setObjectDisplayName("testdataDPNAME");
			populator.setObjectId("testdataaaOnjId");
			populator.setObjectSummary("testdataaSummarey");
			populator.setObjectType("testdataaObjectType");
			populator.setObjectUrl("testdataaObjectUrl");
			
			populator.setToId(actorId);
			populator.setToObjectType("person");
			
			populator.setPublished("2013-07-06T01:55:56.276Z");
			populator.setUrl("http://testdataurl");
			populator.setId("testdataid");
			
			populator.setVerb("post");
			populator.setTitle("testing data post");
			populator.setContent("testing data post CONTENT");
			populator.setUpdated("2013-07-06T01:55:56.276Z");
			
			ActivityStreamService service = new ActivityStreamService();
			String entryId = service.postEntry(populator); 
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