<!-- /*
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
 */-->
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityServiceException"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileRequestParams"%>
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
<title>SBT JAVA Sample - Upload New Version of File and Community File</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
  <%
      try {
          FileService fileService = new FileService();
          FileList fileEntries = fileService.getMyFiles();
          if(fileEntries != null && !fileEntries.isEmpty()) { 
	          File fileEntry = fileService.getFile(fileEntries.get(0).getFileId(), false);
	          Map<String, String> paramsMap = new HashMap<String, String>();
	          paramsMap.put(FileRequestParams.TAG.getFileRequestParams(), "NewVersionTag");
			  
	          String content = "New Version Uploaded";
			  String name = "NewVersionFile" + System.nanoTime() + ".txt";
			  fileEntry = fileService.updateFile(new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8"))), 
					  fileEntry.getFileId(), name, paramsMap);
	          out.println("New Version of File Uploaded  : " + fileEntry.getFileId());
          } else {
        	  out.println("No Files to be updated");
          }
          
          // Community Files
          CommunityService commSvc = new CommunityService();
          CommunityList commList = commSvc.getMyCommunities();
          if(commList != null && !commList.isEmpty()) {
        	  
        	  // Create Community File
        	  String commFileContent = "CommunityFileCreated";
  			  String newFileName = "CommunityFileCreated" + System.currentTimeMillis();
  			  File fileUploaded = commSvc.uploadFile(new ByteArrayInputStream(commFileContent.getBytes(Charset.forName("UTF-8"))), commList.get(0).getCommunityUuid(), newFileName, commFileContent.length());
              
	          // Now updating community File
  			  Map<String, String> paramsMap = new HashMap<String, String>();
	          paramsMap.put(FileRequestParams.TAG.getFileRequestParams(), "NewVersionCommunityTag");
	          
			  String content = "New Version of Community File" + System.nanoTime();
			  String name = fileUploaded.getLabel()+"NewVersion" + System.nanoTime() + ".txt";
			  fileUploaded = fileService.updateCommunityFile(new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8"))), 
					  fileUploaded.getFileId(), name, fileUploaded.getLibraryId(), paramsMap);
	          out.println("New Version of File Uploaded  : " + fileUploaded.getLabel() + " , Community = " + commList.get(0).getTitle());
          } else {
        	  out.println("No Communities");
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