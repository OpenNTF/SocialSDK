require(["sbt/dom", "sbt/lang", "sbt/connections/controls/communities/CommunityGrid", "sbt/dom"], 
    function(dom, lang, CommunityGrid) {
	    var grid = new CommunityGrid({
	    	theme:"bootstrap",
	    	hidePager:true,
	    	hideSorter:true,
	    	hideFooter:true
	    });
	    
	    dom.byId("gridDiv").appendChild(grid.domNode);
	             
	    grid.update();
});


