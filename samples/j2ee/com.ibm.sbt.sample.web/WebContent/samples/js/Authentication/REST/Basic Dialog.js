require(["sbt/Endpoint", "sbt/dom"], function(Endpoint, dom) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl : "/connections/opensocial/basic/rest/people/@me/",
        handleAs : "json",
        loginUi : "dialog",
        preventCache : true,
        load: function(response) {
            dom.setText("content", "Successfully logged in");
        },
        error: function(error){
            dom.setText("content", "Failed log in");
        }
  });
});