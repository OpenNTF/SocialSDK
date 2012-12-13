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
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.List"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.files.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Get My Files</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<form method="POST">
		<span>File Name</span><br/><input type="text" name="name" id="name" value="NewFile.txt"/><br/>
		<span>File Content</span><br/><textarea rows="4" cols="50" name="content" id="content">File content</textarea><br/>
		<input type="submit" />
	</form>
	<% 
	
		try {
			if (request.getMethod().compareToIgnoreCase("POST")==0) {
				FileService svc = new FileService();
				String content = request.getParameter("content");
				String name = request.getParameter("name");
				//this could be a file input stream for a file but streams of any kind are supported, as long as the length is known
				InputStream stream = new ByteArrayInputStream(content.getBytes());
				
				FileEntry entry = svc.uploadFile(stream, name, content.length());
						
				out.println("<br>Entry " + entry.getDisplayName() + " uploaded, available in <a href='"
						+ entry.getPageURL() + "'>Smartcloud File Page</a>");
			}
		} catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>
	<br/>
</body>
</html>
