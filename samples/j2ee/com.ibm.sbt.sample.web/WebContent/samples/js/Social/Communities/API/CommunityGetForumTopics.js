require([ "sbt/dom", "sbt/json", "sbt/connections/CommunityService" ], function(dom,json,ForumService) {
        var communityService = new CommunityService();
        var promise = communityService.getForumTopics("%{sample.communityId}");
        promise.then(
            function(topics) {
                dom.setText("json", json.jsonBeanStringify(topics));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
