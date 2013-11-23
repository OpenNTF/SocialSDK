require([ "sbt/connections/CommunityService", "sbt/connections/CommunityConstants", "sbt/dom", "sbt/json" ], function(CommunityService,consts,dom,json) {
    try {
        var communityService = new CommunityService();
        var communityJson = {
            type : consts.Public,
            communityUuid : "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}",
            communityType : "public",
            title : "%{name=sample.communityTitle}",
            content : "%{name=sample.communityContent}",
            tags : [ "tag1", "tag2", "tag3" ]
        };
        var community = communityService.newCommunity(communityJson);
        var promise = community.update();
        promise.then(function(response) {
            dom.setText("json", json.jsonBeanStringify(response));
        }, function(error) {
            dom.setText("json", json.jsonBeanStringify(error));
        });
    } catch (error) {
        dom.setText("json", json.jsonBeanStringify(error));
    }

});
