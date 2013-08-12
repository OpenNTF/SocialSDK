require(["sbt/connections/CommunityService", "sbt/dom", "sbt/json"], 
    function(CommunityService, dom, json) {
    	var communityService = new CommunityService();	
    	var communityUuid = "%{sample.communityId2}";	
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
