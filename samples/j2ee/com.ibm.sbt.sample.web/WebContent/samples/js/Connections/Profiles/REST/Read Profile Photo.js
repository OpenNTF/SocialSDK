require([ "sbt/connections/ConnectionsConstants", "sbt/dom", "sbt/xml", "sbt/xpath", "sbt/Endpoint" ], function(conn, dom, xml, xpath, Endpoint) {
    var endpoint = Endpoint.find("connections");

    var options = { 
        method : "GET", 
        handleAs : "text",
        query : {
        	userid : "%{sample.id1}"
        }
    };
        
    endpoint.request("/profiles/atom/profile.do", options).then(
        function(response) {
            var doc = xml.parse(response);
            var photo = xpath.selectText(doc, "/a:feed/a:entry/a:content/h:div/h:span/h:div/h:img[@class='photo']/@src", conn.Namespaces);
            dom.create("img", {
                src : photo
            }, dom.byId("content"));
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});