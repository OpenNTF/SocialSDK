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
<%@page import="java.io.File"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="javax.activation.MimetypesFileTypeMap"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="java.io.IOException"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="com.ibm.commons.util.io.StreamUtil"%>
<%@page import="java.io.OutputStream"%>
<%@page import="org.apache.http.Header"%>
<%@page import="java.io.InputStream"%>
<%@page import="org.apache.http.HttpEntity"%>
<%@page import="java.net.URLConnection"%>
<%@page import="java.net.URL"%>
<%@page import="com.ibm.sbt.service.basic.ProxyService"%>
<%@page import="org.apache.http.HttpResponse"%>
<%@page import="com.ibm.sbt.services.client.ClientService.Content"%>
<%@page import="org.apache.http.impl.client.DefaultHttpClient"%>
<%@page import="com.ibm.sbt.services.client.ClientService.Args"%>
<%@page import="org.apache.http.client.methods.HttpGet"%>
<%@page import="com.ibm.sbt.services.client.connections.ConnectionsService"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.service.basic.ConnectionsFileProxyService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.feedHandler.FileFeedHandler"%>
<%@page import="com.ibm.sbt.services.client.Response"%>
<%@page import="java.util.Map"%>
<%@page import="com.ibm.sbt.services.client.ClientService.ContentStream"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.Comment"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileList"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Collection"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Add Community Member</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>


<body>
<%!
	public static final int BUFFER_SIZE = 8192;// restricting the buffer size to 8Kb
	// The upload method gets called when form is submitted
	void upload(HttpServletRequest request) throws IOException {
		try {
			FileItemFactory factory = new DiskFileItemFactory();
			// Create a new file upload handler
			ServletFileUpload upload = new ServletFileUpload(factory);
			//get the FileItem instance by parsing the request
			FileItem fileItem = (FileItem)upload.parseRequest(request).get(0);
			//convert FileItem to File instance ,using converToFile method
			File file = convertToFile(fileItem);
			
			//FileService fileService = new FileService();
        	CommunityService svc = new CommunityService();
			CommunityList comm = svc.getMyCommunities();
			String communityId = comm.get(0).getCommunityUuid();
			svc.uploadFile(file, communityId);
			
			request.setAttribute("message", "File uploaded successfully");
			request.setAttribute("success", "true");
			
		} catch (Exception ex) {
			if(!(ex instanceof InvalidContentTypeException)){
				request.setAttribute("message", "File upload failed"+ ex.getMessage());
				request.setAttribute("success", "false");
			}
		}
	}
	File convertToFile(FileItem fileItem) throws IOException {
			File file = new File(fileItem.getName());
			InputStream uploadedFileContent = fileItem.getInputStream();
			//Create the OutputStream instance by using the file
	        OutputStream outputStream = new FileOutputStream(file);
            //Initiate a byte array and read bytes from input stream and write it to the output stream
            int readBytes = 0;Long size = fileItem.getSize(); 
    		int bufferSize = size.intValue() < BUFFER_SIZE ? size.intValue() : BUFFER_SIZE;
            byte[] buffer = new byte[bufferSize];
            while ((readBytes = uploadedFileContent.read(buffer, 0, bufferSize)) != -1) {
                outputStream.write(buffer, 0, readBytes);
                outputStream.flush();
            }
            outputStream.close();
            uploadedFileContent.close();
			return file;
	}
%>
<div>
	<h3>Choose File to upload </h3>
	<form method="post" enctype="multipart/form-data" action="<%upload(request);%>">
		<input type="file" name="file" /> 
		<input type="submit" value="upload" />
	</form>
	<%if (request.getAttribute("message") != null) {%>
	<h4><%=request.getAttribute("message")%></h4>
	<% } %>

</div>
<%	if (request.getAttribute("success") != null) {
		if (request.getAttribute("success").equals("true")) {
			out.println("File uploaded");
		}
	}%>
</body>
</html>