require([ "sbt/config", "sbt/_bridge/Transport", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,Transport,lang,dom,json,xml) {
    
    var endpoint = config.findEndpoint("connections");
    
    var url = "/communities/service/atom/community/instance";
    url = endpoint.proxy.rewriteUrl(endpoint.baseUrl, url, endpoint.proxyPath);
    
    var options = { 
        method: "GET" , 
        handleAs : "text",
        query : {
            communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}"
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
