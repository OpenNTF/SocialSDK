<!-- /*
 * © Copyright IBM Corp. 2013
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
<%@page import="com.ibm.commons.util.io.StreamUtil"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
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
		    FileList list = service.getCommunityFiles(communityId, null);
		    String fileId = "";
		    if(list != null && ! list.isEmpty()) {
		    	fileId = list.get(0).getFileId();
			    OutputStream ostream = new ByteArrayOutputStream();
		    	long noOfBytes = service.downloadCommunityFile(ostream, fileId, communityId, null);
			    out.println("File Downloaded in ostream = " + noOfBytes + "\nFile Name = " + list.get(0).getTitle());
				InputStream istream = new ByteArrayInputStream(((ByteArrayOutputStream)ostream).toByteArray());
				String newFileName = "TestCommUpload"+System.currentTimeMillis();
				File fileUploaded = service.uploadFile(istream, communityId, newFileName, noOfBytes);
				out.println("File Uploaded as : " + fileUploaded.getTitle()); 
				out.println("File's Url " + fileUploaded.getDownloadUrl());
			} else {
				out.println("No Files to be Downloaded");
			}
		} else {
			out.println("No Communities");
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