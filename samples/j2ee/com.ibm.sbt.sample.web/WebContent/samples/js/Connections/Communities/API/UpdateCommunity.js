require([ "sbt/connections/CommunityService", "sbt/connections/CommunityConstants", "sbt/dom", "sbt/json" ], function(CommunityService,consts,dom,json) {
    var results = [];
    try {
        var communityService = new CommunityService();
        var community = communityService.newCommunity();
        community.setCommunityUuid("%{sample.communityId}").setCommunityType("public").setTitle("%{sample.communityTitle}").setContent("%{sample.communityContent}");
        var promise = communityService.updateCommunity(community);
        promise.then(function(response) {
            results[0] = response;
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(error) {
            results[0] = error;
            dom.setText("json", json.jsonBeanStringify(results));
        });

        var communityJson = {
            type : consts.Public,
            communityUuid : "%{sample.communityId}",
            communityType : "public",
            title : "%{sample.communityTitle} - 1",
            content : "%{sample.communityContent} - 1",
            tags : [ "tag1", "tag2", "tag3" ]
        };
        community = communityService.newCommunity(communityJson);
        promise = community.update();
        promise.then(function(response) {
            results[1] = response;
            dom.setText("json", json.jsonBeanStringify(results));
        }, function(error) {
            results[1] = error;
            dom.setText("json", json.jsonBeanStringify(results));
        });
    } catch (error) {
        results[2] = error;
        dom.setText("json", json.jsonBeanStringify(results));
    }

});
