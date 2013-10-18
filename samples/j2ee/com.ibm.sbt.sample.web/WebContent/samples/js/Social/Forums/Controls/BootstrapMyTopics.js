require(["sbt/dom", 
         "sbt/connections/controls/forums/ForumGrid"], 
function(dom, ForumGrid) {
	var domNode = dom.byId("myForums");
    var CustomForumRow = domNode.text || domNode.textContent;
	
    var grid = new ForumGrid({
         type: "myTopics",
         hideSorter: true,
 		 hidePager: true
    });
    
    grid.renderer.template = CustomForumRow;
             
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
    
});