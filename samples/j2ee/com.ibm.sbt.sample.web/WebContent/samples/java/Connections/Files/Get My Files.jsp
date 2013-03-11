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
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileRequestParams"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>  
 
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Files</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<%
	try {
			FileService fileService = new FileService();
				out.println("<b> My Files: " + "</b>");	
				out.println("<br>"); 
				List<FileEntry> fileEntries = fileService.getMyFiles(); 
				if(fileEntries != null && ! fileEntries.isEmpty())
				{
					for (Iterator iterator = fileEntries.iterator(); iterator.hasNext();)
					{
						FileEntry fileEntry = (FileEntry) iterator.next();
						out.println("File Name : " + fileEntry.getLabel());
						out.println("<br>");
						out.println("File's UUID : " + fileEntry.getFileId());
						out.println("<br>");
						out.println("Last Modified By : " + fileEntry.getModifier().getName());
						out.println("<br>");
						out.println("Download File : <a href=\"" + fileEntry.getDownloadLink() + "\"> "+ fileEntry.getLabel() + "</a>" );
						out.println("<br>************************************************************<br>");
					}
				}
				else
				{
					out.println("No Results");
				}
		}catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");	
		}					
		%>
	 <br>
</body>
</html>