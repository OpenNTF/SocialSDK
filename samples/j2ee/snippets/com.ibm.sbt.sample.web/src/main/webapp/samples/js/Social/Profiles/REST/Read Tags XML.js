require(["sbt/dom", "sbt/config" ], function(dom, config) {
    var endpoint = config.findEndpoint("connections");
    
    var options = { 
        method : "GET", 
        handleAs : "text",
        query : {
        	format : "full",
        	targetEmail : "%{name=sample.email1}"
        }
    };
        
    endpoint.request("/profiles/atom/profileTags.do", options).then(
        function(response) {
        	dom.setText("content", response);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});