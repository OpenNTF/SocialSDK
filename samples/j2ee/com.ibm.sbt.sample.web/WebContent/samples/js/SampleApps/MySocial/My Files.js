require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "library",
	         pinFile: true
	    });
        grid.renderer.template = dom.byId("fileRow").innerHTML;
        grid.renderer.pagerTemplate = dom.byId("pagerTemplate").innerHTML;
        grid.renderer.sortTemplate = dom.byId("sortingTemplate").innerHTML;
        grid.renderer.sortAnchor = dom.byId("sortAnchorTemplate").innerHTML;
        grid.renderer.tableClass = "table table-striped";       
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});