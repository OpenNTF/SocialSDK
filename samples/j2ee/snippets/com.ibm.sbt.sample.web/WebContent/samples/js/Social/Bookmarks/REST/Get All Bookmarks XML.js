require([ "sbt/dom","sbt/config" ], function(dom,config) {
	var endpoint = config.findEndpoint("connections");
    
    var url = "/dogear/atom";
    
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