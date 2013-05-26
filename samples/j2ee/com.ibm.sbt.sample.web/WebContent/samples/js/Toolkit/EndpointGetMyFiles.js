require([ "sbt/Endpoint", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(Endpoint,lang,dom,json,xml) {
    
    var endpoint = Endpoint.find("smartcloudBasic");
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
