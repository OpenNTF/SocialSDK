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
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.javasnippets.JavaSnippetAssetNode"%>
<%@page import="demo.DemoJavaSnippetNode"%>
<%@page import="com.ibm.sbt.playground.assets.Node"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.sbt.playground.assets.CategoryNode"%>
<%@page import="demo.SnippetFactory"%>
<Module>
  <ModulePrefs title="Social Business Toolkit Samples">
    <Require feature="settitle"/>
    <Require feature="pubsub-2">
  		<Param name="topics">
    	<![CDATA[ 
    	<Topic title="Sample URL" name="com.ibm.sbt.sample.url"
               description="Publishes the URL of a sample to be loaded and executed." 
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

	function publish(uuid) {
  		gadgets.Hub.publish("com.ibm.sbt.sample.url", uuid);
	}
    </script>
    <link href="/sbt/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="/sbt/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
	<div class="well sidebar-nav">
		<ul class="nav nav-list">
		<%RootNode rootNode = SnippetFactory.getSnippets(application);
		List<Node> children = rootNode.getAllChildrenFlat();
		for (int i=0; i<children.size(); i++) {
			Node node = children.get(i);
			if (node.isCategory()) {
				int level = ((node.getLevel() - 1) * 2) - 1;
				String name = node.getName();%>
			<li class="nav-header" style='margin-left: <%=level%>em'><%=name%></li>
		<%} else if (node.isAsset()) {
				int level = (node.getLevel() - 1) * 2;
				String clazz = node.getUnid().equals(request.getParameter("snippet")) ? "active" : "";
				String url = ((DemoJavaSnippetNode)node).getJSPUrl(request);
				String name = node.getName();%>
			<li class="<%=clazz%>" style='margin-left: <%=level%>em'>
				<a onclick="publish('<%=url%>')"><%=name%> </a>
			</li>
		<%
			}
		}
		%>
		</ul>
	</div>
	<div class="span9">
		<div>
			<div>
			</div>
		<div id="content"></div>
		<div id="loading" style="visibility: hidden;"><img src="/sbt.sample.web/images/progressIndicator.gif"> </div>
			<div>
			</div>
		</div>
	</div>
]]>
  </Content>
</Module>
 