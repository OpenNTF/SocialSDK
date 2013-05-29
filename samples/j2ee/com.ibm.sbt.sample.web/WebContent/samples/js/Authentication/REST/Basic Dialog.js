require(["sbt/Endpoint", "sbt/dom"], function(Endpoint, dom) {
    var endpoint = Endpoint.find("connections");
    
    var url = "/connections/opensocial/basic/rest/people/@me/";
    
    var options = { 
    	method : "GET", 
    	handleAs : "json",
    	preventCache : true 
    };
    
    endpoint.request(url, options).then(
    	function(response) {
            dom.setText("content", "Successfully logged in");
        },
        function(error){
            dom.setText("content", "Failed log in");
        }
    );
});