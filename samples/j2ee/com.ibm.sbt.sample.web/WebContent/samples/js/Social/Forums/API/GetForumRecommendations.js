require([ "sbt/dom", "sbt/json", "sbt/connections/ForumService" ], function(dom,json,ForumService) {
        var forumService = new ForumService();
        var promise = forumService.getForumRecomendations("%{name=ForumService.postUuid}");
        promise.then(
            function(recomendations) {
                dom.setText("json", json.jsonBeanStringify(recomendations));
            },
            function(error) {
                dom.setText("json", json.jsonBeanStringify(error));
            }
        );
    }
);
