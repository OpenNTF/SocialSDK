require(["sbt/connections/BookmarkService", "sbt/dom"], 
    function(BookmarkService, dom) {
	    var createRow = function(tag) {
	        var table = dom.byId("bookmarksTable");
	        var tr = document.createElement("tr");
	        table.appendChild(tr);
	        var td = document.createElement("td");
	        td.appendChild(dom.createTextNode(tag.getTerm()));
	        tr.appendChild(td);
	        td = document.createElement("td");
	        td.appendChild(dom.createTextNode(tag.getFrequency()));
	        tr.appendChild(td);
	    };
    
    	var bookmarkService = new BookmarkService();
    	bookmarkService.getBookmarksTags({ ps: 5 }).then(
            function(tags){
                if (tags.length == 0) {
                    text = "bookmarks tags returned no results.";
                    dom.setText("content", text);
                } else {
                    for(var i=0; i<tags.length; i++){
                        var tag = tags[i];
                        createRow(tag);
                    }
                }
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);