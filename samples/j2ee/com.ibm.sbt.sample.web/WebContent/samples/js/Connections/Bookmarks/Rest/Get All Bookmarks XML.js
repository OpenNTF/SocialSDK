require(["sbt/dom", "sbt/config"], function(dom) {
    var ep = sbt.Endpoints['connections'];
    ep.xhrGet({
        serviceUrl : "/dogear/atom",
        handleAs : "text",
        load : function(response) {
            dom.byId("content").appendChild(dom.createTextNode(response));
        },
        error : function(error) {
            dom.byId("content").appendChild(
                    dom.createTextNode("Error: " + error));
        }
    });
});