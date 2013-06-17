require([ "sbt/dom", "sbt/config" ], function(dom, config) {
    var endpoint = config.findEndpoint("connections");
    

    var options = { 
        method : "GET", 
        handleAs : "text",
        query : {
        	userid : "%{sample.id1}"
        }
    };
        
    endpoint.request("/profiles/atom/profile.do", options).response.then(
        function(response) {
        	dom.setText("content", "Content-Type: " + response.getHeader("Content-Type"));
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});