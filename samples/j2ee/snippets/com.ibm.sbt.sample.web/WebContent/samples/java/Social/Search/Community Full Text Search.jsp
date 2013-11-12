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
<%@page import="com.ibm.sbt.services.client.connections.search.Result"%>
<%@page import="com.ibm.sbt.services.client.connections.search.ResultList"%>
<%@page import="com.ibm.sbt.services.client.connections.search.SearchService"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.util.*"%>

				
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<html>
<head>
<title>SBT JAVA Sample - Search</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" title="Style" href="/sbt.bootstrap211/bootstrap/css/bootstrap.css"></link>
<link rel="stylesheet" type="text/css" title="Style" href="/sbt.bootstrap211/bootstrap/css/bootstrap-responsive.css"></link>
</head>

<body>
	<h4>Search : Community Full Text Search</h4>
	<div id="content">
	<table class="table table-bordered" id="communityTable">
		<tr class="label label-info">
			<th>Community Name</th>
			<th>Community Uuid</th>
		</tr>
	<%
		try {
			SearchService svc = new SearchService();
			Map<String, String> requestMap = new HashMap<String, String>();
			requestMap.put("scope", "communities");
			ResultList results = svc.getResults("Test",requestMap);
			if(results.size()<=0){
				out.println("<tr><td colspan='2'>No communities associated with topic: Test</td><tr>");
			}			
			for (Result result : results) {
					out.println("<tr><td>"+result.getTitle()+"</td><td>"+result.getId()+"</td></tr>");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem occurred while executing search query: " + e.getMessage());
		}
	%>
	</table>
	</div>
</body>
</html>