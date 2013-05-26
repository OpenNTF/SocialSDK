require([ "sbt/Endpoint", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(Endpoint,lang,dom,json,xml) {
    
    var endpoint = Endpoint.find("connections");

    // Making a request which returns a promise
    
    var url = "/communities/service/atom/communities/my";
    
    var options = { 
        method: "GET" , 
        handleAs : "text",
        query : {
            email : "%{sample.email1}"
        },
        loginUi : "dialog"
    };
    
    endpoint.request(url, options).then(
        function(data) {
            dom.setText("json", data);
        }, function(err) {
            dom.setText("json", json.jsonBeanStringify(err));
        }
    );
    
});
