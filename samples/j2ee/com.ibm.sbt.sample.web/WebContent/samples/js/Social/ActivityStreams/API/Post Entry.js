require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
	var postData = { //creating post data
			actor:{
				id:"@me"
			},
			object:{
				id:"objectid",
				displayName:"IBM software universe event"
			},
			verb:"invite"
		};
    var promise = acticityStreamService.postEntry(
    	postData,
		ASConstants.ASUser.PUBLIC,
		ASConstants.ASGroup.ALL,
		ASConstants.ASApplication.ALL
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
