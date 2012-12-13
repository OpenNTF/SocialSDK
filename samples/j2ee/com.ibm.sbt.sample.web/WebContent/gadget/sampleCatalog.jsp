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
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.commons.runtime.Context"%>
<%@page import="com.ibm.sbt.playground.snippets.RootNode"%>
<%@page import="com.ibm.sbt.playground.snippets.Snippet"%>
<%@page import="demo.DemoSnippetNode"%>
<%@page import="com.ibm.sbt.playground.snippets.AbstractNode"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.sbt.playground.snippets.CategoryNode"%>
<%@page import="demo.SnippetFactory"%>
<Module>
  <ModulePrefs title="Social Business Toolkit Samples">
    <Require feature="settitle"/>
    <Require feature="pubsub-2">
  		<Param name="topics">
    	<![CDATA[ 
    	<Topic title="Sample UNID" name="com.ibm.sbt.sample.unid"
               description="Publishes the UNID of a sample to be loaded and executed." 
               type="string" publish="true">
    	</Topic>
    	]]>
  		</Param>
	</Require>
  </ModulePrefs>
  <Content type="html">
<![CDATA[
    <script type="text/javascript">
    // Set title (if supported by container)
    gadgets.window.setTitle('Social Business Toolkit Samples');

	function publish(unid) {
		alert(unid);
  		gadgets.Hub.publish("com.ibm.sbt.sample.unid", unid);
	}
    </script>
    <link href="/sbt/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/sbt/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
	<div class="well sidebar-nav">
		<ul class="nav nav-list">
		<%
		RootNode rootNode = SnippetFactory.getSnippets(application);
		List<AbstractNode> children = rootNode.getAllChildrenFlat();
		for (int i=0; i<children.size(); i++) {
			AbstractNode node = children.get(i);
			if (node.isCategory()) {
				int level = ((node.getLevel() - 1) * 2) - 1;
				String name = node.getName();
		%>
			<li class="nav-header" style='margin-left: <%=level%>em'><%=name%></li>
		<%
			} else if (node.isSnippet()) {
				int level = (node.getLevel() - 1) * 2;
				String clazz = node.getUnid().equals(request.getParameter("snippet")) ? "active" : "";
				String unid = node.getUnid();
				String name = node.getName();
		%>
			<li class="<%=clazz%>" style='margin-left: <%=level%>em'>
				<a onclick="publish('<%=unid%>')"><%=name%> </a>
			</li>
		<%
			}
		}
		%>
		</ul>
	</div>
]]>
  </Content>
</Module>
 