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
                String value = ParameterProcessor.getWebProvider(request, session, snippetName).getParameter(param);
                String name = ParameterProcessor.getParameterPart(param, "label");
                if(name == null){
                    name = ParameterProcessor.getParameterPart(param, "name");
                }
                
                String storeKey = snippetName + "_" + name;
                String storedValue = (String) session.getAttribute(storeKey);
                if(storedValue != null){
                    value = storedValue;
                }
                
                if(value == null){
                    value = "";
                }
                
                boolean isRequired = true;
                String requiredParam = ParameterProcessor.getParameterPart(param, "required");
                if(requiredParam != null && requiredParam.equalsIgnoreCase("false")){
                    isRequired = false;
                }
                String requiredMarkUp = isRequired ? "<span id='requiredMarker' style='display:inline; color:red;'>*</span>" : "";

                String helpSnippetId = ParameterProcessor.getParameterPart(param, "helpSnippetId");
                String helpLinkMarkUp = helpSnippetId != null ? "<a target='_blank' href='javascriptPreview.jsp?snippet=" + helpSnippetId + "'>" + name + "</a>" : name;
            %>
            <tr>
                <th style="vertical-align:middle;" scope="row"><%=helpLinkMarkUp%><%=requiredMarkUp%></th>
                <td><input style="vertical-align:middle;margin-bottom:0px;" type="text" name="<%=name %>" value="<%=value %>" ></td>
            </tr>
            <%
            }
            %>
        </table>
        <p id="paramsMissingError" class="text-error hide"></p>
    </div>
    