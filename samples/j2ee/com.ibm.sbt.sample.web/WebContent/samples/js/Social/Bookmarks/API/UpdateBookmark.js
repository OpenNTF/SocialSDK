require(["sbt/connections/BookmarkService", "sbt/dom", "sbt/json"], 
    function(BookmarkService, dom, json) {
		var now = new Date();
        var bookmarkService = new BookmarkService();  
    	bookmarkService.getAllBookmarks({ ps: 1 }).then(
            function(bookmarks){
            	var bookmark = bookmarks[0];
                bookmark.setTitle("Bookmark Updated at " + now.getTime());
		        return bookmarkService.updateBookmark(bookmark);
            }
        ).then(
    		function(updatedBookmark) {
                dom.setText("json", json.jsonBeanStringify(updatedBookmark.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );		        
    }
);