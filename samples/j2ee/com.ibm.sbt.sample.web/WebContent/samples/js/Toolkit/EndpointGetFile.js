require([ "sbt/Endpoint", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(Endpoint,lang,dom,json,xml) {
    
    var endpoint = Endpoint.find("connections");
    var results = [];
    
    endpoint.request("files/basic/api/myuserlibrary/document/%{sample.fileId}/entry", { method : "GET" }).then(
        function(response) {
            dom.setText("json", response || "<empty>");
        }, 
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    
});
