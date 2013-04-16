<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.util.HtmlTextUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.javasnippets.JavaSnippet"%>
<%@page import="com.ibm.sbt.sample.web.util.SnippetFactory"%>
<%
String javaSamplePath = request.getParameter("snippet");
String jsp = null;
String doc = null;
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
<div id="jspContents">
<%
if (StringUtil.isNotEmpty(jsp)) {
	String pre = "<pre style='background-color:#FFFFFF;border-style:none'>" + HtmlTextUtil.toHTMLContentString(jsp, false) + "</pre>\n";
	out.println(pre);
}
%>
</div>

<div id="docContents" style="display: hidden;">
<%
if (StringUtil.isNotEmpty(jsp)) {
    String pre = ""; //TODO add correct doc when java samples get doc.
    out.println(pre);
}
%>
</div>
