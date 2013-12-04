require(["sbt/connections/BookmarkService", "sbt/dom"], 
    function(BookmarkService, dom) {
    
    	var bookmarkService = new BookmarkService();
    	var promise = bookmarkService.getServiceConfigEntries();
    	promise.then(
            function(){
                    dom.setText("emailConfigValue", promise.summary.isEmailExposed);
            },
            function(error){
                dom.setText("content", "Error code:" +  error.code + ", message:" + error.message);
            }       
    	);
    }
);