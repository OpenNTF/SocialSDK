require(["sbt/dom", "sbt/connections/controls/forums/ForumGrid"], function(dom, ForumGrid) {
	
    var grid = new ForumGrid({
        type: "public",
    	theme: "bootstrap",        
    	hidePager:true,
        hideSorter:true,
        hideFooter: true
    });
     
     dom.byId("gridDiv").appendChild(grid.domNode);
              
     grid.update();
    
});