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
<%@page import="com.ibm.sbt.services.client.connections.search.Constraint"%>
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
</head>

<body>
	<h4>Search : Blog Constraint</h4>
	<div id="content">
	<%
		try {
			SearchService svc = new SearchService();
			Constraint constraint = new Constraint();
			constraint.setType("category");
			List<String> values = new ArrayList<String>();
			values.add("Source/blogs");
			//values.add("Source/profiles");
			constraint.setValues(values);
			List<Constraint> constraints = new ArrayList<Constraint>();
			constraints.add(constraint);
			ResultList results = svc.getResultsWithConstraint("test",constraints);
			if(results.size()<=0){
				out.println("No results found");
			}			
			out.println("<br>");
			for (Result result : results) {
					out.println("<b>getTitle : </b> " + result.getTitle());
					out.println("<b>getId : </b> " + result.getId());
					out.println("<b>getTags : </b> " + result.getTags());
					out.println("<b>getSummary : </b> " + result.getSummary());
					out.println("<b>getScore : </b> " + result.getScore());
					out.println("<b>getRank : </b> " + result.getRank());
					out.println("<b>getUpdated : </b> " + result.getUpdated());
					out.println("<b>getApplication : </b> " + result.getApplication());
					out.println("<br>");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while executing search query: " + e.getMessage());
		}
	%>
	</div>
</body>
</html>