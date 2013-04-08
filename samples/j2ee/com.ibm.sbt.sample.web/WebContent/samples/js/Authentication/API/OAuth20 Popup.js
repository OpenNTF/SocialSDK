require(["sbt/Endpoint", "sbt/dom", "sbt/config"], function(Endpoint, dom, config) {
    var ep = Endpoint.find("connectionsOA2");
    config.Properties["loginUi"] = "popup";
    ep.authenticate({
        forceAuthentication: true,
        load: function(response){
            dom.setText("content", "Successfully logged in");    
        }
    });
});