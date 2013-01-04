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
<%@page
	import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page
	import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page
	import="com.ibm.sbt.services.client.connections.communities.Bookmark"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Get Subcommunities of a Community</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<%
		try {
			String communityUuid = Context.get().getProperty("sample.communityId");
			CommunityService communityService = new CommunityService();
			Community community = communityService.getCommunity(communityUuid, true);
			Community[] Communities = communityService.getSubCommunities(community);
			if (Communities.length > 0) {
				out.println("<b>Subcommunities</b>");
				out.println("<br>");
				for (int i = 0; i < Communities.length; i++) {
					out.println("<b>Title : </b> " + Communities[i].getTitle());
					out.println("<br>");
				}
			} else {
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