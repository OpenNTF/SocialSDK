require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,lang,dom,json,xml) {
    
    var endpoint = config.findEndpoint("connections");
    var results = [];
    
    var cnUrl = "/files/basic/anonymous/api/documents/feed?visibility=public";
    
    endpoint.request(cnUrl, { method : "GET" }).then(
        function(response) {
            dom.setText("json", response);
        }, 
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    
});
