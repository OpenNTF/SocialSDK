require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
        var communityService = new CommunityService();
        var communityId = "%{name=CommunityService.communityUuid|helpSnippetId=Social_Communities_Get_My_Communities}";
        var promise = communityService.getAllInvites(communityId);
        promise.then(
            function(invites) {
                dom.setText("json", json.jsonBeanStringify(invites));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
