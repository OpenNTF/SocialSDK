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
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.files.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Get My Files</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>

	<%
		try {
			//obtain the smartcloud file service.
			FileService svc = new FileService();
			//prepare a filter. object id and type id are added by default.
			FileService.FieldFilter filter = new FileService.FieldFilter();
			//add the entry name to the fields that will be requested
			filter.add("cmis:name");
			//in this example, the entry id is known
			String entryId = Context.get().getProperty("sample.smartcloud.fileEntryId");
			FileEntry entry = svc.getEntry(entryId, false, filter);
			out.println("<br>loaded " + entry.getDisplayName() + " as found at the <a href='"
					+ entry.getPageURL() + "'>SmartCloud File Page</a>");
		} catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>

	<br>
</body>
</html>
