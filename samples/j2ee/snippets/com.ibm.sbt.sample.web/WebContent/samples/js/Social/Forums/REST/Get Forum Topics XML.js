require([ "sbt/config","sbt/dom" ], function(config,dom) {
	var endpoint = config.findEndpoint("connections");
    
    var url = "/forums/atom/topics";
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : { forumUuid : "%{name=ForumService.forumUuid}" }
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