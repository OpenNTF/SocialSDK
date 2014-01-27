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
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.follow.model.Type"%>
<%@page import="com.ibm.sbt.services.client.connections.follow.model.Source"%>
<%@page import="com.ibm.sbt.services.client.connections.follow.FollowedResource"%>
<%@page import="com.ibm.sbt.services.client.connections.follow.FollowService"%>
<%@page import="java.util.Collection"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Context"%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Follow Service</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<h4>Updates from a specific community</h4>
	<div id="content">
	<%
		try {
			
			Community community = new CommunityService().getPublicCommunities().get(0);
			if(null == community){
				out.print("No Communities found");
			}else{
				FollowService svc = new FollowService();
				FollowedResource resource = svc.getFollowedResource(Source.COMMUNITIES.getSourceType(), Type.COMMUNITIES.getType(), community.getCommunityUuid());
				if(resource!=null){
				out.print("Title "+resource.getTitle()+"<br>");
				out.print("ID : "+resource.getId()+"<br>");
				out.print("Source : "+resource.getSource()+"<br>");
				out.print("TYPE : "+resource.getType()+"<br>");
				out.print("RESTYPE : "+resource.getResourceType()+"<br>");
				out.print("RESID : "+resource.getResourceId()+"<br>");
				out.print("<br><br>");
				}else{
					out.print("Public community found with id "+community.getCommunityUuid()+" is not being followed, please follow and re-run the sample");
				}
			}
			
		} catch (Exception e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>