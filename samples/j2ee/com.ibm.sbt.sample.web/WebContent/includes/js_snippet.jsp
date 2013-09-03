<%@page import="java.util.List"%>
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
    <div id="htmlContents" style="display: none;">
    <%
    if(StringUtil.isNotEmpty(html)){
        String pre = "<pre>" + HtmlTextUtil.toHTMLContentString(html, false) + "</pre>";
        out.println(pre);
    }
    %>
    </div>
    <div id="cssContents" style="display: none;">
    <% 
    if(StringUtil.isNotEmpty(css)){
        String pre = HtmlTextUtil.toHTMLContentString(css, false);
        out.println(pre);
    }
    %>
    </div>
    
    <div id="docContents" style="display: none;">
    <% 
    if(docHtml!=null && StringUtil.isNotEmpty(docHtml)){
        out.println(docHtml);
    }
    %>
    </div>
    <% 
    List <String> paramList = ParameterProcessor.getParameters(html+js);
    
    %>
    <div id="propertyContents" style="display: none;">
        
        <table style="width:20px" class="table" >
        <%if(paramList.size() > 0){%>
            <tr>
                <th scope="col">Property</th>
                <th scope="col">Value</th>
            </tr>
        <%}%>
        
            <%
            for(String param : paramList){
                String value = "";
                String storeKey = snippetName + "_" + param;
                String storedValue = (String) session.getAttribute(storeKey);
                if(storedValue != null){
                    value = storedValue;
                }
                Context con = Context.get();
                if(con.getProperty(param + ".idHelpSnippet") == null){
            %>
            <tr>
                <th style="vertical-align:middle;" scope="row"><%=param %></th>
                <td><input style="vertical-align:middle;margin-bottom:0px;" type="text" name="<%=param %>" value="<%=value %>" ></td>
            </tr>
            <%
                }
            else{
            %>
            <tr>
                <th style="vertical-align:middle;" scope="row"><a target="_blank" href="javascriptPreview.jsp?snippet=<%=con.getProperty(param + ".idHelpSnippet")%>"><%=param %></a></th>
                <td><input style="vertical-align:middle;margin-bottom:0px;" type="text" name="<%=param %>" value="<%=value %>" ></td>
            </tr>
            <%
                }
            }
            %>
        </table>
        <p id="paramsMissingError" class="text-error hide"></p>
    </div>
    