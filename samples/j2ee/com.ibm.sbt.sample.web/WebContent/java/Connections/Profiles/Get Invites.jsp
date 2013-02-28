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
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ProfileService"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.Profile"%>
<%@page import="com.ibm.sbt.services.client.connections.profiles.ColleagueConnection"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>Get Invites</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
	<%
		try{		
			
			String userId = Context.get().getProperty("sample.id1");
			ProfileService connProfSvc = new ProfileService();
			Map<String, String> params = new HashMap<String, String>();
			params.put("connectionType", "colleague");
			params.put("status", "pending");
			params.put("inclMessage", "true");

			params.put("userid", userId);
			ColleagueConnection[] connections = (ColleagueConnection[]) connProfSvc.getInvites(params);

			if (connections.length > 0) {
				out.println("<b> Pending Invites from: </b>");
				out.println("<br>");
				for (int i = 0; i < connections.length; i++) {
					out.println("<b>Name : </b> "+ connections[i].getTitle());
					out.println("<br>");
					out.println("<b>Invite Message : </b> "	+ connections[i].getContent());
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