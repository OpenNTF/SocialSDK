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
<%@page import="com.ibm.sbt.services.client.connections.files.File"%>
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
<title>SBT JAVA Sample</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
  <h4>Download File</h4>
  <div id="content">
    <%
    try {
	    CommunityService service = new CommunityService();
	    CommunityList communities = service.getMyCommunities();
	    String communityId = "";
	    if(communities != null && ! communities.isEmpty())  {
	    	communityId = communities.get(0).getCommunityUuid();
	    }
	    
    	FileList listFiles = service.downloadCommunityFiles(communityId, null);
	    if(listFiles != null && !listFiles.isEmpty()) {
	    	for(File file : listFiles) {
	    		out.println(file.getLabel() + "\t\t<a href=\"" + file.getDownloadUrl() + "\">  Download </a><br/>" );
	    	}
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