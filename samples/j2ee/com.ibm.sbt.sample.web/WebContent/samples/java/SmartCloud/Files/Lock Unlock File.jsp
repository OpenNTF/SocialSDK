<!-- /*
 * � Copyright IBM Corp. 2013
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
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.CommentEntry"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileEntryList"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.HashMap"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Upload File</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head> 
 
<body>
	<h4>Lock & Unlock File</h4>
	<div id="content">
	<%
    try {
        FileService fileService = new FileService("smartcloud");
        List<FileEntry> fileEntries = fileService.getMyFiles();
        FileEntry fileEntry = fileEntries.get(0);
        String lock = fileEntry.getLock();
        if ("NONE".equalsIgnoreCase(lock)) {
        	fileService.lock(fileEntry.getFileId());
        	fileEntry = fileService.getFile(fileEntry.getFileId(), true);
        	out.println("File " + fileEntry.getLabel() + " locked: " + fileEntry.getLock());
        	fileService.unlock(fileEntry.getFileId());
        	fileEntry = fileService.getFile(fileEntry.getFileId(), true);
        	out.println("<br/>File " + fileEntry.getLabel() + " locked: " + fileEntry.getLock());
        } else {
        	fileService.unlock(fileEntry.getFileId());
        	fileEntry = fileService.getFile(fileEntry.getFileId(), true);
        	out.println("File " + fileEntry.getLabel() + " locked: " + fileEntry.getLock());
        	fileService.lock(fileEntry.getFileId());
        	fileEntry = fileService.getFile(fileEntry.getFileId(), true);
        	out.println("<br/>File " + fileEntry.getLabel() + " locked: " + fileEntry.getLock());
        }
    } catch (Throwable e) {
        out.println("<pre>");
       	out.println(e.getMessage());
        out.println("</pre>");
    }
	%>
	</div>
</body>
</html>