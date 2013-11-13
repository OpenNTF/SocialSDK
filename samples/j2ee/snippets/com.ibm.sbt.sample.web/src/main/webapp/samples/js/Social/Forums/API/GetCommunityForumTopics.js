require([ "sbt/dom", "sbt/json", "sbt/connections/ForumService" ], function(dom,json,ForumService) {
        var forumService = new ForumService();
        var promise = forumService.getForumTopics("%{ForumService.forumUuid}", "%{ForumService.communityUuid}");
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
