<%@page import="com.ibm.sbt.sample.web.util.SnippetFactory"%>
<%@page import="com.ibm.sbt.playground.assets.javasnippets.JavaSnippet"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.sample.web.util.Util"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="java.util.Map"%>
 <%
 String snippetId = request.getParameter("snippet");
 String envId = request.getParameter("env");
 String pagePath = "includes/java_error.jsp?snippet=" + snippetId;
 
 String envName = StringUtil.isNotEmpty(envId) ? envId : Util.defaultEnvironment;
 
 if(Context.get().getProperty("environment") == null){
     Context.get().setProperty("environment", envName);
 }
 
Map<String, String[]> paramMap = request.getParameterMap();

for(String paramName : paramMap.keySet()){
	if(paramName.startsWith("sample.")){
		Context.get().setProperty(paramName, paramMap.get(paramName)[0]);
	}
}
 
 JavaSnippet snippet = SnippetFactory.getJavaSnippet(application, request, snippetId);
 if (snippet != null) {
     pagePath = snippet.getJspPath();
 }
 %>
 <jsp:include page="<%=pagePath%>" flush="false"/>
