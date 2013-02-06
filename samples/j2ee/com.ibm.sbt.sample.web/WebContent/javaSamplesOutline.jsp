<!DOCTYPE html>
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
 
<%@page import="com.ibm.sbt.playground.assets.AssetNode"%>
<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.commons.util.PathUtil"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<%@page import="demo.DemoJavaSnippetNode"%>
<%@page import="com.ibm.sbt.playground.assets.Node"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.sbt.playground.assets.CategoryNode"%>
<%@page import="demo.SnippetFactoryForJava"%>
<%@page import="com.ibm.sbt.services.endpoints.Endpoint"%>
<%@page import="com.ibm.sbt.services.endpoints.EndpointFactory"%>
<%@page buffer="500kb" autoFlush="false" %>

<link rel="stylesheet" href="/sbt.dojo180/dijit/themes/tundra/tundra.css" type="text/css" />
<!-- Manually adding dojo -->
<script type="text/javascript" src="/sbt.dojo180/dojo/dojo.js" djConfig="parseOnLoad: true"></script>

<%
	RootNode root = SnippetFactoryForJava.getSnippets(application);
%>

<%
	if(true) {
%>

<!-- REGULAR NAVIGATOR IMPLEMENTATION -->

          <div class="well sidebar-nav">
			<ul class="nav nav-list">
								<%
									List<Node> allSnippets = root.getAllChildrenFlat();
															for(int i=0; i<allSnippets.size(); i++) {
																Node node = allSnippets.get(i);
								%>
									<%
										if(node.isCategory()) {
									%>
											<li class="nav-header" style='margin-left: <%=((node.getLevel()-1)*2)-1%>em'><%=node.getName()%></li>
									<%
										} else if(node.isAsset()) {
									%>
											 <%
											 	String snippetPath = ((DemoJavaSnippetNode)node).getPath();
											 										if (snippetPath.indexOf("Smartcloud")>0) {
											 											endpointName = "smartcloud";
											 										} else {
											 											endpointName = "connections";
											 										}
											 %>
											 <%
											 	if((((DemoJavaSnippetNode)node).getPath()+".jsp").equals(request.getParameter("snippet"))){
											 %>
													<li class = "active" style='margin-left: <%=(node.getLevel()-1)*2%>em'> 
											 <%
 											 	}else{
 											 %>	 
													 <li style='margin-left: <%=(node.getLevel()-1)*2%>em'>
											    <%
											    	}
											    %>
											    
														<a href="<%=((DemoJavaSnippetNode)node).getJSPUrl(request)%>&endpoint=<%=endpointName%>">
															<%=node.getName()%>
														</a>
													</li>
									<% } %>
								<%
									}
								%> 
							</ul>
          </div><!--/.well -->


<% } else { %>


<!-- TREE IMPLEMENTATION -->

<script type="text/javascript">


function treeCollapseAll(tree) {
	tree = dijit.byId(tree);
	if(tree.collapseAll) { // 1.8 and later
		tree.collapseAll();
	} else {
        function collapse(node) {
        	if(node!=tree.rootNode) {
        		tree._collapseNode(node);
        	}
            dojo.map(node.getChildren(),collapse);
        }
        return collapse(tree.rootNode);
    }
}

function treeExpandAll(tree) { 
	tree = dijit.byId(tree);
	if(tree.expandAll) { // 1.8 and later
		tree.expandAll();
	} else {
        function expand(node) {
        	if(node!=tree.rootNode) {
        		tree._expandNode(node);
        	}
            dojo.map(node.getChildren(),expand);
        }
        return expand(tree.rootNode);
    }
}

function treeExpandId(tree,id) {
	tree = dijit.byId(tree);
	function itemPath(model,items,item){
	    items.push(item.id);
	    if(item.id==id){
	    	return items;
	    }
	    var cc = item.children;
	    for(var i in cc){
	        if(itemPath(model,items,cc[i])) {
	        	return items;
	        }
	    }
	    items.pop();
	    return undefined;
	}
	return itemPath(tree.model,[],tree.model.root);
}
</script>
			<div class="well sidebar-nav">

			<div style="margin-bottom: 5px">
				<span title="Expand All" style="margin-left: 3px; margin-right: 3px">
					<a id="link1" onclick="treeExpandAll('snippetsTree');">
						<img id="image2" alt="Expand All" src="images/expandall.png"></img>
					</a>
				</span>
				<span title="Collapse All" style="margin-left: 3px; margin-right: 8px">
					<a id="link1" onclick="treeCollapseAll('snippetsTree');">
						<img id="image2" alt="Expand All" src="images/collapseall.png"></img>
					</a>
				</span>
			</div>

			<div class="well sidebar-nav">
				<script id="scriptBlock1">
					require(['dojo/ready','dijit/tree/TreeStoreModel', 'dojo/data/ItemFileReadStore','dijit/Tree'], function(ready,TreeStoreModel,ItemFileReadStore,Tree){
						ready(function() {
							var snippetsData = <%=root.getAsJson()%>;
							var snippetsStore = new ItemFileReadStore({data:{identifier:'id',label:'name',items:snippetsData}});
							var snippetsModel = new TreeStoreModel({
							    store: snippetsStore
							});
							var navTree = new Tree({model: snippetsModel, showRoot: false }, "snippetsTree");
							navTree.onClick = function(item){
								if(item.url) {
									var baseUrl = '<%=UrlUtil.getRequestUrl(request,false)%>';
									window.location.href = baseUrl+"?snippet="+item.jspUrl+".jsp";
								}
							};
							navTree.startup();
						});
					});
				</script>
				<div id="snippetsTree"></div>
			</div>
		</div>


<% }%>

