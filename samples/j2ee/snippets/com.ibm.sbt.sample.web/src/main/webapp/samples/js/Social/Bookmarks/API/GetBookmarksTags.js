require(["sbt/connections/BookmarkService", "sbt/dom", "sbt/json"], 
    function(BookmarkService, dom, json) {
        var bookmarkService = new BookmarkService();
        var bookmark = bookmarkService.newBookmark(); 
        var now = new Date();
        bookmark.setTitle("Bookmark at " + now.getTime());
        bookmark.setUrl("ibm.com");
        bookmark.setTags(['bookmarktest', 'test2bookmark']);
    	bookmarkService.createBookmark(bookmark).then(
            function(bookmark){
            	return bookmarkService.getBookmarksTags();
            }
        ).then(
			function(tags) {
				for(var i=0;i<tags.length;i++){
					if(tags[i].getTerm() == "bookmarktest"){
						dom.setText("json", json.jsonBeanStringify({ tagTerm : tags[i].getTerm() }));
						break;
					}
				}
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
    	);			        		        
    }
);