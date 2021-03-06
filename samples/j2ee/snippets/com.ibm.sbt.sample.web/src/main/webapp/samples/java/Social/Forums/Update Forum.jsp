<!-- /*
 * � Copyright IBM Corp. 2013
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
<%@page import="com.ibm.sbt.services.client.connections.forums.ForumService"%>
<%@page import="com.ibm.sbt.services.client.connections.forums.Forum"%>
<%@page import="java.util.Collection"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.services.client.base.datahandlers.EntityList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page 
	language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<html>
<head>
	<title>SBT JAVA Sample - Update Forum</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>	
	<h4>Update Forum</h4>
	<div id="content">
	<%
	try {
		ForumService service = new ForumService();
		EntityList<Forum> forums = service.getMyForums();
		if(forums.size() > 0){
			Forum forum = (Forum)forums.get(0);
			long randomNumber = System.currentTimeMillis();
			forum.setTitle("Updated ForumTitle" + randomNumber);
			List<String> tags = new ArrayList<String>();
			tags.add("tag1_"+randomNumber);
			tags.add("tag2_"+randomNumber);
			forum.setTags(tags);
			forum = forum.save(); 
			out.println("Forum updated with title : " + forum.getTitle());
			tags = forum.getTags();
				for (int i = 0; i < tags.size(); i++) {
					out.println(tags.get(i));
					out.println("<br>");
				}
		}
		else{
			out.println("No forums exist");
		}
		
	} catch (Exception e) {
		out.println("<pre>");
		out.println(e.getMessage());
		out.println("</pre>");
	}
	%>
	</div>
</body>
</html>