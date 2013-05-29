require([ "sbt/dom","sbt/Endpoint" ], function(dom,Endpoint) {
	var endpoint = Endpoint.find("connections");
    
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