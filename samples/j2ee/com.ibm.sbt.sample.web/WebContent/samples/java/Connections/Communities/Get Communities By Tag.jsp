<!-- /*
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
 */-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> 
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%> 
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityParams"%> 
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="java.util.Collection"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Get Communities of which user is a member</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<%
	try {
		String tag = "test";
		CommunityService communityService = new CommunityService();
		CommunityParams params = new CommunityParams();
		params.setTag(tag);
		Collection<Community> communities = communityService.getPublicCommunities(params.getParameterMap());
		if(communities.size() > 0){
			out.println("<b> Communities associated with Tag: </b>"+tag);	
			out.println("<br>");
			for (Community community : communities) {
					out.println("<b>Title : </b> " + community.getTitle());
					out.println("<br>");
				}
		
		}
		else{
			out.println("No result");
		}
	} catch (Throwable e) {
		out.println("<pre>");
		e.printStackTrace(new PrintWriter(out));
		out.println("</pre>");
	}
	%>
	 <br>
</body>
</html>