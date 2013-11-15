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
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Invite"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.InviteList"%>
<%@page import="java.util.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Get Invite</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%
		try {
			CommunityService svc = new CommunityService();
			InviteList invites = svc.getMyInvites();
			if(invites.size() > 0){
				String communityUuid = invites.get(0).getCommunityUuid();
				String inviteUuid = invites.get(0).getInviteUuid();
				String inviteeUuid = invites.get(0).getInviteeUuid();
				Invite invite = svc.getInvite(communityUuid, inviteeUuid);
				out.println("<b>Invite Id : </b>" + invite.getInviteUuid()+"<br>");
				out.println("<b>Invite Title : </b>" + invite.getTitle()+"<br>");
				out.println("<b>Invite Url : </b>" + invite.getInviteUrl()+"<br>");
				out.println("<b>community Url : </b>" + invite.getCommunityUrl()+"<br>");
				out.println("<b>Contributor Uuid : </b>" + invite.getUserid()+"<br>");
			}
			else{
				out.println("No invites exist");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>