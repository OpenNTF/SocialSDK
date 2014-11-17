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
<title>SBT Rest Client Sample - Delete Bookmark</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">	
	<%
	 final String apiPath = "/dogear/atom";
	 final String deleteApiPath = "/dogear/api/app";

		try {
			RestClient restClient = new RestClient("connections");
			Response<AtomFeed> responseFeed = restClient.doGet(apiPath).asAtomFeed();	
			AtomFeed feed = responseFeed.getData();
			AtomEntry entry = feed.getEntries().get(0);
			String bookmarkUrl = entry.getAsString("a:link[1]/@href");
			if(bookmarkUrl != null && feed.getEntries().size() > 0){
				Response<String> deleteResponse = restClient.doDelete(deleteApiPath).parameter("url", bookmarkUrl).asString().checkResponseCode(204);
				out.print("Successfully deleted Bookmark - "+bookmarkUrl);
			}else{
				out.println("There are no bookmarks to delete, please create a bookmark and run this sample again");
			}
		} catch (ClientServicesException e) {
			out.println("<pre>");
			out.println("Problem Occurred while deleting bookmark: " + e.getMessage());
			out.println("</pre>");
		} catch(NullPointerException e){
			
		}
	%>
	</div>
</body>
</html>