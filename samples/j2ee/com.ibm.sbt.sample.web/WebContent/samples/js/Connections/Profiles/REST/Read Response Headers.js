require([ "sbt/dom", "sbt/Endpoint", "sbt/config" ], function(dom, Endpoint) {
    var endpoint = Endpoint.find("connections");
    

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