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
 */
 -->
 <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="org.w3c.dom.Document"%>
<%@page import="org.w3c.dom.Node"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.commons.util.io.json.JsonObject"%>
<%@page import="com.ibm.sbt.services.client.LinkedInClientService"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.LinkedInOAuth2Endpoint"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.xml.DOMUtil"%>
<%@page import="java.net.URLEncoder" %>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NamedNodeMap"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT Sample - LinkedIn</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<body>
	<%
		LinkedInOAuth2Endpoint ep = (LinkedInOAuth2Endpoint)EndpointFactory.getEndpoint("linkedinOA2");
		if(!ep.isAuthenticationValid()) {
			ep.authenticate(true);
    		return; 
    	}
	 %>
	 

	Searching results for people working in IBM  Social Business Toolkit project... <br/><br/><br/>
	<%
		String searchFor = URLEncoder.encode("IBM Social Business Toolkit", "UTF-8");
		String linkedinSearch = "v1/people-search";
		System.err.println("url "+linkedinSearch);
		LinkedInOAuth2Endpoint linkedinep = (LinkedInOAuth2Endpoint)EndpointFactory.getEndpoint("linkedinOA2");
		LinkedInClientService svc = (LinkedInClientService)linkedinep.getClientService();
        
        Map<String, String> params = new HashMap<String,String>();
        params.put("keywords","IBM Social Business Toolkit");
        params.put("start", "1");
        params.put("count", "20"); 
        Object result = svc.get(linkedinSearch, params, ClientService.FORMAT_XML);		
 		Document resultDoc = (Document)result;
 		Element connElt = resultDoc.getDocumentElement();
 		String count = connElt.getFirstChild().getAttributes().item(0).getNodeValue();
 		int total = Integer.parseInt(count);
	 %>
		
	<%
 		NodeList nl = DOMUtil.getAllChildElementsByName(resultDoc.getDocumentElement(), "person");
 		for (int i = 0; i < total; i++) {
			Element onePerson = (Element)nl.item(i);
			
			NodeList idList = DOMUtil.getAllChildElementsByName(onePerson, "id");
			Node idnode = idList.item(0);
			String id = idnode.getTextContent();
			NodeList firstNameList = DOMUtil.getAllChildElementsByName(onePerson, "first-name");
			Node firstName = firstNameList.item(0);
			String fName = firstName.getTextContent();
			NodeList lastNameList = DOMUtil.getAllChildElementsByName(onePerson, "last-name");
			Node lastName = lastNameList.item(0);
			String lName = lastName.getTextContent();
			NodeList picList = DOMUtil.getAllChildElementsByName(onePerson, "picture-url");
			
			%>
			<div><%= "<a href='http://www.linkedin.com/profile/view?"+id+ "' target='_blank'>" + fName + " " + lName+"</a>"%></div>		
			
 	<%	} 
	%> 			
</body>
</html>