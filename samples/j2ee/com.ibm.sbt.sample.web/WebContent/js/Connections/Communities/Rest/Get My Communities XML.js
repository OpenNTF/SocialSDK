require([ "sbt/dom", "sbt/config", "sbt/Endpoint" ], function(dom, config, Endpoint) {
    config.Properties["loginUi"] = "dialog";
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl : "/communities/service/atom/communities/my?ps=5",
        handleAs : "text",
        load : function(response) {
            dom.byId("content").appendChild(dom.createTextNode(response));
        },
        error : function(error) {
            dom.byId("content").appendChild(dom.createTextNode(error));
        }
    });
});