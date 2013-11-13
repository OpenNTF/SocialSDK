require([ "sbt/dom", "sbt/json", "sbt/MockTransport" ], function(dom,json,MockTransport) {
        var transport = new MockTransport();
        var options = {
            method : "GET",
            handleAs : "text",
            query : { asc : true , page : 1 , ps : 2 }
        };
		var promise = transport.request("https://localhost:8443/sbt.sample.web/service/proxy/connections/forums/atom/forums/my", options);
        promise.response.then(
            function(response) {
                dom.setText("json", json.jsonBeanStringify(response));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
