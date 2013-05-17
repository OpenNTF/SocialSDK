var grid;
require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        grid = new FileGrid({
	         type : "recycledFiles"
	    });
		         
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});