require(["sbt/dom", "sbt/connections/controls/activities/ActivityGrid"], function(dom, ActivityGrid) {
	
    var grid = new ActivityGrid({
		type: "my",
		theme: "bootstrap",
		hidePager: true,
		hideSorter: true,
		hideFooter:true
	});
     
     dom.byId("gridDiv").appendChild(grid.domNode);
              
     grid.update();
});