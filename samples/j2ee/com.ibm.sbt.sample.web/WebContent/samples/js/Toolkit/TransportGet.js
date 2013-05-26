require([ "sbt/Endpoint", "sbt/_bridge/Transport", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(Endpoint,Transport,lang,dom,json,xml) {
    
    var endpoint = Endpoint.find("connections");
    
    var url = "/communities/service/atom/community/instance";
    url = endpoint.proxy.rewriteUrl(endpoint.baseUrl, url, endpoint.proxyPath);
    
    var options = { 
        method: "GET" , 
        handleAs : "text",
        query : {
            communityUuid : "%{sample.communityId}"
        }
    };
    
    var transport = new Transport();
    var promise = transport.request(url, options);
    
    var results = [];
    promise.then(
        function(data) {
            results.push({data : data || "<empty>"});
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(error) {
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
