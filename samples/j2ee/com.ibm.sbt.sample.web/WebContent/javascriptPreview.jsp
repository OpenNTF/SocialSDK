<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.AssetNode"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="demo.SnippetFactory"%>
<%@page import="demo.Util"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%;">
  <% 
	String snippetName = request.getParameter("snippet");
	JSSnippet snippet = (JSSnippet)SnippetFactory.getSnippet(application, request, snippetName);
	String html = null;
	String js = null;
	String css = null;
	String theme = request.getParameter("themeId");
    boolean debug = false;
    boolean loadDojo = true;
    
    // doGet
    if(request.getMethod().equals("GET")){
        if (snippet != null) {
    	    if (html == null)
                html = snippet.getHtml();
    	    if (js == null)
    	        js = snippet.getJs();
            if (css == null)
                css = snippet.getCss();
		    if(theme == null)
		        theme = snippet.getTheme();
		
		    // replace substitution variables
		    if (StringUtil.isNotEmpty(js)) {
			    js = ParameterProcessor.process(js);
		    }
		    if (StringUtil.isNotEmpty(html)) {
			    html = ParameterProcessor.process(html);
		    }
		} else if (snippetName != null && snippetName.length() > 0) {
			js = "require(['sbt/dom'], function(dom) {\ndom.setText('content', 'Error, unable to load: " + snippetName + "');\n});";
		}
    } 
    // doPost
    else if (request.getMethod().equals("POST")) {
        html = request.getParameter("htmlData");
        js = request.getParameter("jsData");
        css = request.getParameter("cssData");
        debug = Boolean.parseBoolean(request.getParameter("debug"));
        loadDojo = Boolean.parseBoolean(request.getParameter("loadDojo"));
    }
  %>
  <head>
    <meta charset="utf-8">
    <title>Social Business Toolkit - JavaScript Preview</title>
    <link href="images/sbt.png" rel="shortcut icon">
	<%
	String[] styleHrefs = Util.getStyles(request, theme);
	for (String styleHref : styleHrefs) {
	%>
	<link rel="stylesheet" type="text/css" title="Style" href="<%=styleHref%>"></link>
	<%
	}
	%>
    <%if(debug){%>
        <script type="text/javascript" src="https://getfirebug.com/firebug-lite.js">
        {startOpened: true}
        </script>
    <%} %>
    <jsp:include page="<%=Util.getLibraryInclude(request)%>" flush="false"/>
    <script type="text/javascript" src="<%=Util.getLibraryUrl(request)%>"></script>
  </head>

  <body class="<%=Util.getBodyClass(request, theme)%>" style="width: 90%; height: 100%;">
	<div id="content"></div>
	<div id="loading" style="visibility: hidden;">
		<img src="/sbt.sample.web/images/progressIndicator.gif">
	</div>
	<%
	if (StringUtil.isNotEmpty(css)) {
		String s = "<style>\n" + css + "</style>\n";
		out.println(s);
	}
	if (StringUtil.isNotEmpty(html)) {
		out.println(html);
	}
	if (StringUtil.isNotEmpty(js)) {
		String s = "<script>" + js + "</script>\n";
		out.println(s);
	}
	%>
  </body>
</html>
