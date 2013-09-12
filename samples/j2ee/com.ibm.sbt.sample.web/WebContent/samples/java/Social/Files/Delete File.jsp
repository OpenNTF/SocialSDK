<!-- /*
 * � Copyright IBM Corp. 2012
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
<%@page import="java.util.List"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.CommentEntry"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileEntryList"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Upload File</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head> 
          
<body>	
	<%
	try {
				FileService service = new FileService(); 
				List<FileEntry> fileEntries = service.getMyFiles();
        		FileEntry fileEntry = fileEntries.get(0);
				String fileId = fileEntry.getFileId();
				out.println("<b> Deleting File </b>" + fileId);	
			 	service.deleteFile(fileId);
				out.println("<br> file deleted successfully"); 
		}catch (Throwable e) {
			out.println("<pre>");
			out.println(e.getMessage());
			out.println("</pre>");		
		}						
	%>
	 <br>
</body>
</html>