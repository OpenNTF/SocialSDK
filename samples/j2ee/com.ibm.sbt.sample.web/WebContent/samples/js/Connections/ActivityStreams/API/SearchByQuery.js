require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
    var userID = "%{sample.id1}";
    var promise = acticityStreamService.searchByQuery(
		"community"
	);
    promise.then(
        function(as) {
            dom.setText("json", json.jsonBeanStringify(as));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );
    
});
