require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,CommunityService) {
        var communityService = new CommunityService();
        var promise = communityService.getMyInvites();
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
