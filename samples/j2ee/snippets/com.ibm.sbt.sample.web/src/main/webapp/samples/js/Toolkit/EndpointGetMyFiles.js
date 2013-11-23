require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,lang,dom,json,xml) {
    
    var endpoint = config.findEndpoint("smartcloudBasic");
    var results = [];
    
    endpoint.request("/files/basic/api/myuserlibrary/feed", { method : "GET" }).then(
        function(response) {
            dom.setText("json", response);
        }, 
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    
});
