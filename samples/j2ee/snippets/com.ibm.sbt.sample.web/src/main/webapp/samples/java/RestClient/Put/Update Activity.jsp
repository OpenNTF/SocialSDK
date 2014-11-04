<!-- /*
 * © Copyright IBM Corp. 2014
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
<%@page import="com.ibm.sbt.services.rest.atom.AtomFeed"%>
<%@page import="com.ibm.sbt.services.rest.atom.AtomEntry"%>
<%@page import="com.ibm.sbt.services.client.Response"%>
<%@page import=" com.ibm.sbt.services.rest.RestClient"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT Rest Client Sample - Update Activity</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">	
	<%
	 final String apiPath = "/activities/service/atom2/everything";
	 final String updateApiPath = "/activities/service/atom2/activitynode";

	 String time = String.valueOf(System.currentTimeMillis());
	 
		try {
			RestClient restClient = new RestClient("connections");
			Response<AtomFeed> responseFeed = restClient.doGet(apiPath).asAtomFeed();	
			AtomFeed feed = responseFeed.getData();
			AtomEntry entry = feed.getEntries().get(0);
			String activityId = entry.getAsString("./snx:activity");
			String body= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+"<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">"
			+"<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"activity\" label=\"activity\"></category>"
			+"<title type=\"text\">New Title Updated on"+time+" (GMT Standard Time)</title>"
			+"<content type=\"html\">Activity Content</content>"
			+"<snx:activity>"+activityId+"</snx:activity>"
			+"</entry>";
			if(activityId != null){
				Response<String> postResponse = restClient.doPut(updateApiPath).body(body, "application/atom+xml").parameter("activityNodeUuid", activityId).asString().checkResponseCode(200);
				out.print("Successfully updated Activity - "+activityId);
			} else {
				out.println("There are no activities available to update");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while updating Activity: " + e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>