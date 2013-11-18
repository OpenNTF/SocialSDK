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
<%@page import="com.ibm.sbt.services.client.connections.blogs.BlogList"%>
<%@page import="com.ibm.sbt.services.client.connections.blogs.Blog"%>
<%@page import="com.ibm.sbt.services.client.connections.blogs.BlogPostList"%>
<%@page import="com.ibm.sbt.services.client.connections.blogs.BlogPost"%>
<%@page import="com.ibm.sbt.services.client.connections.common.Person"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.model.Reply"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>

<head>
<title>SBT JAVA Sample - Recommend Blog Post</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>

<body>
	<div id="content">
	<%
		try {
			BlogService service = new BlogService();
			BlogList blogs = service.getBlogs();
			Blog blog = (Blog)blogs.get(0);
			
			BlogPostList posts = service.getBlogPosts(blog.getHandle());
			if(posts.size()>0){
				String postId = posts.get(0).getUid();
				service.recommendPost(blog.getHandle(), postId);
				out.println("recommended post with id"+ postId);
			}
			else{
				out.println("No post exists");
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