require(["sbt/dom", "sbt/lang", "sbt/connections/controls/files/FileGrid"], function(dom,lang, FileGrid) {
        var grid = new FileGrid({
	         type : "myFiles",
	         theme: "bootstrap",
	         hideSorter:true,
	         hidePager:true,
	         hideFooter:true	        	 
	    });
        
	    dom.byId("gridDiv").appendChild(grid.domNode);
		         
	    grid.update();
});