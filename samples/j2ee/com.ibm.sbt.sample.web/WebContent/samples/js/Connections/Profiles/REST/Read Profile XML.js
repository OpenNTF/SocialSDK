require(["sbt/dom", "sbt/Endpoint", "sbt/config" ], function(dom, Endpoint) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl : "/profiles/atom/profile.do",
        handleAs : "text",
        content : {
            email : "%{sample.email1}",
            format : "lite", // or "full"
            output : "hcard" // or "vcard"
        },
        load : function(response) {
            dom.setText("xml", response);
        },
        error : function(error) {
            dom.setText("xml", json.jsonBeanStringify(error));
        }
    });
});