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
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityList"%>
<%@page import="com.ibm.sbt.services.client.connections.communities.CommunityService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.File"%>
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
	    }
	    
	    OutputStream ostream = new FileOutputStream("tempFile");
    	long noOfBytes = service.downloadCommunityFile(ostream, "023118f5-8c06-4e47-bbba-39446db5fdcd", "ebb7852b-0caf-46f1-a04a-054b4808bbe1", null);
	    out.println("File Downloaded in ostream");

    } catch (Throwable e) {
      out.println("<pre>");
      out.println(e.getMessage());
      out.println("</pre>");
    }
    %>
  </div>
</body>
</html>