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
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Collection"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.communities.Member"%> 
<%@page import="com.ibm.sbt.services.client.smartcloud.communities.CommunityService"%>
<%@page import="java.util.List"%>
<%@page import="org.w3c.dom.Node"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Get Communities Members</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<%
		try {
			CommunityService svc = new CommunityService();
			String communityUuid = Context.get().getProperty("sample.smartcloud.communityId1");
			out.println("<b>Communities Members</b>");
			out.println("<br>");
			Collection<Member<Node>> members = svc
					.getCommunityMembers(communityUuid);
			for (Member<Node> member : members) {
				out.println("<br> - Member name: " + member.get("title")
						+ ", ");
				out.println(member.get("role") + "<br>");
			}
			out.println("<br>");
			out.println("<br> Found " + members.size() + " members");
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	<br>
</body>
</html>