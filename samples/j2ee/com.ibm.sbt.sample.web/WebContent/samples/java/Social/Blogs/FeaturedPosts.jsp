<!-- 
/*
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
 */ -->

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@page import="com.ibm.sbt.services.client.connections.blogs.BlogService"%>
<%@page import="com.ibm.sbt.services.client.connections.blogs.model.BaseBlogEntity"%>
<%@page import="com.ibm.sbt.services.client.connections.blogs.BlogPostList"%>
<%@page import="com.ibm.sbt.services.client.connections.common.Person"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.model.Reply"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<html>
<head>
<title>SBT JAVA Sample - Featured Posts</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%
		try {
			BlogService service = new BlogService();
			BlogPostList entries = service.getFeaturedPosts();

			if (entries.size() <= 0)
				out.println("No updates to be displayed");

			for (BaseBlogEntity entry : entries) {
				Person author = entry.getAuthor();
				out.println("Post title : "+entry.getTitle());
				out.println("uid of blog :"+entry.getUid());
				out.println("date published :"+entry.getPublished());
				out.println("date updated : "+entry.getUpdated());
				out.println("author name : "+author.getName());
				out.println("author state : "+author.getState());
				out.println("author email : "+author.getEmail());
				out.println("author uid : "+author.getId());
				out.println("<br><br>");
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