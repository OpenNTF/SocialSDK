<%@page import="demo.SnippetFactory"%>
<%@page import="com.ibm.sbt.playground.assets.javasnippets.JavaSnippet"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="demo.Util"%>
 <%
 String snippetId = request.getParameter("snippet");
 String pagePath = "includes/java_error.jsp?snippet=" + snippetId;
 JavaSnippet snippet = SnippetFactory.getJavaSnippet(application, request, snippetId);
 if (snippet != null) {
     pagePath = snippet.getJspPath();
 }
 %>
 <jsp:include page="<%=pagePath%>" flush="false"/>
