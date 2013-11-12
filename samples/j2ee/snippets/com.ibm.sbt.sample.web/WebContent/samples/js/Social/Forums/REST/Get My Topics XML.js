require([ "sbt/config","sbt/dom" ], function(config,dom) {
	var endpoint = config.findEndpoint("connections");
    
    var url = "/forums/atom/topics/my";
    
    var options = { 
        method : "GET", 
        handleAs : "text"
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