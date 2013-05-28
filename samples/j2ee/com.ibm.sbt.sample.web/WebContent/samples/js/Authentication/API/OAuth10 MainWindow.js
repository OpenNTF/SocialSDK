require(["sbt/Endpoint", "sbt/dom", "sbt/config"], function(Endpoint, dom, config) {
    var endpoint = Endpoint.find("smartcloud");
    
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