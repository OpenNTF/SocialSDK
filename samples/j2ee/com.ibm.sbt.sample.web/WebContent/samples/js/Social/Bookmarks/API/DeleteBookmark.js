require(["sbt/connections/BookmarkService", "sbt/dom", "sbt/json"], 
    function(BookmarkService, dom, json) {
        var bookmarkService = new BookmarkService();
        var bookmark = bookmarkService.newBookmark(); 
        var now = new Date();
        bookmark.setTitle("Bookmark at " + now.getTime());
        bookmark.setUrl("ibm.com");
    	bookmarkService.createBookmark(bookmark).then(
            function(bookmark){
            	return bookmarkService.deleteBookmark(bookmark.getUrl());
            }
        ).then(
    		function(deletedBookmarkUrl) {
                dom.setText("json", json.jsonBeanStringify({ deletedBookmarkUrl : deletedBookmarkUrl }));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );			        
    }
);