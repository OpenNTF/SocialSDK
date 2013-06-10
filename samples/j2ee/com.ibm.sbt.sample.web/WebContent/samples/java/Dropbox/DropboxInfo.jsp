<!-- 
/*
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
 */ -->
 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.commons.util.io.json.JsonJavaFactory"%>
<%@page import="com.ibm.commons.util.io.json.JsonGenerator"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.InputStream"%>

<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.commons.util.io.json.JsonObject"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>Dropbox Sample</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js.uncompressed.js"></script>
<script type="text/javascript" src="/sbt.sample.web/library?lib=dojo&ver=1.8.0"></script></head>
<body>
	 
	 <%
		Endpoint ep = EndpointFactory.getEndpoint("dropbox");
		if(!ep.isAuthenticationValid()) {
    %>
   	Please authenticate with the Dropbox popup window
    <script>
    	require(['sbt/Endpoint'], function(Endpoint) {
    		var ep = Endpoint.find("dropbox");
    		ep.authenticate({loginUi: "popup"});
    	});
    </script>
	<%
    		return; 
    	}
	 %>
	 
	 	
	<%
		String accountInfo = "/1/account/info";
     	Map<String, String> params = new HashMap<String,String>();
        Object result = ep.xhrGet(accountInfo, params);
        
        BufferedReader reader = new BufferedReader(new InputStreamReader((InputStream)result, "UTF-8"));
    	StringBuilder sb = new StringBuilder();
    	String line = null;
    	while ((line = reader.readLine()) != null) {
        	sb.append(line + "\n");
    	}
    	out.print("Account Information : ");
        out.print(sb.toString());
	 %>

</body>
</html>
