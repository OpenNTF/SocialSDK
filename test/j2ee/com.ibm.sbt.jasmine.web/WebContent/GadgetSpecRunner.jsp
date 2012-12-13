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
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.sbt.playground.snippets.SnippetNode"%>
<%@page import="com.ibm.sbt.playground.snippets.RootNode"%>
<%@page import="com.ibm.sbt.playground.snippets.Snippet"%>
<%@page import="com.ibm.sbt.playground.snippets.AbstractNode"%>
<%@page import="com.ibm.sbt.playground.snippets.CategoryNode"%>
<%@page import="com.ibm.sbt.jasmine.SpecFactory"%>
<%@page import="com.ibm.sbt.jasmine.SpecCategory"%>
<%@page import="com.ibm.sbt.jasmine.SpecNode"%>
<%@page import="com.ibm.sbt.jasmine.SnippetFactory"%>
<%@page import="java.util.List"%>
<Module>
  <ModulePrefs title="Social Business Toolkit Unit Tests">
    <Require feature="settitle"/>
    <Require feature="dynamic-height"/>
  </ModulePrefs>
  <Content type="html">
<![CDATA[
    <script type="text/javascript">
      // Set title (if supported by container)
      gadgets.window.setTitle('Social Business Toolkit Unit Tests');
    </script>
    <link rel="stylesheet" type="text/css" href="jasmine/jasmine.css">
    <script type="text/javascript" src="jasmine/jasmine.js"></script>
    <script type="text/javascript" src="jasmine/jasmine-html.js"></script>

    <!-- include source files here... -->
    <script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js" djConfig="parseOnLoad: true"></script>
    <script type="text/javascript" src="/sbt.jasmine.web/library?lib=dojo&ver=1.8.0&context=gadget"></script>
    <script type="text/javascript" src="js/ExecJasmine.js"></script>
  
    <!-- include spec files here... -->
    <script type="text/javascript" src="spec/SpecHelper.js"></script>
    <%
  	RootNode rootNode = SpecFactory.getSpecs(application);
  	List<AbstractNode> children = SpecFactory.getSpecNodes(rootNode);
	for (int i = 0; i < children.size(); i++) {
		AbstractNode node = children.get(i);
		if (node.isSnippet()) {
			String unid = node.getUnid();
			Snippet snippet = rootNode.loadSnippet(SpecFactory.getRootFile(application), unid);
			String js = snippet.getJs();
			String html = snippet.getHtml();
    %>  
    <script type="text/javascript">
    <%=js%>
    </script>
    <div id="specHtml<%=unid%>" style="visibility:hidden">
    <%=html%>
    </div>
    <%
		}
	}
    %>  
    <script type="text/javascript">
      // Execute the unit tests
      execJasmine();
      
      // Adjust the height of the gadget to fit the new data
      gadgets.window.adjustHeight(1000);
    </script>
]]>
  </Content>
</Module>
 