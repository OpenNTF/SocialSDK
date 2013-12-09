require(["sbt/connections/BookmarkService", "sbt/dom", "sbt/json"], 
    function(BookmarkService, dom, json) {
        var bookmarkService = new BookmarkService();
    	var promise = bookmarkService.getServiceConfigEntries();
    	promise.then(
       		function(serviceConfigs) {
                dom.setText("json", json.jsonBeanStringify(serviceConfigs));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
       ); 
	}
);