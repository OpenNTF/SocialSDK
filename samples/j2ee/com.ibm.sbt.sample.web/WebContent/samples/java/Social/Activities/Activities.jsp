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

<%@page import="com.ibm.commons.xml.DOMUtil"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="com.ibm.sbt.services.client.Response"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>

<head>
<title>SBT JAVA Sample - Activity OAuth Sample</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<h4>Activities</h4>
	<h2>Sample demonstrates using a Service (like Activities) which is not available OOB with SDK</h2>
	<div id="content">
	<%
		try {
			Endpoint ep = EndpointFactory.getEndpoint("connectionsOA2");
			if(!ep.isAuthenticated()){
				ep.authenticate(true);
			}else{
				// When using a service which is not supported by SDK use the endpoint methods like xhrGet, xhrPost etc. directly
				Response rp = ep.xhrGet("/activities/oauth/atom2/activities");
				out.print(DOMUtil.getXMLString((Node)rp.getData(),false));
				
			}
			
		} catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>
	</div>
</body>
</html>