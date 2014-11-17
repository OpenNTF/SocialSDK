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
<title>SBT Rest Client Sample - Delete Wiki</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">	
	<%
	 final String apiPath = "/wikis/basic/api/mywikis/feed";
	 final String deleteApiPath = "";

		try {
			RestClient restClient = new RestClient("connections");
			Response<AtomFeed> responseFeed = restClient.doGet(apiPath).asAtomFeed();	
			AtomFeed feed = responseFeed.getData();
			AtomEntry entry = feed.getEntries().get(0);
			String wikiTitle = entry.getTitle();
			System.out.println(wikiTitle);
			if(wikiTitle != null){
				Response<String> deleteResponse = restClient.doDelete("/wikis/basic/api/wiki/"+wikiTitle+"/entry").asString().checkResponseCode(200);
				out.print("Successfully deleted Wiki - "+wikiTitle);
			}else{
				out.println("There are no Wikis to delete, please create a Wiki and run this sample again");
			}
		} catch (ClientServicesException e) {
			out.println("<pre>");
			out.println("Problem Occurred while deleting Wiki: " + e.getMessage());
			out.println("</pre>");
		} catch(NullPointerException e){
			
		}
	%>
	</div>
</body>
</html>