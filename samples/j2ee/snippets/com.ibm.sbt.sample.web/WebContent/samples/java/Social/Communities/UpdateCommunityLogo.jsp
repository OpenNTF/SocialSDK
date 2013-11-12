<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="java.io.File,javax.activation.MimetypesFileTypeMap"%>
<%@page import="java.io.InputStream,java.io.OutputStream,java.io.FileOutputStream,java.io.IOException,java.io.File"%>
<%@page	import="org.apache.commons.fileupload.FileItem,org.apache.commons.fileupload.FileItemFactory,
org.apache.commons.fileupload.disk.DiskFileItemFactory,org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page	import="org.apache.commons.fileupload.FileUploadBase.InvalidContentTypeException"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page	import="com.ibm.sbt.services.client.connections.communities.Community"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"	pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Update Community Logo</title>
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
			//Create CommunityService instance and call updateCommunityLogo wrapper function to update Community Logo
			CommunityService communityService = new CommunityService();
			CommunityList communities = communityService.getMyCommunities();
			String communityId = communities.get(0).getCommunityUuid();
			communityService.updateCommunityLogo(file, communityId);
			request.setAttribute("message", "Logo updated successfully");
			request.setAttribute("success", "true");
		} catch (Exception ex) {
			if(!(ex instanceof InvalidContentTypeException)){
				request.setAttribute("message", "Logo update failed"+ ex.getMessage());
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
	<h3>Choose Image to upload as Community Logo</h3>
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
			CommunityService communityService = new CommunityService();
			CommunityList communities = communityService.getMyCommunities();
			Community myCommunity = communities.get(0);
			out.println("Logo for Community with title: " +  myCommunity.getTitle()+ " and CommunityUuid: "+myCommunity.getCommunityUuid()+"<br>");
			out.println("<img src=\"" +  myCommunity.getLogoUrl()+ "\">");
		}
	}%>
</body>
</html>
