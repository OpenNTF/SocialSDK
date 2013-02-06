<%@page contentType="application/xml" %><?xml version="1.0" encoding="UTF-8"?>
<!-- 
/*
 * © Copyright IBM Corp. 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */ -->
 
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.util.HtmlTextUtil"%>
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<%@page import="com.ibm.sbt.playground.assets.Node"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.sbt.playground.assets.CategoryNode"%>
<%@page import="demo.SnippetFactory"%>
<%
RootNode rootNode = SnippetFactory.getSnippets(application);
String snippetName = request.getParameter("snippet");
JSSnippet snippet = (JSSnippet)rootNode.loadAssert(SnippetFactory.getRootFile(application), snippetName);
String[] labels = snippet.getLabels();
String name = (labels != null && labels.length > 0) ? labels[0] : snippetName;
String html = null;
String js = null;
String css = null;
if (snippet != null) {
	html = snippet.getHtml();
	js = snippet.getJs();
	css = snippet.getCss();
	
	// replace substitution variables
	if (js != null) {
		js = ParameterProcessor.process(js);
	}
}
%>
<Module>
  <ModulePrefs title="Social Business Toolkit Sample - <%=name%>">
    <Require feature="settitle"/>
    <Require feature="dynamic-height"/>
  </ModulePrefs>
  <Content type="html">
<![CDATA[
    <script type="text/javascript">
      // Set title (if supported by container)
      gadgets.window.setTitle('Social Business Toolkit Sample - <%=name%>');
    </script>
    <link href="/sbt/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/sbt/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
 	<script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js" djConfig="parseOnLoad: true"></script>
	<%
	String baseUrl = UrlUtil.getBaseUrl(request);
	String toolkit = PathUtil.concat(baseUrl,"/library?ver=1.8.0&env=openSocial",'/');
	%> 
	<script type="text/javascript" src="<%=toolkit%>"></script>
 	<div class="span9">
		<div>
			<pre><%=HtmlTextUtil.toHTMLContentString(js, false)%></pre>
		</div>
		<div id="content"></div>
		<div id="loading" style="visibility: hidden;"><img src="/sbt.sample.web/images/progressIndicator.gif"> </div>
		<div>
		<%
		if (StringUtil.isNotEmpty(css)) {
			String s = "<style>" + css + "</style>";
			out.println(s);
		}
		if (StringUtil.isNotEmpty(html)) {
			out.println(html);
		}
		if (StringUtil.isNotEmpty(js)) {
			String s = "<script>" + js + "</script>";
			out.println(s);
		}
		%>
		</div>
	</div>
    <script type="text/javascript">
      // Adjust the height of the gadget to fit the new data
      gadgets.window.adjustHeight();
    </script>
]]>
  </Content>
</Module>
