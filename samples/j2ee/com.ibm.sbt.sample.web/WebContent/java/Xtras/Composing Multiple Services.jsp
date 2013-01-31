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
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Date"%>
<%@page import="com.ibm.sbt.services.client.activitystreams.ASVerb"%>
<%@page import="com.ibm.commons.util.io.json.JsonJavaObject"%>
<%@page
	import="com.ibm.sbt.services.client.activitystreams.ActivityStreamService"%>
<%@page import="java.io.ByteArrayInputStream"%>
<%@page import="java.io.InputStream"%>
<%@page import="java.util.List"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.smartcloud.files.*"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
<title>SBT JAVA Sample - Composing Services</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>

	<pre>	
An important issue when using the SBTK API 
is correctly handling the usage of multipe services
which may or may not be using different endpoints

each endpoint usage will trigger an authentication redirect.
	
this example has a set of actions to be performed sequentially:
		
	1. post a file
	2. post the file link on an activity stream
		
if the activity stream service endpoint is not logged in, 
it will cause a redirect which will then return to this page
with the side effect of having the file uploaded twice.
</pre>

	<form method="POST">
		<span>File Name</span><br />
		<input type="text" name="name" id="name" value="NewFile.txt" /><br />
		<span>File Content</span><br />
		<textarea rows="4" cols="50" name="content" id="content">File content</textarea>
		<br /> <input type="submit" />
	</form>

	<%
		try {
			if (request.getMethod().compareToIgnoreCase("POST") == 0) {
				//declare service at start
				FileService fileService = new FileService();
				ActivityStreamService activityService = new ActivityStreamService();
				//because we execute operations that require authentication, 
				//we make sure to be authenticated rigth at the beginning
				if (!fileService.getEndpoint().isAuthenticated()) {
					//do not force autentication here, may cause an endless redirect loop!
					fileService.getEndpoint().authenticate(false);
					//return and let the redirect be processed by the browser as early as possible
					return;
				}
				if (!activityService.getEndpoint().isAuthenticated()) {
					//do not force autentication here, may cause an endless redirect loop!
					activityService.getEndpoint().authenticate(false);
					//return and let the redirect be processed by the browser as early as possible
					return;
				}
				//now that we have enforced authentication, we can start
				//performing the actual actions
				String content = request.getParameter("content");
				String name = request.getParameter("name");

				InputStream stream = new ByteArrayInputStream(
						content.getBytes());

				FileEntry entry = fileService.uploadFile(stream, name,
						content.length());

				out.println("<br>Entry " + entry.getDisplayName()
						+ " uploaded, available in <a href='"
						+ entry.getPageURL() + "'>SmartCloud File Page</a>");

				JsonJavaObject postPayload = new JsonJavaObject();

				JsonJavaObject actor = new JsonJavaObject();
				actor.put("id", "@self");

				JsonJavaObject object = new JsonJavaObject();
				object.put("summary", "Posting Microblog Through API");
				object.put("objectType", "note");
				object.put("id", "randomobjectid");
				object.put("displayName",
						"Display Name for Microblogging sameple");
				object.put("url", "http://www.ibm.com");

				postPayload.put("actor", actor);
				postPayload.put("verb", ASVerb.POST.getVerbType());
				postPayload.put("title", "file uploaded");
				postPayload.put("content",
						"<br>Entry " + entry.getDisplayName()
								+ " uploaded, available in <a href='"
								+ entry.getPageURL()
								+ "'>SmartCloud File Page</a>");
				postPayload.put("updated", new Date().getTime());
				postPayload.put("object", object);
				Map<String, String> header = new HashMap<String, String>();
				header.put("Content-Type", "application/json");

				activityService.postEntry(postPayload, header);

			}
		} catch (Throwable e) {
			out.println("<pre>");
			e.printStackTrace(new PrintWriter(out));
			out.println("</pre>");
		}
	%>
	<br />
</body>
</html>
