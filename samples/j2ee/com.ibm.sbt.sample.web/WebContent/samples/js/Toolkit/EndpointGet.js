require([ "sbt/Endpoint", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(Endpoint,lang,dom,json,xml) {
    
    var options = { 
        method : "GET", 
        query : {
            communityUuid : "%{sample.communityId}"
        }
    };
    
    var endpoint = Endpoint.find("connections");
    var promise = endpoint.request("/communities/service/atom/community/instance", options);

    var results = [];
    promise.then(
        function(data) {
            results.push({data : data || "<empty>"});
            dom.setText("json", json.jsonBeanStringify(results));
        }, 
        function(error) {
            results.push(error);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );    
    promise.response.then(
        function(response) {
            results.push(response);
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(response) {
            results.push(response);
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    
});
