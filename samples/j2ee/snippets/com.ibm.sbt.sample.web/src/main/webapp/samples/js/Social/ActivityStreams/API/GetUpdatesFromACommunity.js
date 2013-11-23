require([ "sbt/dom", "sbt/json", "sbt/connections/ActivityStreamService", "sbt/connections/ActivityStreamConstants" ], function(dom,json,ActivityStreamService, ASConstants) {
    var acticityStreamService = new ActivityStreamService();
    var communityID = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
    var promise = acticityStreamService.getUpdatesFromCommunity(
    	communityID,
		{
			count: 5
		}
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
