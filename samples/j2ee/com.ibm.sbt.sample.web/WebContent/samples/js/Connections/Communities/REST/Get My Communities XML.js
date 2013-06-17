require([ "sbt/dom", "sbt/config" ], function(dom, config) {
    config.Properties["loginUi"] = "dialog";
    
    var endpoint = config.findEndpoint("connections");
    
    endpoint.request("/communities/service/atom/communities/my?ps=5").then(
        function(response) {
        	dom.setText("content", response);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});