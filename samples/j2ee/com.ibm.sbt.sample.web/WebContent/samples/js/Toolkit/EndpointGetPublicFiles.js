require([ "sbt/Endpoint", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(Endpoint,lang,dom,json,xml) {
    
    var endpoint = Endpoint.find("connections");
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
