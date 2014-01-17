require(["sbt/dom", "sbt/connections/controls/bookmarks/BookmarkGrid"], function(dom, BookmarkGrid) {
    
	var grid = new BookmarkGrid({
		type: "private",
		targetName: "_blank"
	});

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});