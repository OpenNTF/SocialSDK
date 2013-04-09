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
            var photo = xpath.selectText(doc, "/a:feed/a:entry/a:content/h:div/h:span/h:div/h:img[@class='photo']/@src", conn.namespaces);
            dom.create("img", {
                src : photo
            }, dom.byId("content"));
        },
        error : function(error) {
            dom.byId("content").appendChild(document.createTextNode(error));
        }
    });
});