require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
    	var communityService = new CommunityService();	
    	var communityUuid = "%{name=CommunityService.communityUuid2|helpSnippetId=Social_Communities_Get_My_Communities}";	
        var promise = communityService.deleteCommunity(communityUuid);
        promise.then(
            function(communityUuid) {
                dom.setText("json", json.jsonBeanStringify({ communityUuid : communityUuid }));
            }, function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
