<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.util.HtmlTextUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="demo.SnippetFactory"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
    <%
    String snippetName = request.getParameter("snippet");
    JSSnippet snippet = SnippetFactory.getSnippet(application, request, snippetName);
    String html = null;
    String js = null;
    String css = null;
    String[] doc = null;
    if (snippet != null) {
        html = snippet.getHtml();
        js = snippet.getJs();
        css = snippet.getCss();
        doc = snippet.getDocumentation();
        
        // replace substitution variables
        if (StringUtil.isNotEmpty(js)) {
            js = ParameterProcessor.process(js);
        }
        if (StringUtil.isNotEmpty(html)) {
            html = ParameterProcessor.process(html);
        }
    }
    %>
    <div id="jsContents">
    <%
    if (StringUtil.isNotEmpty(js)) {
        String pre = HtmlTextUtil.toHTMLContentString(js, false);
        out.println(pre);
    }
    %>
    </div>
    <div id="htmlContents" style="display: hidden;">
    <%
    if(StringUtil.isNotEmpty(html)){
        String pre = HtmlTextUtil.toHTMLContentString(html, false);
        out.println(pre);
    }
    %>
    </div>
    <div id="cssContents" style="display: hidden;">
    <% 
    if(StringUtil.isNotEmpty(css)){
        String pre = HtmlTextUtil.toHTMLContentString(css, false);
        out.println(pre);
    }
    %>
    </div>
    
    <div id="docContents" style="display: hidden;">
    <% 
    if(doc!=null && StringUtil.isNotEmpty(doc[0])){
        String pre = HtmlTextUtil.toHTMLContentString(doc[0], false);
        out.println(pre);
    }
    %>
    </div>
