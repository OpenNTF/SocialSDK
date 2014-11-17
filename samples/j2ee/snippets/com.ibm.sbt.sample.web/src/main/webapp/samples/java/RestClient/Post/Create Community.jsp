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
<title>SBT Rest Client Sample - Create Community</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">	
	<%
	 final String apiPath = "/communities/service/atom/communities/my";
	 final String userId = Context.get().getProperty("sample.id1");
	 final String userName = Context.get().getProperty("sample.displayName1");
	 final String email = Context.get().getProperty("sample.email1");
	 String time = String.valueOf(System.currentTimeMillis());
	 final String body= "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			+"<entry "
			+"xmlns=\"http://www.w3.org/2005/Atom\" "
			+"xmlns:app=\"http://www.w3.org/2007/app\" "
			+"xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\">" 
	 		+"<id>ignored</id>"
			+"<title type=\"text\">Day Care Community"+time+"</title>"
			+"<summary type=\"text\">ignored</summary>"
			+"<content type=\"html\">Share information about day care.</content>"
			+"<published>ignored</published>"
			+"<author>"
			+"<name>"+userName+"</name>"
			+"<email>"+email+"</email>"
			+"<snx:userid>"+userId+"</snx:userid>"
			+"<snx:userState>active</snx:userState>"
			+"</author>"
			+"<updated>ignored</updated>"
			+"<contributor>"
			+"<name>"+userName+"</name>"
			+"<email>"+email+"</email>"
			+"<snx:userid>"+userId+"</snx:userid>"
			+"<snx:userState>active</snx:userState>"
			+"</contributor>"
			+"<category term=\"community\" scheme=\"http://www.ibm.com/xmlns/prod/sn/type\"></category>"
			+"<snx:membercount>ignored</snx:membercount>"
			+"<snx:communityType>public</snx:communityType>"
			+"</entry>";
	
		try {
			RestClient restClient = new RestClient("connections");
			Response<String> postResponse = restClient.doPost(apiPath).body(body, "application/atom+xml").asString();
			if(postResponse.getResponse().getStatusLine().getStatusCode() == 201){
				out.print("Successfully created community");
			}else{
				out.print("the server returned an HTTP "+postResponse.getResponse().getStatusLine().getStatusCode());
			}
			
		} catch (ClientServicesException e) {
			out.println("<pre>");
			out.println("Problem Occurred while creating community: " + e.getMessage());
			out.println("</pre>");
		} catch(NullPointerException e){
			
		}
	%>
	</div>
</body>
</html>