require(["sbt/dom", "sbt/connections/controls/bookmarks/BookmarkGrid"], function(dom, BookmarkGrid) {
    
	var grid = new BookmarkGrid({
		type: "community",
		communityUuid: "%{name=CommunityService.communityUuid|label=communityId|helpSnippetId=Social_Communities_Get_My_Communities}"
	});

    dom.byId("gridDiv").appendChild(grid.domNode);

    grid.update();
});