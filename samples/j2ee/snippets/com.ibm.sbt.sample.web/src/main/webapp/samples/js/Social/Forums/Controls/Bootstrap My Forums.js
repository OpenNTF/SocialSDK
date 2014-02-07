require(["sbt/dom", "sbt/connections/controls/forums/ForumGrid"], function(dom, ForumGrid) {    
	
    var grid = new ForumGrid({
        type: "my",
        theme: "bootstrap"
    });

	 grid.renderer.tableClass = "table-striped";
     
     dom.byId("gridDiv").appendChild(grid.domNode);
              
     grid.update();
});


