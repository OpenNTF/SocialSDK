<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<html>
<head>
</head>
<body>
 
<h4>Get My Communities </h4>
	<div id="content">
	<%
		try {
			CommunityService svc = new CommunityService();
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("ps", "5");
			CommunityList communities = svc.getMyCommunities(parameters);
			out.println("<br>Listing my communities , Total communities found : "+communities.getTotalResults());
			out.println("<br>");
			for (Community community : communities) {
					out.println("<b>Name : </b> " + community.getTitle());
					out.println("<br>");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println("Problem Occurred while fetching my communities: " + e.getMessage());
			e.printStackTrace();
		}
	%>
</div>
</body>
</html>

