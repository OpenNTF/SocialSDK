<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.util.HtmlTextUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.javasnippets.JavaSnippet"%>
<%@page import="demo.SnippetFactory"%>
<ul class="nav nav-tabs">
    <li class="active">
		<a href="#">JavaServer Page</a>
    </li>
    <li class="disabled">
    	<a href="#">Documentation</a>
    </li>
</ul>
<%
String javaSamplePath = request.getParameter("snippet");
String jsp = null;
if (StringUtil.isNotEmpty(javaSamplePath)) {
	JavaSnippet snippet = SnippetFactory.getJavaSnippet(application, request, javaSamplePath);
	if (snippet != null) {
		jsp = snippet.getJspForDisplay();
		
		// replace substitution variables
		if (StringUtil.isNotEmpty(jsp)) {
			jsp = ParameterProcessor.process(jsp);
		}
	}
}
%>
<div id="snippetJava" style="border-style:solid;border-width:1px;width=500px;height:300px;border-color:#D3D3D3;overflow:scroll;">
<%
if (StringUtil.isNotEmpty(jsp)) {
	String pre = "<pre style='background-color:#FFFFFF;border-style:none'>" + HtmlTextUtil.toHTMLContentString(jsp, false) + "</pre>\n";
	out.println(pre);
}
%>
</div>
