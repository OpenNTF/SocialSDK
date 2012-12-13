require([ "sbt/dom", "sbt/Endpoint" ], function(dom, Endpoint) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl : "/profiles/atom/profile.do",
        handleAs : "text",
        content : {
            email : "%{sample.id1}"
        },
        load : function(response) {
            dom.byId("content").appendChild(dom.createTextNode(response));
        },
        error : function(error) {
            dom.byId("content").appendChild(dom.createTextNode(error));
        }
    });
});