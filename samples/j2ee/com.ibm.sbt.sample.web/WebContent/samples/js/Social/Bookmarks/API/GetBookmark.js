require(["sbt/connections/BookmarkService", "sbt/dom", "sbt/json"], 	
    function(BookmarkService, dom, json) {
        var bookmarkService = new BookmarkService(); 
        	var bookmark = bookmarkService.newBookmark(); 
        	var now = new Date();
            bookmark.setTitle("Bookmark at " + now.getTime());
            bookmark.setUrl("ibm.com");
            bookmarkService.createBookmark(bookmark).then(   //getting first bookmark by setting page size to 1
            function(bookmark){
            	return bookmarkService.getBookmark(bookmark.getUrl());
            }
    	).then(
			function(bookmark) {
				dom.setText("json", json.jsonBeanStringify(bookmark.toJson()));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
         );
	}
);