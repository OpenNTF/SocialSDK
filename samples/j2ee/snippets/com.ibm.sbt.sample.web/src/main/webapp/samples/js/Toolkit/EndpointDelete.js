require([ "sbt/config", "sbt/lang", "sbt/dom", "sbt/json", "sbt/xml" ], 
    function(config,lang,dom,json,xml) {
    console.log('A------------------');
    var options = { 
        method : "DELETE", 
        query : {
            communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}"
        }
    };
    console.log('B------------------');
    var endpoint = config.findEndpoint("connections");
    var promise = endpoint.request("/communities/service/atom/community/instance", options);
    console.log('C------------------');
    var results = [];
    console.log('D------------------');
    promise.then(
        function(data) {
        	console.log('L------------------');
            results.push({data : data || "<empty>"});
            console.log('M------------------' + data);
            console.log('M------------------' + Object.keys(data));
            console.log('M------------------' + data.getClass().toString());
            console.log('M------------------' + data.toString());
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(error) {
            results.push(error);
            console.log('N------------------');
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );
    console.log('E------------------');
    promise.response.then(
        function(response) {
        	console.log('F------------------');
            results.push(response);
            console.log('G------------------' + response);
            console.log('G------------------' + Object.keys(response));
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(response) {
        	console.log('H------------------');
            results.push(response);
            console.log('I------------------');
            dom.setText("json", json.jsonBeanStringify(results));
        }
    );


});
