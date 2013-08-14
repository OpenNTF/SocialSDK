<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.File,javax.activation.MimetypesFileTypeMap"%>
<%@page import="java.io.InputStream,java.io.OutputStream,java.io.FileOutputStream,java.io.IOException,java.io.File"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem,org.apache.commons.fileupload.FileItemFactory,
org.apache.commons.fileupload.disk.DiskFileItemFactory,org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page
	import="com.ibm.sbt.services.client.connections.profiles.ProfileService"%>
<%@page
	import="com.ibm.sbt.services.client.connections.profiles.Profile"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Profile Photo upload</title>
</head>
<body>

 <%!
 	String fileNameOrId = null;

	void upload(HttpServletRequest request) throws IOException{

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				List<FileItem> fileItems = upload.parseRequest(request);
				if (fileItems.size() == 0) {
					request.setAttribute("message","File Name not provided.Error:" + HttpServletResponse.SC_BAD_REQUEST);
					return;
				}
				String userId = "";
				for (FileItem item : fileItems) {
					if (item.isFormField()) {
						userId = item.getString(); // userId is passed as hidden input

					} else {
				
						InputStream uploadedFileContent = item.getInputStream();
						fileNameOrId = new File(item.getName()).getName();
						File file = convertInputStreamToFile(uploadedFileContent, item.getSize());
						String mimetype= new MimetypesFileTypeMap().getContentType(file);
       					String type = mimetype.split("/")[0];
        				if(type.equals("image")){
        					ProfileService profileService = new ProfileService();
							profileService.updateProfilePhoto(file, userId);
						}
						else{
							request.setAttribute("message","File selected is not an image file");
							request.setAttribute("success", "false");
							return;
						}
					}
					request.setAttribute("message","File Uploaded Successfully");
					request.setAttribute("success", "true");
				}
			} catch (Exception ex) {
				request.setAttribute("message", "File Upload Failed due to " + ex);
				request.setAttribute("success", "false");
			}
		}
	}

	File convertInputStreamToFile(InputStream inputStream, Long size)throws IOException {
		OutputStream out = null;
		try {
			File file = new File(fileNameOrId);
			out = new FileOutputStream(file);
			int bufferSize = size.intValue() < 8192 ? size.intValue() : 8192;
			byte[] bytes = new byte[bufferSize];
			int read = 0;	Long length = 0L;
			while ((read = inputStream.read(bytes)) != -1) {
				length += read;
				out.write(bytes);
				out.flush();
			}
			inputStream.close();
			out.close();
			return file;
		} catch (IOException e) {
			throw e;
		} finally {
			inputStream.close();
			if (out != null) {
				out.close();
			}
		}
	}%>
	<%
		ProfileService profileService = new ProfileService();
		String userId = Context.get().getProperty("sample.id1");
	%>
	<div>
		<h3>Choose Image to upload as Profile photo</h3>
		<form method="post" enctype="multipart/form-data" onsubmit="<%upload(request);%>">
			<input type="hidden" name="userId" id="userId" value="<%=userId%>" />
			<input type="file" name="file" /> <input type="submit"
				value="upload" />
		</form>
		<h4>${requestScope["message"]}</h4>
	</div>
	<%
		if (request.getAttribute("success") != null) {
			if (request.getAttribute("success").equals("true")) {
			Profile profile = profileService.getProfile(userId);
			out.println("<img src=\"" + profile.getThumbnailUrl()+ "\">");
			}
		}
	%>
	</div>
</body>
</html>
