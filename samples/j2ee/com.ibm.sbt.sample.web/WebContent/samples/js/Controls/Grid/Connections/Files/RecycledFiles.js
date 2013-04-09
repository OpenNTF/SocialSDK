var grid;
require(["sbt/dom", "sbt/controls/grid/connections/FileGrid"], function(dom, FileGrid) {
        grid = new FileGrid({
	         type : "recycledFiles"
	    });
		         
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});