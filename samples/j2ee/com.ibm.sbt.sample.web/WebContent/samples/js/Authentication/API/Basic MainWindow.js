require(["sbt/dom", "sbt/config"], function(dom, config) {
    var endpoint = config.findEndpoint("connections");
    
    config.Properties["loginUi"] = "mainWindow";
    
    endpoint.authenticate({ forceAuthentication: false }).then(
    	function(response){
            dom.setText("content", "Successfully logged in");    
        },
        function(response){
            dom.setText("content", "Cancelled log in");
        }      
    );
});