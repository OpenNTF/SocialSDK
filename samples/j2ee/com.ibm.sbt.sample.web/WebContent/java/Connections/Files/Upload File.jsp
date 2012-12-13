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
<%@page import="com.ibm.sbt.services.client.connections.files.model.Headers"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileRequestParams"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.ibm.sbt.services.client.ClientService"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileEntry"%> 
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
	
				FileService service = new FileService();
				//Upload File
				out.println("<b> Upload File </b>");	
				String pathOfFileOnServer = Context.get().getProperty("sample.uploadFilePath");
				out.println("<br> Test file upload - Path = " + pathOfFileOnServer );
				FileEntry fileEntry = service.upload(pathOfFileOnServer);
				out.println("<br> File Upload Status : " + service.FileStatus );
				
				if(fileEntry!=null)
				{
				out.println("<br> Display Uploaded file's Data");
				out.println("<br>File name : " + fileEntry.getLabel());
				out.println("<br>File created : " + fileEntry.getCreated());
				out.println("<br>File Media Size" + fileEntry.getTotalMediaSize());
				}
				//Upload File with Metadata 
				pathOfFileOnServer = Context.get().getProperty("sample.uploadMultiPartFilePath");
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put(FileRequestParams.TAG, "multipartPost-UploadFile-Tag");
				parameters.put(FileRequestParams.IDENTIFIER, "label");
				Map<String, String> headers = new HashMap<String, String>();
				headers.put(Headers.XUpdateNonce, service.getNonce());
				FileEntry fileE = service.uploadFileWithMetadata(pathOfFileOnServer, parameters, headers, null);
				out.println("<br> Test file upload Multipart - Path = " + pathOfFileOnServer );
				if(fileE != null)
					out.println("<br> TotalMediaSize : " + fileE.getTotalMediaSize());
				
				out.println(service.FileStatus);
	%>
	 <br>
</body>
</html>