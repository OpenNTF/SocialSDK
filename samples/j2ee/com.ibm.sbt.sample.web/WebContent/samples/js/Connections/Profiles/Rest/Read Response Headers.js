require([ "sbt/dom", "sbt/Endpoint", "sbt/config" ], function(dom, Endpoint) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl : "/profiles/atom/profile.do",
        handleAs : "text",
        content : {
            email : "%{sample.id1}"
        },
        load : function(response, ioargs) {
            var headers = ioargs.headers;
            for(var header in headers) {
                if (headers.hasOwnProperty(header)) {
                    var p = document.createElement("p");
                    p.appendChild(dom.createTextNode(header + "=" + headers[header]));
                    dom.byId("content").appendChild(p);
                }
            }
        },
        error : function(error) {
            dom.byId("content").appendChild(dom.createTextNode(error));
        }
    });
});