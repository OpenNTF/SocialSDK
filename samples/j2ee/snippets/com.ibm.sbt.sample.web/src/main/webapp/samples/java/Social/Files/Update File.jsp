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
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileRequestPayload"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileCreationParameters"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.Comment"%>
<%@page import="com.ibm.sbt.services.client.connections.files.File"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Update</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
  <%
      try {
          FileService fileService = new FileService();
          FileList fileEntries = fileService.getMyFiles();
          if(fileEntries != null) { 
			  File fileEntry = fileEntries.get(0);
	          String fileId = fileEntry.getFileId();
	          String shareWithUserId = Context.get().getProperty("sample.userId2");
	          fileEntry = fileService.getFile(fileId, false);
	
	          out.println("<b> Updating File </b>");
	          out.println("<br>");
	
	          FileCreationParameters p = new FileCreationParameters();
	          p.sharePermission = FileCreationParameters.Permission.EDIT;
	          p.shareWith.add(shareWithUserId);
	          p.tags.add("TagUpdateFile");
	          Map<String, String> paramsMap = p.buildParameters();
	
	          fileEntry.setLabel("LabelUpdateFileNew" + System.currentTimeMillis());
	          fileEntry.setSummary("SummaryNew" + System.currentTimeMillis());
	          fileEntry = fileService.updateFileMetadata(fileEntry, paramsMap);
	
	          out.println("File Updated : " + fileEntry.getFileId());
          } else {
        	  out.println("No Files to be updated");
          }
      } catch (Throwable e) {
          out.println("<pre>");
          out.println(e.getMessage());
          out.println("</pre>");
      }
  %>
  <br>
</body>
</html>