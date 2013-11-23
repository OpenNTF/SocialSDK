require(["sbt/dom", "sbt/connections/controls/activities/ActivityGrid"], function(dom, ActivityGrid) {
    
    var domNode = dom.byId("myActivities");
    var CustomBookmarkRow = domNode.text || domNode.textContent;
	
    var grid = new ActivityGrid({
		type: "my",
		hideSorter: true,
		hidePager: true
	});

	 grid.renderer.template = CustomBookmarkRow;
     
     dom.byId("gridDiv").appendChild(grid.domNode);
              
     grid.update();
});