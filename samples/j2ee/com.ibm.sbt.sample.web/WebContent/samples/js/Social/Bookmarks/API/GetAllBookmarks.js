require(["sbt/connections/BookmarkService", "sbt/dom", "sbt/json"], 
    function(BookmarkService, dom, json) {
        var bookmarkService = new BookmarkService();  
        var bookmark = bookmarkService.newBookmark(); 
        var now = new Date();
        bookmark.setTitle("Bookmark at " + now.getTime());
        bookmark.setUrl("ibm.com");
    	bookmarkService.createBookmark(bookmark).then(
            function(){
            	return bookmarkService.getAllBookmarks({ ps: 5 });
            }
       ).then(
       		function(bookmarks) {
                dom.setText("json", json.jsonBeanStringify(bookmarks));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
       ); 
	}
);