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
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="java.util.Collection"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page
	import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page
	import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Get My Communities</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<%
		// Make sure that the endpoint is authenticated
		// If not, then load the JavaScript library and authenticate in a popup
		String epName = "connections";
		Endpoint ep = EndpointFactory.getEndpoint(epName);
		if(!ep.isAuthenticated()) {
	%>
			<script src="/sbt.dojo180/dojo/dojo.js.uncompressed.js"></script>
			<script src="/sbt.sample.web/library?lib=dojo&ver=1.8.0"></script>				
			<script>
				require(['sbt/config'], function(config) {
					var ep = config.findEndpoint("<%=epName%>");
					ep.authenticate({
						loginUi: "popup",
						load: function() {
							document.location.reload(true);
						}
					});
				});
			</script>				
	<%
			return;
		}
		
		try {
			CommunityService communityService = new CommunityService();
			Collection<Community> communities = communityService.getMyCommunities();
			out.println("<b> My Communities </b>");
			out.println("<br>");
			if (communities.size() > 0) {
				for (Community community : communities) {
					out.println("<b>Title : </b> " + community.getTitle());
					out.println("<br>");
				}
			} else {
				out.println("No result");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>
	<br>
</body>
</html>