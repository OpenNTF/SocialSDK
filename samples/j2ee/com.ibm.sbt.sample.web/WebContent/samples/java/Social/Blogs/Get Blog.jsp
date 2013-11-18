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
<%@page import="com.ibm.sbt.services.client.connections.blogs.model.Author"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.services.client.connections.activitystreams.model.Reply"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>SBT JAVA Sample - Get Blog</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
	<div id="content">
	<%
		try {
			BlogService service = new BlogService();
			BlogList blogs = service.getBlogs();
			Blog blog = (Blog)blogs.get(0);
			// above blog entity is used to get the blogUuid
			// so that it can be used in showcasing how to ise the getBlog wrapper which requires a blogUuid  
			Blog fetched_blog = service.getBlog(blog.getBlogUuid());
			if(StringUtil.isNotEmpty(fetched_blog.getHandle())){
				out.println("Blog title : "+fetched_blog.getTitle()+"<br>");
				out.println("Blog Uuid : "+fetched_blog.getBlogUuid()+"<br>");
				out.println("Blog handle : "+fetched_blog.getHandle()+"<br>");
				out.println("Blog summary : "+fetched_blog.getSummary()+"<br>");
			}
			else{
				out.println("No blog exists");
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