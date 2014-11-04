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
<title>SBT Rest Client Sample - Create Wiki</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
		<%
			final String apiPath = "/wikis/basic/api/wikis/feed";			
			String time = String.valueOf(System.currentTimeMillis());
			 
			final String body = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
				 +"<entry xmlns=\"http://www.w3.org/2005/Atom\" xmlns:o=\"http://ns.opensocial.org/2008/opensocial\" xmlns:app=\"http://www.w3.org/2007/app\" xmlns:thr=\"http://purl.org/syndication/thread/1.0\" xmlns:fh=\"http://purl.org/syndication/history/1.0\" xmlns:snx=\"http://www.ibm.com/xmlns/prod/sn\" xmlns:opensearch=\"http://a9.com/-/spec/opensearch/1.1/\" xmlns:h=\"http://www.w3.org/1999/xhtml\" xmlns:td=\"urn:ibm.com/td\" xmlns:relevance=\"http://a9.com/-/opensearch/extensions/relevance/1.0/\" xmlns:ibmsc=\"http://www.ibm.com/search/content/2010\" xmlns:xhtml=\"http://www.w3.org/1999/xhtml\" xmlns:spelling=\"http://a9.com/-/opensearch/extensions/spelling/1.0/\" xmlns:ca=\"http://www.ibm.com/xmlns/prod/composite-applications/v1.0 namespace\">"
				 +"<category scheme=\"http://www.ibm.com/xmlns/prod/sn/type\" term=\"wiki\" label=\"wiki\"></category>"
				 +"<title type=\"text\">title"+time+"</title>"
				 +"<summary type=\"text\">summary</summary>"
				 +"<category term=\"tag\"></category>"
				 +"</entry>";
			
			try {
				RestClient restClient = new RestClient("connections");
				System.out.println(body);
				Response<String> postResponse = restClient.doPost(apiPath).body(body, "application/atom+xml").asString();
				
				if(postResponse.getResponse().getStatusLine().getStatusCode() == 201){
					out.print("Successfully created wiki");
				}else{
					out.print("the server returned an HTTP "+postResponse.getResponse().getStatusLine().getStatusCode());
				}
			
			} catch (Throwable e) {
				out.println("<pre>");
				out.println("Problem Occurred while creating Wiki: " + e.getMessage());
				out.println("</pre>");
			}
		%>
	</div>
</body>
</html>