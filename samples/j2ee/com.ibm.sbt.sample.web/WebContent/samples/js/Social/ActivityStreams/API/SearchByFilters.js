require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
    var userID = "%{name=sample.id1|helpSnippetId=Social_Profiles_Get_Profile}";
    var promise = acticityStreamService.searchByFilters(
    		"[" +
				"{" +
					"'type':'tag'," +
					"'values':['test','mobile']" +
				"}" +
			"]"
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
