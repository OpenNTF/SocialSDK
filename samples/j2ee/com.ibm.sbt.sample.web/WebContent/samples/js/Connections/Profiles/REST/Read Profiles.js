require(["sbt/Endpoint", "sbt/dom" ], function(Endpoint, dom) {
    var emails = [ "%{sample.email1}", "%{sample.email2}" ];

    var text = "";

    var endpoint = Endpoint.find("connections");
    for (var i=0; i<emails.length; i++) {
        endpoint.xhrGet({
            serviceUrl : "/profiles/atom/profile.do",
            handleAs: "text",
            content: { email: emails[i] },
            handle: function(response) {
                text += "\n" + response;
                dom.setText("xml", text);
            }
        });
    }
});