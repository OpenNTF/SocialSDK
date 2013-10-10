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
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.model.CommunityXPath"%>
<%@page import="com.ibm.sbt.services.client.connections.files.CommentList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="java.io.ByteArrayOutputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.Comment"%>
<%@page import="com.ibm.sbt.services.client.connections.files.File"%>
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
  <h4>Upload File</h4>
  <div id="content">
    <%
      try {
        CommunityService commService = new CommunityService();
        String content = "Test File Content";
        String name = "Test File " + System.nanoTime() + ".txt";
		String newFileName = "TestCommUpload" + System.currentTimeMillis();
		CommunityList communities = commService.getMyCommunities();
	    String communityId = "";
	    if(communities != null && ! communities.isEmpty())  {
	    	communityId = communities.get(0).getCommunityUuid();
	    	out.println("Community Name : " + communities.get(0).getTitle());
			File fileUploaded = commService.uploadFile(new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8"))), communityId, newFileName, content.length());
            out.println("File created: id ["+fileUploaded.getFileId()+"] title [" + fileUploaded.getTitle() + "]");
            
            // adding comment to community file
            FileService fileService = new FileService();
			for(int i=0;i<5;i++) {            
            	fileService.addCommentToCommunityFile(fileUploaded.getFileId(), "JSP Added community file comment"+System.currentTimeMillis(), communityId, null);
			}
			
            // Get All community File Comments
           CommentList comments = fileService.getAllCommunityFileComments(fileUploaded.getFileId(), communityId, null);
            if(comments != null && ! comments.isEmpty())  { 
				for(Comment comment : comments) {
					out.println(comment.getComment());
				}
            } else {
    			out.println("No Comments");
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