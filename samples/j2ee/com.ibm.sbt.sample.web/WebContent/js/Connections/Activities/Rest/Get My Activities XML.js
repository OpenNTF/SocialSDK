require(["sbt/config","sbt/dom"], function(sbt, dom) {
    var ep = sbt.Endpoints['connections'];
    ep.xhrGet({
        serviceUrl : "/activities/service/atom2/activities",
        handleAs : "text",
        content : {
            email : "%{sample.email1}"
        },
        load : function(response) {
            dom.byId("content").appendChild(dom.createTextNode(response));
        },
        error : function(error) {
            dom.byId("content").appendChild(
                    dom.createTextNode("Error: " + error));
        }
    });
});