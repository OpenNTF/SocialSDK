require([ "sbt/connections/CommunityService", "sbt/connections/CommunityConstants", "sbt/dom", "sbt/json" ], function(CommunityService,consts,dom,json) {
    try {
        var communityService = new CommunityService();
        var community = communityService.newCommunity();
        community.setCommunityUuid("%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}").setCommunityType("public").setTitle("%{name=sample.communityTitle}").setContent("%{name=sample.communityContent}");
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
