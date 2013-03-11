require(["sbt/Endpoint", "sbt/dom", "sbt/config"], function(Endpoint, dom, config) {
    var ep = Endpoint.find("smartcloud");
    config.Properties["loginUi"] = "mainWindow";
    ep.authenticate({
        forceAuthentication: true,
        success: function(response){
            dom.setText("content", "Successfully logged in");    
        }
    });
});