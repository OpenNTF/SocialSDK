require([ "sbt/dom", "sbt/json", "sbt/_bridge/Transport" ], function(dom,json,Transport) {
        var transport = new Transport();
		var promise = transport.request("https://localhost:8443/sbt.sample.web/service/proxy/connections/forums/atom/forums/my?asc=true&page=1&ps=2", {});
        promise.then(
            function(response) {
                dom.setText("json", json.jsonBeanStringify(response));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
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
