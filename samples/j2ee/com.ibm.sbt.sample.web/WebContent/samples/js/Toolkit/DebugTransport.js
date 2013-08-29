require([ "sbt/dom", "sbt/json", "sbt/DebugTransport" ], function(dom,json,DebugTransport) {
        var transport = new DebugTransport();
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
