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
<%@page import="com.ibm.commons.util.io.json.JsonJavaFactory"%>
<%@page import="com.ibm.commons.util.io.json.JsonGenerator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.commons.util.io.json.JsonObject"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.SmartCloudService"%>
<%@page import="com.ibm.commons.xml.DOMUtil"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NodeList"%>



<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<html>
	<head>
		<title>OAUTH2.0 BASED SMARTCLOUD COMMUNITIES SAMPLE</title> 
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	</head>
	<body> 
		 
		 <%
 		 		 	// Check if user is authenticated, if not redirect to SmartCloud for authentication.
 		 		  		 		 	Endpoint ep = EndpointFactory.getEndpoint("smartcloudOA2");
 		 		  		 		 	if(!ep.isAuthenticationValid()) {	// Check, Do we have a valid token for this user
 		 		  		 		 		ep.authenticate(true);			// Authenticate
 		 		  		 		 	    		return;							// Exit, JSP would be invoked from Callback handler
 		 		  		 		 	    	}
 		 		 %>
		 
		 
		 <%
		 		 		 	String myCommunitiesURL = "/communities/service/atom/communities/my";
		 		 		 	//ClientService svc = new SmartCloudOAuth2Service(EndpointFactory.getEndPoint("smartcloudo2"),myCommunitiesURL);
		 		 		 	ClientService svc = new SmartCloudService(ep);
		 		 		 	        Object result = svc.get(myCommunitiesURL,null,ClientService.FORMAT_XML); // Get my Communities, Format should be XML
		 		 		 	        out.println("Open Authentication 2.0 with IBM SmartCloud Server was successful<br><br>");
		 		 		 		    Element root = ((Document)result).getDocumentElement();
		 		 		 		    NodeList titlenodes = ((Document)result).getElementsByTagName("title");
		 		 		 		    out.println("We found <b>"+(titlenodes.getLength()-1)+"</b> communities for you<br><br>"); 
		 		 		 		    for(int index=1;index<titlenodes.getLength();index++){
		 		 		 		    	Node node = (Node)titlenodes.item(index);
		 		 		 		    	out.println(index +" : "+ DOMUtil.getTextValue(node)+"<br>");
		 		 		 		    }
		 		 		 %>
		 
	</body>
</html>