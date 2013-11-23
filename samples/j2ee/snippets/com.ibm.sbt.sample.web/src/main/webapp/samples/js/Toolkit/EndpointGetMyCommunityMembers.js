require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/xml", "sbt/xpath", "sbt/connections/ConnectionsConstants" ], 
    function(config,lang,dom,xml,xpath,conn) {
    
    var endpoint = config.findEndpoint("connections");
    
    var members = "";
    
    endpoint.request("/communities/service/atom/communities/my", { method : "GET" }).then(
        function(response) {
        	var doc = xml.parse(response);
			var nodes = xpath.selectNodes(doc,"//snx:communityUuid",conn.Namespaces);
			for (var i in nodes) {
				var node = nodes[i];
				var communityUuid = (node.text || node.textContent);
				members += "{communityUuid:"+communityUuid+"}\n";
				dom.setText("content", members);
				endpoint.request("communities/service/atom/community/members?communityUuid="+communityUuid, { method : "GET" }).then(
					function(response1) {
						var doc1 = xml.parse(response1);
						var totalResults = xpath.selectText(doc1,"/a:feed/opensearch:totalResults",conn.Namespaces);
						var selfUrl = xpath.selectText(doc1,"/a:feed/a:link[@rel='self']/@href",conn.Namespaces);
						members += "{selfUrl:"+selfUrl+",total:"+totalResults+"}\n";
						dom.setText("content", members);
					},
					function(error) {
						dom.setText("content", error);
					}
				);
			}
        }, 
        function(error) {
            dom.setText("content", error);
        }
    );
    
});
