require(["sbt/dom", "sbt/connections/controls/forums/ForumGrid"], function(dom, ForumGrid) {    
	
    var grid = new ForumGrid({
        type: "my",
        theme: "bootstrap",
        hidePager:true,
        hideSorter:true,
        hideFooter: true
    });

	 grid.renderer.tableClass = "table-striped";
     
     dom.byId("gridDiv").appendChild(grid.domNode);
              
     grid.update();
});


