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
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.assets.Node"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SBT Samples</title>

<!-- Le styles -->
<link href="/sbt/bootstrap/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
body {
	padding-top: 50px;
}
</style>
<link href="/sbt/bootstrap/css/bootstrap-responsive.css"
	rel="stylesheet">
<!-- THIS CAN BE CALCULATED -->
<!-- Manually adding dojo -->

<script type="text/javascript"
	src="<%=request.getAttribute("jsLibPath")%>"></script>
<link rel="stylesheet" href="<%=request.getAttribute("jsLibCSSPath")%>"
	type="text/css" />



<!-- Include the toolkit -->
<script type="text/javascript"
	src="<%=request.getAttribute("toolkit")%>"></script>


<script type="text/javascript">
function hideJsSnippetCode() {
	var jsCodePre =  document.getElementById("jsCodePre");
	var jsCodeSpan =  document.getElementById("jsCodeSpan");
	var accordianInner =  document.getElementById("accordianInner");
	var ShowCodeLink =  document.getElementById("ShowCodeLink");
	var HideCodeLink =  document.getElementById("HideCodeLink");
	if(jsCodePre.style.height == '0px'){
		ShowCodeLink.style.display = 'none';
		HideCodeLink.style.display = '';
		accordianInner.style.display = '';
		jsCodePre.style.height = 'auto';
		jsCodePre.style.display = '';
		jsCodeSpan.style.display = '';
	}else{
		ShowCodeLink.style.display = '';
		HideCodeLink.style.display = 'none';
		accordianInner.style.display = 'none';
		jsCodeSpan.style.display = 'none';
		jsCodePre.style.height = '0px';
		jsCodePre.style.display = 'none';
	}
}
</script>

</head><!-- opening tag is in dojoXXX.jsp that includes this -->

<body class='<%=request.getAttribute("jsLibTheme")%>'>

	<div class="navbar navbar-fixed-top">
		<div class="navbar-inner">
			<div class="container-fluid">
				<a class="brand">Social Business Toolkit - JavaScript Samples</a>
				<div class="nav-collapse collapse">
					<ul class="nav">
						<li class="active"><a href='samplesHome.html'>Home</a>
						</li>
					</ul>
				</div>
				<!--/.nav-collapse -->
			</div>
		</div>
	</div>

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span3">
				<%@include file="jsSamplesOutline.jsp"%>
			</div>
			<div class="span8">
				<div>
					<!-- Div content for the pure JavaScript sample -->
					<div>
						<!-- Display the current code snippet -->
						<%
							String snippetName = request.getParameter("snippet");
							JSSnippet snippet = (JSSnippet)root.loadAsset(SnippetFactory.getRootFile(application), snippetName);
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
							if (StringUtil.isNotEmpty(js) || StringUtil.isNotEmpty(html)) {
							%>
							
		              <div id="accordion2" class="accordion">
		                <div class="accordion-group">
		                  <div class="accordion-heading">
		       				<a id="ShowCodeLink" onclick="hideJsSnippetCode()" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle">
		         					Show Snippet Code
		       				</a>
		       				<a id="HideCodeLink" style="display:none" onclick="hideJsSnippetCode()" data-parent="#accordion2" data-toggle="collapse" class="accordion-toggle">
		         					Hide Snippet Code
		       				</a>
		                  </div>
		   				<div class="accordion-body" id="jsSnoppetCode" style="height: auto;">
							 <div class="accordion-inner" id="accordianInner" style="display:none">
								<%
								if (StringUtil.isNotEmpty(js)) {
									String s = "<pre id='jsCodePre' style='height:0px;'><span id='jsCodeSpan'>"
											+ HtmlTextUtil.toHTMLContentString(js, false)
											+ "</span></pre>\n";
									out.println(s);
								}
								if (StringUtil.isNotEmpty(html)) {
									String s = "<pre id='jsCodePre' style='height:0px;'><span id='jsCodeSpan'>"
											+ HtmlTextUtil.toHTMLContentString(html, false)
											+ "</span></pre>\n";
									out.println(s);
								}
								}
								%>
							</div>
		         		</div>
		               </div>
		             </div>
					</div>
					<div id="content"></div>
					<div id="loading" style="visibility: hidden;"><img src="/sbt.sample.web/images/progressIndicator.gif"> </div>
					<div>
						<!-- Display the current code snippet -->
						<%
							if (StringUtil.isNotEmpty(css)) {
								String s = "<style>\n" 
									+ css 
									+ "</style>\n";
								out.println(s);
							}
							if (StringUtil.isNotEmpty(html)) {
								out.println(html);
							}
							if (StringUtil.isNotEmpty(js)) {
								String s = "<script>\n"
										+ js
										+ "</script>\n";
								out.println(s);
							}
						%>
					</div>
				</div>
				<!--/span-->
			</div>
			<!--/row-->
		</div>
	</div>
</body>
</html><!-- opening tag is in dojoXXX.jsp that includes this -->
