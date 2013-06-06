require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
    var userID = "%{sample.id1}";
    var postData = { //creating post data
		content: "A test update @ "+new Date()
	};
    var promise = acticityStreamService.postMicroblogEntry(
		postData,
    	userID,
		ASConstants.ASGroup.ALL
    );
    promise.then(
        function(newEntry) {
            dom.setText("json", json.jsonBeanStringify(newEntry));
        },
        function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        }
    );    
});
