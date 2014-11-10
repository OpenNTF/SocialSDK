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
<%@page import="com.ibm.sbt.services.client.ClientServicesException" %>
<%@page import="java.util.*"%>


<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<title>SBT Rest Client Sample - Create Bookmark</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
		<%
			 final String apiPath = "/dogear/api/app";			
			 String time = String.valueOf(System.currentTimeMillis());
			 final String body= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			 +"<entry xmlns=\"http://www.w3.org/2005/Atom\">"
			 +"<title type=\"text\">Bookmark at "+time+"</title>"
			 +"<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"bookmark\"></category>"
			 +"<link href=\"ibm.com\">"
			 +"<content type=\"html\"></content>"
			 +"</entry>";
			
			 try {
			 	RestClient restClient = new RestClient("connections");
				System.out.println(body);
				Response<String> postResponse = restClient.doPost(apiPath).body(body, "application/atom+xml").asString();
				if(postResponse.getResponse().getStatusLine().getStatusCode() == 201) {
					out.print("Successfully created Bookmark");
				} else {
					out.print("the server returned an HTTP "+postResponse.getResponse().getStatusLine().getStatusCode());
				}
			} catch (ClientServicesException e) {
				out.println("<pre>");
				out.println("Problem Occurred while creating Bookmark: " + e.getMessage());
				out.println("</pre>");
			}
		%>
	</div>
</body>
</html>