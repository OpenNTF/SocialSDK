require(["sbt/dom", "sbt/Endpoint", "sbt/config" ], function(dom, Endpoint) {
    var endpoint = Endpoint.find("connections");
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : {
        	userid : "%{sample.id1}"
        }
    };
        
    endpoint.request("/profiles/atom/profile.do", options).then(
        function(response) {
        	dom.setText("content", response);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});