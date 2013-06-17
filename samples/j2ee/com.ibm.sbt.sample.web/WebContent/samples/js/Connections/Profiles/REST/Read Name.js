require([ "sbt/connections/ConnectionsConstants", "sbt/dom", "sbt/xml", "sbt/xpath", "sbt/config" ], function(conn, dom, xml, xpath, config) {
    var endpoint = config.findEndpoint("connections");

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
            var name = xpath.selectText(doc, "/a:feed/a:entry/a:contributor/a:name", conn.Namespaces);
            dom.setText("content", name);
        },
        function(error){
        	dom.setText("content", error);
        }
    );
});