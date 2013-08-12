require([ "sbt/connections/CommunityService", "sbt/connections/CommunityConstants", "sbt/dom", "sbt/json" ], function(CommunityService,consts,dom,json) {
    try {
        var communityService = new CommunityService();
        var community = communityService.newCommunity();
        community.setCommunityUuid("%{sample.communityId}").setCommunityType("public").setTitle("%{sample.communityTitle}").setContent("%{sample.communityContent}");
        var promise = communityService.updateCommunity(community);
        promise.then(function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        });
    } catch (error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }

});
