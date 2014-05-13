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
<%@page import="com.ibm.sbt.services.client.connections.files.model.FileCreationParameters"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.Comment"%>
<%@page import="com.ibm.sbt.services.client.connections.files.File"%>
<%@page import="com.ibm.sbt.services.client.connections.common.Tag"%>
<%@page import="java.nio.charset.Charset"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
  pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Upload File</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
  <h4>Create & Delete File</h4>
  <div id="content">
    <%
        try {
            FileService fileService = new FileService();
            String content = "Content uploaded by Create Delete File java sample";
            String id = "File" + System.currentTimeMillis() + ".txt";

            FileCreationParameters parameters = new FileCreationParameters();
            parameters.visibility = FileCreationParameters.Visibility.PUBLIC;
            parameters.tags.add("text");
            Map<String, String> params = parameters.buildParameters();

            Map<String, String> headers;

            File entry = fileService.uploadFile(
                    new ByteArrayInputStream(content.getBytes(Charset.forName("UTF-8"))), id,
                    content.length(), params);

            out.println("Created file: " + entry.getFileId());

            fileService.deleteFile(entry.getFileId());
            out.println("<br/>Deleted file: " + entry.getFileId());
        } catch (Throwable e) {
            out.println("<pre>");
            out.println(e.getMessage());
            out.println("</pre>");
        }
    %>
  </div>
</body>
</html>