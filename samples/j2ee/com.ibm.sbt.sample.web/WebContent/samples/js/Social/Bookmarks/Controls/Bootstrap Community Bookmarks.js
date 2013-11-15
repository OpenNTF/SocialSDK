require(["sbt/dom", "sbt/connections/controls/bookmarks/BookmarkGrid"], function(dom, BookmarkGrid) {
    
	var domNode = dom.byId("bookmarkRow");
    var CustomBookmarkRow = domNode.text || domNode.textContent;
	
	var grid = new BookmarkGrid({
		type: "community",
		communityUuid: "%{name=CommunityService.communityUuid|label=communityId|helpSnippetId=Social_Communities_Get_My_Communities}",
		hideSorter: true,
		hidePager: true
	});

	 grid.renderer.template = CustomBookmarkRow;
     
     dom.byId("gridDiv").appendChild(grid.domNode);
              
     grid.update();

});