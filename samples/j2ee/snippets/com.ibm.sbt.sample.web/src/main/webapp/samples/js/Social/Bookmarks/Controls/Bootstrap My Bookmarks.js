require(["sbt/dom", 
         "sbt/connections/controls/bookmarks/BookmarkGrid"], 

function(dom, BookmarkGrid) {
		
    var grid = new BookmarkGrid({
		type: "private",
		theme: "bootstrap",
		hidePager:true,
		hideSorter:true,
		hideFooter:true
	});

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});