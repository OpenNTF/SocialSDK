require([ "sbt/config","sbt/dom" ], function(config,dom) {
	var endpoint = config.findEndpoint("connections");
    
    var url = "/forums/atom/replies";
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : { topicUuid : "%{name=ForumService.topicUuid}" }
    };
    
    endpoint.request(url, options).then(
    	function(response) {
    		dom.setText("content", response);
        },
        function(error){
            dom.setText("content", error);
        }
    );
});