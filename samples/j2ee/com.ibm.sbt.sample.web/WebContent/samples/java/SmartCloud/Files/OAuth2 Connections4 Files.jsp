<!-- 
/*
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
 */ -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@ page
	import="com.ibm.sbt.services.client.connections.ConnectionsService"%>
<%@ page import="com.ibm.sbt.services.client.ClientService.Args"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.commons.xml.DOMUtil"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>

<head>
<title>SBT JAVA Sample - Profiles</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<script>
	function setEmailValue(value) {
		document.getElementById('emailValue').value = value;
		document.forms["getProfileForm"].submit();
	}
</script>
<h2>Click on button to get files</h2>
<body>
	<form name="getProfileForm" method="post" action="">
		<input type="hidden" id="emailValue" name="emailValue" />

		<button name="UserBtn" type="button"
			onclick="setEmailValue('FrankAdams@renovations.com');">
			Click here to view Files from Frank</button>
	</form>
	<br>

	<%
		// Check if user is authenticated, if not redirect to SmartCloud for authentication.
		Endpoint ep = null;

		try {
			ep = EndpointFactory.getEndpoint("connectionsOA2");
			if (!ep.isAuthenticationValid()) { // Check, Do we have a valid token for this user
				ep.authenticate(true); // Authenticate
				return; // Exit, JSP would be invoked from Callback handler
			}
		} catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");
		}
	%>

	<%
		if (ep != null) {
			try {
				if (request.getParameter("emailValue") != null) {
					//String profileUrl = "activities/service/atom2/activities";
					String filesUrl = "files/oauth/api/myuserlibrary/feed";
					ConnectionsService cs = new ConnectionsService(ep);
					Map<String, String> parameters = new HashMap<String, String>();
					if (request.getParameter("emailValue") != null)
						parameters.put("startIndex",
								request.getParameter("0"));
					Object result = cs.get(filesUrl, parameters,
							ClientService.FORMAT_XML);

					Element root = ((Document) result).getDocumentElement();
					NodeList titlenodes = ((Document) result)
							.getElementsByTagName("title");
					out.println("We found <b>"
							+ (titlenodes.getLength() - 2)
							+ "</b> File uploaded by Frank <br>");
					for (int index = 2; index < titlenodes.getLength(); index++) {
						Node node = (Node) titlenodes.item(index);
						out.println("<h3>" + (index - 1) + " : "
								+ DOMUtil.getTextValue(node) + "</h3>");
					}
				}
			} catch (Throwable e) {
				out.println("<pre>");
				out.println(e.getMessage());
				out.println("</pre>");
			}
		}
	%>


</body>
</html>