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
<%@page import="java.io.InputStream"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.Headers"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileRequestParams"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%>
<%@page import="org.w3c.dom.Document"%> 
<%@page import="java.io.File"%>
<%@page import="org.apache.commons.fileupload.disk.*"%>
<%@page import="org.apache.commons.fileupload.*"%>
<%@page import="org.apache.commons.fileupload.servlet.*"%> 
<%@page import="org.apache.commons.io.output.*"%>
<%@page import="java.util.*"%>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@page 
	language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<html>
<head>
	<title>SBT JAVA Sample - Upload File</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head> 
          
<%
	Endpoint ep = EndpointFactory.getEndpoint("connections"); 
	if(!ep.isAuthenticationValid()) {
		ep.authenticate(true);
   		return;
   	}
%>
<body>

<form enctype="multipart/form-data" name="myForm" method="post" accept-charset="UTF-8">
	<b>Choose the file To Upload:</b><br>
   	<input type="file" name="filename">
    <input type="submit" name="Submit" value="Upload">
    <input type="reset" name="Reset" value="Cancel">
</form>

	<%
	InputStream uploadedStream = null;
	String contentType = request.getContentType();
	
	if (contentType != null && (contentType.indexOf("multipart/form-data") >= 0)) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
	  	ServletFileUpload upload = new ServletFileUpload(factory);
	  	try { 
	    	List fileItems = upload.parseRequest(request);
	    	Iterator i = fileItems.iterator();
	    	i.hasNext ();
		    FileItem fi = (FileItem)i.next();
		    if (!fi.isFormField ()) {
	        	String fileName = fi.getName();
	        	if (fileName == "") 
	        	{ // do nothing
	        	}
	        	else 
	        	{
					uploadedStream = fi.getInputStream();
		    	
		    		FileService service = new FileService();  
		    		Map<String, String> params = new HashMap<String, String>();
					params.put(FileRequestParams.VISIBILITY, "public");
					params.put(FileRequestParams.TAG, "text");
		    		
		    		Map<String, String> headers = new HashMap<String, String>();
					headers.put(Headers.XUpdateNonce, service.getNonce());
					headers.put(Headers.Slug, fileName);
					FileEntry fileEntry = service.upload(uploadedStream, params, headers);
			   		if(fileEntry!=null)
					{
						out.println("<br><b> Display Uploaded file's Data </b><br>");
						out.println("<br>File name : " + fileEntry.getLabel());
						out.println("<br>File created : " + fileEntry.getCreated());
						out.println("<br>File Media Size : " + fileEntry.getTotalMediaSize());
					}
				}
			}
		} 
		catch(Exception ex) { 
			System.out.println(ex); 
		}
		finally {
				uploadedStream.close();
		}
	}
	else {
	}
%>
	 <br>
</body>
</html>