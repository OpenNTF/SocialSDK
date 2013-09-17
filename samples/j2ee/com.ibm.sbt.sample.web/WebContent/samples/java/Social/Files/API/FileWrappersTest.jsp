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
<%@page import="com.ibm.sbt.services.client.connections.files.FileService"%>
<%@page import="com.ibm.sbt.services.client.connections.files.Comment"%>
<%@page import="com.ibm.sbt.services.client.connections.files.File"%>
<%@page import="com.ibm.sbt.services.client.connections.files.FileList"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.commons.runtime.Application"%>
<%@page import="com.ibm.commons.runtime.Context"%>  
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.io.PrintWriter"%>

<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<h4>Test Sample</h4>
	<div id="content">
	<%
		try { 
			        FileService fileService = new FileService();
			        FileList fileEntries = fileService.getMyFiles();
			        File fileEntry = fileEntries.get(0);
			        
			        // File Wrappers tests
			        out.println("Acls : " + fileEntry.getAcls() + "<br>");
			        out.println("AlternateUrl : " + fileEntry.getAlternateUrl() + "<br>");
			        out.println("AnonymousHitCount : " + fileEntry.getAnonymousHitCount() + "<br>");
			        out.println("AttachmentsCount : " + fileEntry.getAttachmentsCount() + "<br>");
			        out.println("AuthorEntry : " + fileEntry.getAuthorEntry() + "<br>");
			        out.println("Category : " + fileEntry.getCategory() + "<br>");
			        out.println("CommentsCount : " + fileEntry.getCommentsCount() + "<br>");
			        out.println("CommentsUrl : " + fileEntry.getCommentsUrl() + "<br>");
			        out.println("ContentType : " + fileEntry.getContentType() + "<br>");
			        out.println("ContentUrl : " + fileEntry.getContentUrl() + "<br>");
			        out.println("Created : " + fileEntry.getCreated() + "<br>");
			        out.println("DownloadUrl : " + fileEntry.getDownloadUrl() + "<br>");
			        out.println("EditLink : " + fileEntry.getEditLink() + "<br>");
			        out.println("EditMediaLink : " + fileEntry.getEditMediaLink() + "<br>");
			        out.println("FileId : " + fileEntry.getFileId() + "<br>");
			        out.println("FoldersCount : " + fileEntry.getFoldersCount() + "<br>");
			        out.println("HitCount : " + fileEntry.getHitCount() + "<br>");
			        out.println("Label : " + fileEntry.getLabel() + "<br>");
			        out.println("LastAccessed : " + fileEntry.getLastAccessed() + "<br>");
			        out.println("LibraryID : " + fileEntry.getLibraryId() + "<br>");
			        out.println("LibraryType : " + fileEntry.getLibraryType() + "<br>");
			        out.println("LockType : " + fileEntry.getLockType() + "<br>");
			        out.println("Modified : " + fileEntry.getModified() + "<br>");
			        out.println("Modifier : " + fileEntry.getModifier() + "<br>");
			        out.println("ObjectTypeId : " + fileEntry.getObjectTypeId() + "<br>");
			        out.println("Propogation : " + fileEntry.getPropogation() + "<br>");
			        out.println("Published : " + fileEntry.getPublished() + "<br>");
			        out.println("RecommendationsCount : " + fileEntry.getRecommendationsCount() + "<br>");
			        out.println("ReferenceCount : " + fileEntry.getReferencesCount() + "<br>");
			        out.println("SelfUrl : " + fileEntry.getSelfUrl() + "<br>");
			        out.println("Service : " + fileEntry.getService() + "<br>");
			        out.println("SharesCount : " + fileEntry.getSharesCount() + "<br>");
			        out.println("Size : " + fileEntry.getSize() + "<br>");
			        out.println("Summary : " + fileEntry.getSummary() + "<br>");
			        out.println("Tags : " + fileEntry.getTags() + "<br>");
			        out.println("ThumbnailUrl : " + fileEntry.getThumbnailUrl() + "<br>");
			        out.println("Title : " + fileEntry.getTitle() + "<br>");
			        out.println("TotalResults : " + fileEntry.getTotalResults() + "<br>");
			        out.println("Type : " + fileEntry.getType() + "<br>");
			        out.println("Updated : " + fileEntry.getUpdated() + "<br>");
			        out.println("VersionLabel : " + fileEntry.getVersionLabel() + "<br>");
			        out.println("VersionsCount : " + fileEntry.getVersionsCount() + "<br>");
			        out.println("VersionUuid : " + fileEntry.getVersionUuid() + "<br>");
			        out.println("Visibility : " + fileEntry.getVisibility() + "<br>");
			        
			    } catch (Throwable e) {
			        out.println("<pre>");
			      	out.println(e.getMessage());
			        out.println("</pre>");
			    }
	%>
	 <br>   
   </div>
</body>
</html>