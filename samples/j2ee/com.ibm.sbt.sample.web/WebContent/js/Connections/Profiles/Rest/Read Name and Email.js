require([ "sbt/connections/core", "sbt/dom", "sbt/xml", "sbt/xpath", "sbt/Endpoint" ], function(conn, dom, xml, xpath, Endpoint) {
    var endpoint = Endpoint.find("connections");
    endpoint.xhrGet({
        serviceUrl : "/profiles/atom/profile.do",
        handleAs : "text",
        content : {
            userid : "%{sample.id1}"
        },
        load : function(response) {
            var doc = xml.parse(response);
            var name = xpath.selectText(doc, "/a:feed/a:entry/a:contributor/a:name", conn.namespaces);
            var email = xpath.selectText(doc, "/a:feed/a:entry/a:contributor/a:email", conn.namespaces);
            dom.setText("content", "Name:" + name + ", email:" + email);
        },
        error : function(error) {
            dom.setText("content", error);
        }
    });
});