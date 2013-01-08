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
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>  
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%>

<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Update</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<%
	try { 
				FileService service = new FileService();
				String fileToBeUpdated = Context.get().getProperty("sample.fileId");

				Map<String, String> params = new HashMap<String, String>(); 
				
				FileEntry fileEntry = service.getFile(fileToBeUpdated, params, true); 
				out.println("<b> Adding Comment</b>");
				out.println("<br>");    
				String comment = "Comment added from JSP..";
				fileEntry = service.addCommentToFile(fileEntry, params, comment); 
				if(fileEntry != null)
				{
					out.println("File's comment Id : " + fileEntry.getCommentEntry().getCommentId());
					out.println("File's Modifier's Email : " + fileEntry.getModifier().getEmail());
				}
				out.println("Status : " + service.FileStatus);
		}catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>
	 <br>   
</body>
</html>