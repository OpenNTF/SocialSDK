require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/lang"], 
         
function(dom, CommunityGrid, lang) {
    var grid = new CommunityGrid({
    		theme:"bootstrap",
    		hidePager:true,
	    	hideSorter:true,
	    	hideFooter:true
    	});
           
    dom.byId("gridDiv").appendChild(grid.domNode);
             
    grid.update();
});


