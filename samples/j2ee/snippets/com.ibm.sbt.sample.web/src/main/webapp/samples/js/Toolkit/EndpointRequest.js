require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,lang,dom,json,xml) {
    
    var endpoint = config.findEndpoint("connections");

    // Making a request which returns a promise
    
    var url = "/communities/service/atom/communities/my";
    
    var options = { 
        method: "GET" , 
        handleAs : "text"
    };
    
    endpoint.request(url, options).then(
        function(data) {
            dom.setText("json", data);
        }, function(err) {
            dom.setText("json", json.jsonBeanStringify(err));
        }
    );
    
});
