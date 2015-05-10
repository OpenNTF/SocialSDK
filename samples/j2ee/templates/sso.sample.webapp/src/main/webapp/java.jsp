<%@page import="java.util.Enumeration"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.*"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList" %>
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
			EntityList<Community> communities = svc.getMyCommunities();
			for(Community comm : communities){
				out.println("Title: " + comm.getTitle() + "<br/>");
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

