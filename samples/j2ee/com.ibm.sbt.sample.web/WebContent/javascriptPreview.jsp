<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.AssetNode"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<%@page import="java.util.List"%>
<%@page import="javax.servlet.http.HttpServletRequest"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor.ParameterProvider"%>
<%@page import="com.ibm.sbt.sample.web.util.SnippetFactory"%>
<%@page import="com.ibm.sbt.sample.web.util.Util"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%;">
  <%
      String defaultEndpoint = Context.get().getProperty("endpoint.default");
      String snippetName = request.getParameter("snippet");
      String html = null;
      String js = null;
      String css = null;
      String theme = request.getParameter("themeId");
      boolean debug = false;
      boolean loadDojo = true;
      
      // doGet
      if(request.getMethod().equals("GET")){
          JSSnippet snippet = (JSSnippet)SnippetFactory.getJsSnippet(application, request, snippetName);
          if (snippet != null) {
              if (html == null)
                  html = snippet.getHtml();
              if (js == null)
                  js = snippet.getJs();
              if (css == null)
                  css = snippet.getCss();
              if(StringUtil.isEmpty(theme))
                  theme = snippet.getTheme();
          
              // replace substitution variables
          	  final HttpServletRequest finalRequest = request;
              if (StringUtil.isNotEmpty(js)) {
          		js = ParameterProcessor.process(js, new ParameterProvider() {
          			public String getParameter(String name) {
          				return finalRequest.getParameter(name);
          			}
          		}, true);
              }
              if (StringUtil.isNotEmpty(html)) {
          		html = ParameterProcessor.process(html, new ParameterProvider() {
          			public String getParameter(String name) {
          				return finalRequest.getParameter(name);
          			}
          		}, true);
              }
          }
      } 
      // doPost
      else if (request.getMethod().equals("POST")){
    	  JSSnippet snippet = null;
          if (StringUtil.isEmpty(theme)){
              snippet = (JSSnippet)SnippetFactory.getJsSnippet(application, request, snippetName);
              theme = snippet.getTheme();
          }
          html = request.getParameter("htmlData");
          js = request.getParameter("jsData");
          css = request.getParameter("cssData");
          loadDojo = Boolean.parseBoolean(request.getParameter("loadDojo"));
      }
      debug = Boolean.parseBoolean(request.getParameter("debug"));
  %>
  <head>
    <meta charset="utf-8">
    <meta name="defaultEndpoint" content="<%=defaultEndpoint%>">
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
    } else {
        out.println("<div>Error, unable to load snippet: "+snippetName+"</div>");
    }
    %>
  </body>
</html>
