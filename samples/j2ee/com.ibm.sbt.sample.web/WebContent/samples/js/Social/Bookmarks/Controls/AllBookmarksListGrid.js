require(["sbt/dom", "sbt/connections/controls/bookmarks/BookmarkGrid"], function(dom, BookmarkGrid) {
    
	var grid = new BookmarkGrid({
		type: "any",
		containerType : "ul"
	});

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});