<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.sbt.services.client.Response"%>
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
	<title>Twitter Sample</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js.uncompressed.js"></script>
<script type="text/javascript" src="/sbt.sample.web/library?lib=dojo&ver=1.8.0"></script></head>
<body>

	 <%
		Endpoint ep = EndpointFactory.getEndpoint("twitter");
		if(!ep.isAuthenticationValid()) {
    %>
   	Please authenticate with the Twitter popup window
    <script>
    	require(['sbt/config'], function(config) {
    		var ep = config.findEndpoint("twitter");
    		ep.authenticate({loginUi: "popup"});
    	});
    </script>
	<%
    		return; 
    	}
	 %>
	 	
	<%
		String twitterUrl = "1.1/followers/ids.json";
     	Map<String, String> params = new HashMap<String,String>();
       	params.put("page", "1"); 
        params.put("count", "3"); 
        Response result = ep.xhrGet(twitterUrl, params);
        if(result != null) {
	 		out.println( JsonGenerator.toJson(JsonJavaFactory.instanceEx,result.getData(),false) ); 
	 	} else {
	 		 out.println("No Result");
	 	}
	 %>
</body>
</html>
