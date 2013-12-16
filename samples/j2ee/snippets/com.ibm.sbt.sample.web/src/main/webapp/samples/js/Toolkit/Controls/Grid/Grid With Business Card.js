require(["sbt/dom", "sbt/connections/controls/files/FileGrid"], function(dom, FileGrid) {
        var grid = new FileGrid({
	         type : "myFiles",
	         displayBusinessCard: true
	    });
		         
	    dom.byId("gridDiv").appendChild(grid.domNode);
  
  		grid.postCreate = function(){this.inherited(arguments);};
		         
	    grid.update();
});