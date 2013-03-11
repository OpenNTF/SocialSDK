<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="java.util.List"%>
<%@page import="demo.DemoJSSnippetNode"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="demo.SnippetFactory"%>
<%@page import="com.ibm.sbt.playground.assets.AssetNode"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.jssnippets.JSSnippet"%>
<%@page import="demo.Util"%>
<div class="well sidebar-nav">
	<div class="btn-group">
		<button class="btn btn-mini" id="expandAll">
			<img src="images/expandall.png" alt="Expand All"></img>
		</button>
		<button class="btn btn-mini" id="collapseAll">
			<img src="images/collapseall.png" alt="Collapse All"></img>
		</button>
	</div>
	<div id="tree"></div>
	<%
	String json = SnippetFactory.getSnippetsAsJson(application, request);
	%>
    <script type="text/javascript">
        dojo.require("dojo.data.ItemFileReadStore");
        dojo.require( "dijit.Tree" );
        var rawdata=<%=json%>;
        
        function prepare(){
            var store = new dojo.data.ItemFileReadStore({
                data: { identifier: 'id', label : 'name', url: 'url', items: rawdata }
            });
            // hack to remove root node since dojo insists on adding its own.
            store._jsonData.items = store._jsonData.items[0].children;
            var treeModel = new dijit.tree.ForestStoreModel({ store: store });
            var tree = new dijit.Tree({
                model: treeModel,
                showRoot: false,
                openOnClick: true,
                autoExpand: true,
                _createTreeNode: function(args){
                    var tnode = new dijit._TreeNode(args);
                    tnode.labelNode.innerHTML = args.label;
                    
                    // Add the class leafNode to the dom node of leaf elements,
                    // so we can identify when leaf nodes are clicked
                    if(!tnode.isExpandable){
                        dojo.addClass(tnode.domNode, "leafNode");
                        tnode.domNode.id = args.item.url;
                    }
                    return tnode;
                }
            }, "tree" );

			// TODO want the tree to be autoExpand:false
			            
            dojo.byId("collapseAll").onclick = function(evt) {
            	tree.collapseAll();
            };
            dojo.byId("expandAll").onclick = function(evt) {
            	tree.expandAll();
            };
        }

        dojo.ready(prepare);
    </script>
</div>