dojo.require("sbt.config");
dojo.require("");
dojo.require("");
dojo.require("");
dojo.require("");

require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,lang,dom,json,xml) {
    
    var options = { 
        method : "GET", 
        query : {
            communityUuid : "%{name=sample.communityId|label=communityId|helpSnippetId=Social_Communities_Get_My_Communities}"
        }
    };
    
    var endpoint = config.findEndpoint("connections");
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
