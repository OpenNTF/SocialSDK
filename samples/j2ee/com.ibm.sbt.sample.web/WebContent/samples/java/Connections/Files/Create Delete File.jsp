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
<%@page import="com.ibm.sbt.services.client.connections.files.model.Headers"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileRequestParams"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%> 
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Upload File</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head> 
          
<body>	
	<h4>Create & Delete File</h4>
	<div id="content">
	<%
	try {		
		FileService fileService = new FileService();
		String content = "Content uploaded by Create Delete File java sample";
		String id = "File" + System.currentTimeMillis() + ".txt";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put(FileRequestParams.VISIBILITY, "public");
		params.put(FileRequestParams.TAG, "text");
		params.put(FileRequestParams.IDENTIFIER, id);
		
		Map<String, String> headers = new HashMap<String, String>();
		headers.put(Headers.XUpdateNonce, fileService.getNonce());
		headers.put(Headers.Slug, id);
		
		FileEntry fileEntry = fileService.upload(content, params, headers);
		out.println("Created file: " + fileEntry.getFileId());
		
		fileService.delete(fileEntry.getFileId());
		out.println("<br/>Deleted file: " + fileEntry.getFileId());
	} catch (Throwable e) {
		out.println("<pre>");
		e.printStackTrace(new PrintWriter(out));
		out.println("</pre>");	
	}					
	%>
	</div>
</body>
</html>