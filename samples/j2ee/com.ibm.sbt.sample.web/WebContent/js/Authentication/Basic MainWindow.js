require(["sbt/Endpoint", "sbt/dom", "sbt/config"], function(Endpoint, dom, config) {
    var ep = Endpoint.find("connections");
    config.Properties["loginUi"] = "mainWindow";
    ep.authenticate({
        forceAuthentication: true,
        success: function(response){
            dom.setText("content", "Successfully logged in");    
        }
    });
});