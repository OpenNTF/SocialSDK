<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.util.HtmlTextUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.sample.web.util.SnippetFactory"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
    <%
    	String snippetName = request.getParameter("snippet");
        JSSnippet snippet = SnippetFactory.getJsSnippet(application, request, snippetName);
        String html = null;
        String js = null;
        String css = null;
        String docHtml = null;
        if (snippet != null) {
            html = snippet.getHtml();
            js = snippet.getJs();
            css = snippet.getCss();
            docHtml = snippet.getDocHtml();
            
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
        String pre = "<pre>" + HtmlTextUtil.toHTMLContentString(html, false) + "</pre>";
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
    if(docHtml!=null && StringUtil.isNotEmpty(docHtml)){
        out.println(docHtml);
    }
    %>
    </div>
