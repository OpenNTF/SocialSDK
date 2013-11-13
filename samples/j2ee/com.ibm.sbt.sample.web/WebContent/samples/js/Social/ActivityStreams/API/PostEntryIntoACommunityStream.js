require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
    var communityID = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
	var postData = { //creating post data
			actor:{
				id:"@me"
			},
			object:{
				id:"objectid",
				displayName:"IBM software universe event posted into communitiy"
			},
			verb:"invite"
		};
    var promise = acticityStreamService.postEntry(
    	postData,
    	ASConstants.ASUser.COMMUNITY+communityID,
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
