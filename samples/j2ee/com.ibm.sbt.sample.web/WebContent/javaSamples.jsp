<!DOCTYPE html>
<!-- 
/*
 * © Copyright IBM Corp. 2012
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */ -->
<%@page import="demo.DemoRootNode"%>
<%@page import="com.ibm.commons.util.HtmlTextUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.playground.snippets.RootNode"%>
<%@page import="com.ibm.sbt.playground.snippets.Snippet"%>
<%@page import="demo.DemoSnippetNode"%>
<%@page import="com.ibm.sbt.playground.snippets.AbstractNode"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.sbt.playground.snippets.CategoryNode"%>
<%@page import="demo.SnippetFactoryForJava"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page buffer="500kb" autoFlush="false" %>
<html>
<head>
	<title>SBT Samples</title>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <link href="/sbt/bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 50px;
      }
    </style>
    <link href="/sbt/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
	
	<script type="text/javascript">
function hideJsSnippetCode() {
	var jspCodePre =  document.getElementById("jspCodePre");
	var jspCodeSpan =  document.getElementById("jspCodeSpan");
	var accordianInner =  document.getElementById("accordianInner");
	var ShowCodeLink =  document.getElementById("ShowCodeLink");
	var HideCodeLink =  document.getElementById("HideCodeLink");
	if(jspCodePre.style.height == '0px'){
		ShowCodeLink.style.display = 'none';
		HideCodeLink.style.display = '';
		accordianInner.style.display = '';
		jspCodePre.style.height = 'auto';
		jspCodePre.style.display = '';
		jspCodeSpan.style.display = '';
	}else{
		ShowCodeLink.style.display = '';
		HideCodeLink.style.display = 'none';
		accordianInner.style.display = 'none';
		jspCodeSpan.style.display = 'none';
		jspCodePre.style.height = '0px';
		jspCodePre.style.display = 'none';
	}
}
</script>
</head>

  <body class="tundra">
    
   	<%
       		String endpointName = request.getParameter("endpoint");
       			if(endpointName==null) {
       		endpointName = "connections";
       			}
       			Endpoint ep = EndpointFactory.getEndpoint(endpointName);
       			if(!ep.isAuthenticationValid()) {	// Check, Do we have a valid token for this user
       		ep.authenticate(true);			// Authenticate
       	    		return;							
       	    	}
       	%>

    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="brand">Social Business Toolkit - Java Samples</a>
          <div class="nav-collapse collapse">
										<ul class="nav">
											<li class="active"><a href='samplesHome.html'>Home</a></li>
										</ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>

    <div class="container-fluid">
      <div class="row-fluid">
        <div class="span3">
          <%@include file="javaSamplesOutline.jsp"%>
        </div><!--/span-->
        <div class="span9">
					<div>
				
				
	<%
		
		String javaSamplePath= request.getParameter("javaSamplePath");
			if(javaSamplePath!=null) {
				Snippet snippet = root.loadSnippetForJava(SnippetFactoryForJava.getRootFile(application), javaSamplePath);
				String jsp = null;
				if (snippet != null) {
					jsp = snippet.getJsp();
					
					// replace substitution variables
					if (jsp != null) {
						jsp = ParameterProcessor.process(jsp);
					}
				}
				
				if (StringUtil.isNotEmpty(jsp)) {
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
		   				<div class="accordion-body" id="jspSnippetCode" style="height: auto;">
							 <div class="accordion-inner" id="accordianInner" style="display:none">
								<%
								
									String s = "<pre id='jspCodePre' style='height:0px;'><span id='jspCodeSpan'>\n"
											+ HtmlTextUtil.toHTMLContentString(jsp, false)
											+ "</span></pre>\n";
									out.println(s);
								}
								%>
							</div>
		         		</div>
		               </div>
		             </div>
				
	       <%		
				javaSamplePath = PathUtil.concat("/java", javaSamplePath, '/');
		   %>
		   	
	      <jsp:include page="<%=javaSamplePath %>" flush="false"/>
		  <% }%>
		  			</div>
        </div><!--/span-->
      </div><!--/row-->


    </div>

  </body>
</html>
