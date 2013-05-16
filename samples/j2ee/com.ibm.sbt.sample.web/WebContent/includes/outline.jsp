<%@page import="com.ibm.commons.runtime.util.UrlUtil"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="com.ibm.sbt.playground.assets.AssetNode"%>
<%@page import="com.ibm.sbt.playground.assets.RootNode"%>
<%@page import="java.util.List"%>
<%@page import="com.ibm.commons.util.StringUtil"%>
<%@page import="com.ibm.commons.runtime.util.ParameterProcessor"%>
<%@page import="com.ibm.sbt.sample.web.util.SnippetFactory"%>
<%@page import="com.ibm.sbt.playground.assets.javasnippets.JavaSnippet"%>
<%@page import="com.ibm.sbt.sample.web.util.Util"%>
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
    String json = "";
    if(request.getRequestURL().toString().endsWith("java.jsp"))
        json = SnippetFactory.getJavaSnippets(application).getAsJson();
    else if(request.getRequestURL().toString().endsWith("javascript.jsp"))
    	json = SnippetFactory.getJsSnippetsAsJson(application, request);
        
    %>
    <script type="text/javascript">
    require(["dojo/_base/declare", "dojo/ready", "dojo/dom", "dojo/dom-class", "dojo/store/Memory",
             "dijit/tree/ObjectStoreModel", "dijit/Tree"], 
        function(declare, ready, dom, domClass, Memory, ObjectStoreModel, Tree){
             var rawData=<%=json%>;
             var treeStore = new Memory({
                 data: rawData,
                 getChildren: function(object){
                     return object.children !== undefined ? object.children : [];
                 }
             });
             var treeModel = new ObjectStoreModel({
                 store: treeStore,
                 query: {id: '_root'},
                 mayHaveChildren: function(item){
                     return item.hasOwnProperty("children");
                 }
             });
             ready(function(){
                 var tree = new Tree({
                     model: treeModel,
                     showRoot: false,
                     openOnClick: true,
                     _createTreeNode: function(args){
                         var treeNode = new Tree._TreeNode(args);
                         treeNode.labelNode.innerHTML = args.label;
                         
                         // Add the class leafNode to the dom node of leaf elements,
                         // so we can identify when leaf nodes are clicked
                         if(!args.item.children){
                             domClass.add(treeNode.domNode, "leafNode");
                             treeNode.domNode.id = args.item.url;
                             setTimeout(function(){//small timeout to ensure that when this event is fired the tnode is already returned.
                                 var tree = dom.byId("tree");
                                 var event;
                                 if(document.createEvent){
                                     event = document.createEvent('HTMLEvents');
                                     event.initEvent("newNodeEvent", true, true);
                                 }else if(document.createEventObject){// IE < 9
                                     event = document.createEventObject();
                                     event.eventType = "newNodeEvent";
                                 }
                                 tree.dispatchEvent(event);
                             }, 30);
                         }
                         return treeNode;
                     }
                 });
                 tree.placeAt(dom.byId("tree"));
                 tree.startup();
                 
                 dom.byId("collapseAll").onclick = function(evt) {
                     tree.collapseAll();
                 };
                 
                 dom.byId("expandAll").onclick = function(evt) {
                     tree.expandAll();
                 };
             });
         });
    </script>
</div>