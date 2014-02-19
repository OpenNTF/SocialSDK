require(["sbt/dom", 
         "sbt/connections/controls/communities/CommunityGrid",
         "sbt/lang"], 
         
      function(dom, CommunityGrid, lang) {
		  var grid = new CommunityGrid({
		        type: "my",
		        theme: "bootstrap",
		        hidePager:true,
		    	hideSorter:true,
		    	hideFooter:true
		  });

		  grid.renderer.tableClass = "table";
		  dom.byId("gridDiv").appendChild(grid.domNode);
		  grid.update();
	});


