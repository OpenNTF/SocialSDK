require(["sbt/connections/BookmarkService", "sbt/dom"], 
    function(BookmarkService, dom) {
	    var createRow = function(bookmark) {
	        var table = dom.byId("bookmarksTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        td.appendChild(dom.createTextNode(bookmark.getTitle()));
	        tr.appendChild(td);
	        td = document.createElement("td");
	        td.appendChild(dom.createTextNode(bookmark.getUrl()));
	        tr.appendChild(td);
	    };
	    var userID = "%{name=sample.id1}";
    	var bookmarkService = new BookmarkService();
    	bookmarkService.getAllBookmarks({ ps: 5, userid : userID }).then(
            function(bookmarks){
                if (bookmarks.length == 0) {
                    text = "All bookmarks returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<bookmarks.length; i++){
                        var bookmark = bookmarks[i];
                        createRow(bookmark);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);