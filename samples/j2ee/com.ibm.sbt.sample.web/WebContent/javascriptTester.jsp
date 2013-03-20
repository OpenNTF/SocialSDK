<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.AssetNode"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.sbt.sample.web.util.SnippetFactory"%>
<%@page import="com.ibm.sbt.sample.web.util.Util"%>
<!DOCTYPE html>
<html lang="en" style="height: 100%;">
  <%
      String snippetName = request.getParameter("snippet");
      JSSnippet snippet = (JSSnippet)SnippetFactory.getJsSnippet(application, request, snippetName);
      String html = null;
      String js = null;
      String css = null;
      String theme = request.getParameter("themeId");
      boolean debug = false;
      boolean loadDojo = true;
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
      		js = ParameterProcessor.process(js, request);
          }
          if (StringUtil.isNotEmpty(html)) {
      		html = ParameterProcessor.process(html, request);
          }
      }
  %>
  <head>
    <meta charset="utf-8">
    <title>Social Business Toolkit - JavaScript Tester</title>
    
    <link href="images/sbt.png" rel="shortcut icon">
    <link rel="stylesheet" type="text/css" title="Style" href="/sbt.bootstrap211/bootstrap/css/bootstrap.css"></link>
    
    <jsp:include page="<%=Util.getLibraryInclude(request)%>" flush="false"/>
    
    <script type="text/javascript" src="<%=Util.getLibraryUrl(request)%>"></script>
    
    <script type="text/javascript">
    function assertEquals(val1, val2) {
	    if (val1 != val2) {
	        var error = new Error("Expected " + val2 + " to equal to " + val1);
	        handleError(error);
	    } else {
	    	trace(val2 + " matches expected value: " + val1);
	    }
	}
	
	function pass() {
	    document.getElementById("result").innerHTML = "Pass";
	}
	
	function trace(msg) {
	    var traceMsg = document.getElementById("trace").innerHTML;
		if (traceMsg) {
			traceMsg += "<br/>" + msg;
		} else {
			traceMsg = msg;
		}
		document.getElementById("trace").innerHTML = traceMsg; 
	}
	
	function fail(msg, error) {
		var errorMsg = document.getElementById("error").innerHTML;
		if (errorMsg) {
			errorMsg += "<br/>" + error.message;
		} else {
			errorMsg = error.message;
		}
	    document.getElementById("error").innerHTML = errorMsg;
	    
	    document.getElementById("result").innerHTML = "Fail: "+msg;
	}
	</script>
    
  </head>

  <body class="<%=Util.getBodyClass(request, theme)%>" style="width: 90%; height: 100%;">
	<div id="result" style="" class="alert"></div>
	<div id="trace" style="" class="alert alert-success"></div>
	<div id="error" style="" class="alert alert-error"></div>
    <%
    if (StringUtil.isNotEmpty(css)) {
        String s = "<style>\n" + css + "</style>\n";
        out.println(s);
    }
    if (StringUtil.isNotEmpty(html)) {
        out.println(html);
    }
    if (StringUtil.isNotEmpty(js)) {
        String s = "<script type='text/javascript'>\n" + js + "</script>\n";
        out.println(s);
    }
    %>
  </body>
</html>
