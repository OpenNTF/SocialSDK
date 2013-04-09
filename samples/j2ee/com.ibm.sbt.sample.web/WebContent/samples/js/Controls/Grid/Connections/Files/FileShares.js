require(["sbt/dom", "sbt/controls/grid/connections/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "fileShares",
	         direction : "inbound"
	    });
		         
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});