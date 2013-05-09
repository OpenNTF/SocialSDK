<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.sbt.services.client.smartcloud.SmartCloudService"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.commons.util.io.json.JsonJavaFactory"%>
<%@page import="com.ibm.commons.util.io.json.JsonGenerator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.commons.util.io.json.JsonObject"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBTX Sample - Twitter</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<%
		Endpoint ep = EndpointFactory.getEndpoint("twitter");
		if(!ep.isAuthenticationValid()) {
			ep.authenticate(true);
    		return;
    	}
	 %>
	 	
	<%
		String twitterUrl = "1/followers/ids.json";
		ClientService svc = new SmartCloudService(EndpointFactory.getEndpoint("twitter"));
        
       Map<String, String> params = new HashMap<String,String>();
     /*    params.put("page", "1"); 
         params.put("count", "3"); 
         params.put("ps", "1");  */
        
        Object result = svc.get( twitterUrl, params);
	 %>
	 <pre>
	 <code>
	 	<%= 
	 		JsonGenerator.toJson(JsonJavaFactory.instanceEx,result,false) 
	 	%>
	 </code>
	 </pre>
</body>
</html>