require([ "sbt/dom", "sbt/config", "sbt/Endpoint" ], function(dom, config, Endpoint) {
    config.Properties["loginUi"] = "dialog";
    
    var endpoint = Endpoint.find("connections");
    
    endpoint.request("/communities/service/atom/communities/my?ps=5").then(
        function(response) {
        	dom.setText("content", response);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});