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
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
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
  <h4>Get Community Files</h4>
  <div id="content">
    <%
    try {
	    CommunityService service = new CommunityService();
	    FileService serviceFs = new FileService();
	    CommunityList communities = service.getMyCommunities();
	    
	    if(communities != null && ! communities.isEmpty())  {
	    	String communityId = communities.get(0).getCommunityUuid();
		    out.println("<br>Getting Community Owned Files<br>");
	    	FileList list = serviceFs.getCommunityFiles(communityId, null);
		    String fileId = "";
		    if(list != null && ! list.isEmpty()) {
		    	for (File fileEntry : list) {
					out.println("<a href=\"" + fileEntry.getDownloadUrl() + "\"> " + fileEntry.getLabel() + "</a><br/>" );
				}
			} else {
				out.println("No Files in Community Library");
			}
		    
		   
		    out.println("<br>Getting Files Shared with the Community<br>");
	    	list = serviceFs.getCommunitySharedFiles(communityId, null);
		    if(list != null && ! list.isEmpty()) {
		    	for (File fileEntry : list) {
					out.println("<a href=\"" + fileEntry.getDownloadUrl() + "\"> " + fileEntry.getLabel() + "</a><br/>" );
				}
			} else {
				out.println("No Files shared with the Community");
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